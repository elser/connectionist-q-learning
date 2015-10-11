package gui.options.learning;

import pl.gdan.elsy.qconf.curiosity.CuriousPlayerPerception;

public class PSlopeSlider extends OptionSlider {
    private static final long serialVersionUID = 1L;

    public PSlopeSlider() {
        super(LearningPanel.MAX);
    }

    public String getTitle() {
        return "P slope";
    }

    public double getInputValue() {
        return CuriousPlayerPerception.getPSlope();
    }

    public void setOutputValue(double val) {
        CuriousPlayerPerception.setPSlope(val);
    }

    protected double getScale() {
        return 0.1;
    }

}
