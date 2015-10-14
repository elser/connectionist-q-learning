package pl.gdan.elsy.qconf;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.gdan.elsy.qconf.mocks.SimpleGamePerceptionMock;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SimpleGameTest {

    private SimpleGamePerceptionMock perception;
    private Brain brain;
    final int bucketsNr = 4;

    @Before
    public void setUp() throws Exception {
        Action[] actionsArray = new Action[bucketsNr];
        perception = new SimpleGamePerceptionMock(bucketsNr);
        brain = new Brain(perception, actionsArray, new int[]{10});
        brain.setAlpha(0.2);
        brain.setGamma(0);
        brain.setLambda(0);
        brain.setRandActionsPercentage(0);
    }

    @Test
    public void testSimpleGame() throws Exception {
        int itersPack = 10000;
        for (int i = 0; i < 20 * itersPack; i++) {
            think(itersPack, i);
        }
        int wins=0;
        for (int i = 0; i < bucketsNr; i++) {
            if(think(1, i)) {
                wins++;
            }
        }
        assertThat(wins, is(bucketsNr));
    }

    private boolean think(int itersPack, int i) {
        perception.setBucket(i % bucketsNr);
        perception.perceive();
        brain.count();
        final boolean wins = perception.getBucket() == brain.getAction();
        if (wins) {
            perception.setReward(0.8);
        } else {
            perception.setReward(0);
        }
        if (i % (itersPack + 1) == 0) {
            System.out.println(perception.getBucket() + " " + brain.getAction() + " rew " + perception.getReward() + " brain=" +
                    Arrays.toString(brain.getOutput()));
        }
        return wins;
    }
}