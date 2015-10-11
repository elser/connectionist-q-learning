package gui.options.learning;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public abstract class OptionSlider extends JSlider {
    private NumberFormat nf = new DecimalFormat("0.000");

    public OptionSlider(int max) {
        super(0, max, 0);
        setValue((int) (getInputValue() * getScale()));
        setBorder(new TitledBorder(getFullTitle()));
        setMajorTickSpacing(1);
        addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setOutputValue(getDoubleValue());
                ((TitledBorder) getBorder()).setTitle(getFullTitle());
            }
        });
    }

    protected double getScale() {
        return getMaximum();
    }

    private String getFullTitle() {
        return getTitle() + ": " + nf.format(getDoubleValue());
    }

    private double getDoubleValue() {
        return getValue() / getScale();
    }

    public abstract String getTitle();

    public abstract double getInputValue();

    public abstract void setOutputValue(double val);
}
