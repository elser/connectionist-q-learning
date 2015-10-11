package gui.options.learning;

import pl.gdan.elsy.qconf.curiosity.CuriousPlayerPerception;

public class RSlopeSlider extends OptionSlider {
    private static final long serialVersionUID = 1L;

    public RSlopeSlider() {
        super(LearningPanel.MAX);
    }

    public String getTitle() {
        return "R slope";
    }

    public double getInputValue() {
        return CuriousPlayerPerception.getRSlope();
    }

    public void setOutputValue(double val) {
        CuriousPlayerPerception.setRSlope(val);
    }

    protected double getScale() {
        return 0.1;
    }

}
