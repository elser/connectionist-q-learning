package gui.options.learning;

import gui.metalworks.ColumnLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class LearningPanel extends JPanel {
    private static final long serialVersionUID = 4770635013098824793L;
    public static final int MAX = 200;

    public LearningPanel() {
        this.setLayout(new GridLayout(1, 0));

        JPanel panel = new JPanel();

        panel.setLayout(new ColumnLayout());
        panel.setBorder(new TitledBorder("Learning params"));
        panel.add(new RMinSlider());
        panel.add(new RMaxSlider());
        panel.add(new RSlopeSlider());
        panel.add(new PMinSlider());
        panel.add(new PMaxSlider());
        panel.add(new PSlopeSlider());
        this.add(panel);
    }
}
