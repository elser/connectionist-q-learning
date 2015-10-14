package pl.gdan.elsy.qconf;

import org.junit.Before;
import org.junit.Test;
import pl.gdan.elsy.qconf.mocks.GamePerceptionMock;

import java.util.Arrays;

public class SimpleGameTest {

    private GamePerceptionMock perception;
    private Brain brain;

    @Before
    public void setUp() throws Exception {
        final int bucketsNr = 4;
        Action[] actionsArray = new Action[bucketsNr];
        perception = new GamePerceptionMock(bucketsNr);
        brain = new Brain(perception, actionsArray, new int[]{10});
        brain.setAlpha(0.2);
        brain.setGamma(0);
        brain.setLambda(0);
        brain.setRandActionsPercentage(0);
    }

    @Test
    public void testOneNeuron() throws Exception {
        int itersPack = 10000;
        for (int i = 0; i < 20 * itersPack; i++) {
            think(itersPack, i);
        }
        for (int i = 0; i < 4; i++) {
            think(1, i);
        }
    }

    private void think(int itersPack, int i) {
        perception.perceive();
        brain.count();
        if (perception.getActiveBucket() == brain.getAction()) {
            perception.setReward(true);
        } else {
            perception.setReward(false);
        }
        if (i % (itersPack + 1) == 0) {
            System.out.println(perception.getActiveBucket() + " " + brain.getAction() + " rew " + perception.getReward() + " brain=" +
                    Arrays.toString(brain.getOutput()));
        }
    }
}