package pl.gdan.elsy.qconf.mocks;

import pl.gdan.elsy.qconf.Perception;

/**
 * TODO: Document this class / interface here
 *
 * @since v7.0
 */
public class PerceptionMock extends Perception {
    private double reward;

    @Override
    public boolean isUnipolar() {
        return false;
    }

    @Override
    public double getReward() {
        return reward;
    }

    @Override
    protected void updateInputValues() {

    }

    public void setReward(double reward) {
        this.reward = reward;
    }


}
