package curiousbot.brain;

import pl.gdan.elsy.qconf.Perception;

public class MyForeseePerc extends Perception {
    private static final int FARTHEST_SIGHT = MyPerception.RADAR_ANGLES * 2 + 1;
    private static final long serialVersionUID = 1L;

    protected void updateInputValues() {
        // we omit the farthest radar input
        double[] percOut = inputPerception.getOutput();
        int predictionOutputSize = percOut.length - FARTHEST_SIGHT;
        // TODO: optimize
        for (int i = 2; i < predictionOutputSize; i++) {
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
