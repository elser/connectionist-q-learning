package pl.gdan.elsy.qconf.mocks;

import pl.gdan.elsy.qconf.Perception;

/**
 * TODO: Document this class / interface here
 *
 * @since v7.0
 */
public class GamePerceptionMock extends Perception {
    private final int inputsNr;
    private int counter = 0;
    private boolean reward;

    public GamePerceptionMock(int inputsNr) {
        this.inputsNr = inputsNr;
    }

    @Override
    public boolean isUnipolar() {
        return true;
    }

    @Override
    public double getReward() {
        return reward ? 0.8 : 0;
    }

    @Override
    protected void updateInputValues() {
        for (int i = 0; i < inputsNr; i++) {
            setNextValue(getActiveBucket() == i);
        }
        counter++;
    }

    public int getActiveBucket() {
        return counter % inputsNr;
    }

    public void setReward(boolean reward) {
        this.reward = reward;
    }
}
