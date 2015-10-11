package gui.options.learning;

import pl.gdan.elsy.qconf.curiosity.CuriousPlayerPerception;

public class PMinSlider extends OptionSlider {
    private static final long serialVersionUID = 1L;

    public PMinSlider() {
        super(LearningPanel.MAX);
    }

    public String getTitle() {
        return "P min";
    }

    public double getInputValue() {
        return CuriousPlayerPerception.getPMin();
    }

    public void setOutputValue(double val) {
        CuriousPlayerPerception.setPMin(val);
    }

    protected double getScale() {
        return 1000;
    }

}
