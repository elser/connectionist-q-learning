package gui.options.learning;

import pl.gdan.elsy.qconf.curiosity.CuriousPlayerPerception;

public class RMaxSlider extends OptionSlider {
    private static final long serialVersionUID = 1L;

    public RMaxSlider() {
        super(LearningPanel.MAX);
    }

    public String getTitle() {
        return "R max";
    }

    public double getInputValue() {
        return CuriousPlayerPerception.getRMax();
    }

    public void setOutputValue(double val) {
        CuriousPlayerPerception.setRMax(val);
    }

}
