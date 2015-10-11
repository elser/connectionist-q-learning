package gui.options.learning;

import pl.gdan.elsy.qconf.curiosity.CuriousPlayerPerception;

public class RMinSlider extends OptionSlider {
    private static final long serialVersionUID = 1L;

    public RMinSlider() {
        super(LearningPanel.MAX);
    }

    public String getTitle() {
        return "R min";
    }

    public double getInputValue() {
        return CuriousPlayerPerception.getRMin();
    }

    public void setOutputValue(double val) {
        CuriousPlayerPerception.setRMin(val);
    }

    protected double getScale() {
        return 1000;
    }
}
