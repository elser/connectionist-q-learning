package gui.options.learning;

import pl.gdan.elsy.qconf.curiosity.CuriousPlayerPerception;

public class PMaxSlider extends OptionSlider {

    public PMaxSlider() {
        super(LearningPanel.MAX);
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String getTitle() {
        return "P max";
    }

    public double getInputValue() {
        return CuriousPlayerPerception.getPMax();
    }

    public void setOutputValue(double val) {
        CuriousPlayerPerception.setPMax(val);
    }

}
