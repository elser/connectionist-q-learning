package pl.gdan.elsy.qconf.mocks;

import pl.gdan.elsy.qconf.Perception;

/**
 * TODO: Document this class / interface here
 *
 * @since v7.0
 */
public class SimpleGamePerceptionMock extends Perception {
    private final int inputsNr;
    private double reward;
    private int bucket;

    public SimpleGamePerceptionMock(int inputsNr) {
        this.inputsNr = inputsNr;
    }

    @Override
    public boolean isUnipolar() {
        return true;
    }

    @Override
    public double getReward() {
        return reward;
    }

    @Override
    protected void updateInputValues() {
        for (int i = 0; i < inputsNr; i++) {
            setNextValue(getBucket() == i);
        }
    }

    public int getBucket() {
        return bucket;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public void setBucket(int bucket) {
        this.bucket = bucket;
    }
}
