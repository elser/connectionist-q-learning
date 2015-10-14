package pl.gdan.elsy.qconf;

import org.hamcrest.number.IsCloseTo;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import pl.gdan.elsy.qconf.mocks.SimpleGamePerceptionMock;
import pl.gdan.elsy.tool.Average;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CumulativeRewardGameTest {

    private static boolean DEBUG = false;
    private SimpleGamePerceptionMock perception;
    private int bucketsNr = 4;
    Action[] actionsArray = new Action[bucketsNr];

    @Before
    public void setUp() throws Exception {
        perception = new SimpleGamePerceptionMock(bucketsNr);
    }

    @Test
    @Ignore
    public void testBestParams() throws Exception {
        Average avga = new Average(10);
        Average avgg = new Average(10);
        Average avgl = new Average(10);
        Average avgb = new Average(10);
        Brain brain = new Brain(perception, actionsArray, new int[]{});
        brain.setAlpha(0.93);
        brain.setGamma(0.62);
        brain.setLambda(1.07);
        brain.setUseBoltzmann(true);
        brain.setRandActionsPercentage(0);
        for (double boltzmann = 0.01; boltzmann <= 0.1; boltzmann += 0.01) {
            for (double alpha = 0.1; alpha <= 1; alpha += 0.1) {
                for (double gamma = 0.5; gamma <= 0.9; gamma += 0.1) {
                    for (double lambda = 0.5; lambda <= 1.4; lambda += 0.1) {
                        brain.setAlpha(alpha);
                        brain.setGamma(gamma);
                        brain.setLambda(lambda);
                        brain.randomize(123l);
                        brain.setBoltzmanTemperature(boltzmann);
                        Average avgReward = new Average(100);
                        for (int i = 0; i < 10000; i++) {
                            avgReward.put(gameSet(brain));
                        }
                        if (avgReward.get() > 0.75) {
                            System.out.format("a=%.2f g=%.2f l=%.2f b=%.2f rew=%.3f\n",
                                    alpha, gamma, lambda, boltzmann, avgReward.get());
                            avga.put(alpha);
                            avgg.put(gamma);
                            avgl.put(lambda);
                            avgb.put(boltzmann);
                        }
                    }
                }
            }
        }
        System.out.println();
        System.out.format("a=%.2f g=%.2f l=%.2f b=%.2f\n",
                avga.get(), avgg.get(), avgl.get(), avgb.get());
        // for boltzman a=0.42 g=0.58 l=1.22 b=0.06
        // for randActions5 a=0.93 g=0.62 l=1.07
    }

    @Test
    public void testCumulativeRewardGame() throws Exception {
        Brain brain = new Brain(perception, actionsArray, new int[]{});
        brain.setAlpha(0.93); //0.93
        brain.setGamma(0.62); //0.62
        brain.setLambda(1.07); //1.07
        brain.setAlpha(0.2); //0.93
        brain.setGamma(0.5); //0.62
        brain.setLambda(0.5); //1.07
        brain.setUseBoltzmann(false);
        brain.setRandActionsPercentage(5);

        double reward = 0;
        Average avg = new Average(100);
        int iterPack = 100000;
        brain.randomize(123l);
        final int loops = iterPack * 10;
        for (int i = 0; i < loops; i++) {
            brain.setRandActionsPercentage(9 - 10 * i / loops);
            reward = gameSet(brain);
            avg.put(reward);
            DEBUG = false;
            if (i % iterPack == 0) {
                System.out.println(avg + " " + brain.getRandActionsPercentage());
                DEBUG = true;
            }
        }
        assertThat(reward, is(IsCloseTo.closeTo(1, 0.1)));
    }

    private double gameSet(Brain brain) {
        double cumulativeReward = 0;
        for (int i = 0; i < bucketsNr; i++) {
            perception.setReward(0);
            perception.setBucket(i);
            perception.perceive();
            brain.count();
            if (perception.getBucket() == brain.getAction()) {
                cumulativeReward += 1.0 / bucketsNr;
            }
            debug(brain, i, cumulativeReward);
            if (i == bucketsNr - 1) {
                perception.setReward(cumulativeReward);
                brain.count();
                debug(brain, i, cumulativeReward);
                brain.reset();
            }
        }
        return cumulativeReward;
    }

    private void debug(Brain brain, int i, double cumulativeReward) {
        if (DEBUG) {
            System.out.println("bucket=" + i + " action=" + brain.getAction() + " " +
                    Arrays.toString(brain.getOutput()) + cumulativeReward);
        }
    }
}