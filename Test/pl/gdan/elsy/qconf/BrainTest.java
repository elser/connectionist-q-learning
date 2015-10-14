package pl.gdan.elsy.qconf;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.gdan.elsy.qconf.mocks.PerceptionMock;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;

/**
 * TODO: Document this file here
 */
public class BrainTest {

    private PerceptionMock perception;
    private Brain brain;

    @Before
    public void setUp() throws Exception {
        Action[] actionsArray = new Action[1];
        perception = new PerceptionMock();
        brain = new Brain(perception, actionsArray, new int[]{});
        brain.setAlpha(1);
        brain.setGamma(0.9);
        brain.setLambda(0);
        brain.setRandActionsPercentage(0);
    }

    @Test
    public void testOneNeuron() throws Exception {
        final double reward = 0.1;
        for (int i = 0; i < 100; i++) {
            brain.count();
            perception.setReward(reward);
            perception.getOutput()[0] = 1;
            if (i < 1 || i >= 95) {
                System.out.println(Arrays.toString(perception.getOutput()) + " " + Arrays.deepToString(brain.getW()) + " " + Arrays.toString(brain.getOutput()));
            }
        }
        Assert.assertThat(brain.getOutput()[0], is(closeTo(reward, 0.1)));
    }


    @Test
    public void testCountBoltzman() throws Exception {
        for (int i = -10; i < 10; i++) {
            final double q = 0.1 * i;
            final double boltzman = brain.countBoltzman(q);
            System.out.println(q + " " + boltzman);
        }
    }
}