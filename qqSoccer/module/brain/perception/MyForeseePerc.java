package module.brain.perception;

import pl.gdan.elsy.qconf.Perception;

public class MyForeseePerc extends Perception {
    private static final long serialVersionUID = 1L;

    protected void updateInputValues() {
        // we omit the farthest radar input
        double[] percOut = inputPerception.getOutput();
        // TODO: optimize
        for (int i = 2; i < percOut.length; i++) {
            setNextValueAbsolute(percOut[i]);
        }
    }

    public double getReward() {
        return 0;
    }

    public boolean isUnipolar() {
        return inputPerception.isUnipolar();
    }
}
