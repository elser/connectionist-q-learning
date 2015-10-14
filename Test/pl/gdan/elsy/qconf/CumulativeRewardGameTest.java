package pl.gdan.elsy.qconf;

import org.hamcrest.number.IsCloseTo;
import org.junit.Before;
import org.junit.Test;
import pl.gdan.elsy.qconf.mocks.SimpleGamePerceptionMock;
import pl.gdan.elsy.tool.Average;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CumulativeRewardGameTest {

    private static boolean DEBUG = false;
    private SimpleGamePerceptionMock perception;
    private Brain brain;
    private int bucketsNr = 4;

    @Before
    public void setUp() throws Exception {
        Action[] actionsArray = new Action[bucketsNr];
        perception = new SimpleGamePerceptionMock(bucketsNr);
        brain = new Brain(perception, actionsArray, new int[]{});
        brain.setAlpha(0.2);
        brain.setGamma(0.5);
        brain.setLambda(0.5);
        brain.setUseBoltzmann(false);
        brain.setBoltzmanTemperature(0.03);
        brain.setRandActionsPercentage(5);
    }

    @Test
    public void testBestParams() throws Exception {
        double max = 0;
        for (double boltzmann = 0.01; boltzmann <= 0.1; boltzmann += 0.01) {
            for (double alpha = 0.3; alpha <= 0.4; alpha += 0.1) {
                for (double gamma = 0.6; gamma <= 0.7; gamma += 0.1) {
                    for (double lambda = 1.3; lambda <= 1.4; lambda += 0.1) {
                        brain.setAlpha(alpha);
                        brain.setGamma(gamma);
                        brain.setLambda(lambda);
                        brain.randomize(123l);
                        brain.setBoltzmanTemperature(boltzmann);
                        Average avgReward = new Average(100);
                        for (int i = 0; i < 10000; i++) {
                            avgReward.put(gameSet());
                        }
                        System.out.format("a=%.2f g=%.2f l=%.2f b=%.2f rew=%.3f\n",
                                alpha, gamma, lambda, boltzmann, avgReward.get());
                    }
                }
            }
        }
    }

    @Test
    public void testSimpleGame() throws Exception {
        double reward = 0;
        Average avg = new Average(100);
        int iterPack = 100000;
        brain.randomize(123l);
        final int loops = iterPack * 10;
        for (int i = 0; i < loops; i++) {
            brain.setRandActionsPercentage(4 - 5 * i / loops);
            reward = gameSet();
            avg.put(reward);
            DEBUG = false;
            if (i % iterPack == 0) {
                System.out.println(avg + " " + brain.getRandActionsPercentage());
                DEBUG = true;
            }
        }
        assertThat(reward, is(IsCloseTo.closeTo(1, 0.1)));
    }

    private double gameSet() {
        double cumulativeReward = 0;
        for (int i = 0; i < bucketsNr; i++) {
            perception.setReward(0);
            perception.setBucket(i);
            perception.perceive();
            brain.count();
            if (perception.getBucket() == brain.getAction()) {
                cumulativeReward += 1.0 / bucketsNr;
            }
            debug(cumulativeReward, i);
            if (i == bucketsNr - 1) {
                perception.setReward(cumulativeReward);
                brain.count();
                debug(cumulativeReward, i);
                brain.reset();
            }
        }
        return cumulativeReward;
    }

    private void debug(double cumulativeReward, int i) {
        if (DEBUG) {
            System.out.println("bucket=" + i + " action=" + brain.getAction() + " " +
                    Arrays.toString(brain.getOutput()) + cumulativeReward);
        }
    }
}