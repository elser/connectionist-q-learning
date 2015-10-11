package gui.options;

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import config.Options;
import gui.FieldGraphic;
import gui.common.WindowFrame;
import gui.metalworks.ColumnLayout;
import gui.metalworks.UISwitchListener;
import gui.options.learning.LearningPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This is dialog which allows users to choose preferences
 *
 * @author Elser
 * @version 1.6 02/06/02
 */
public class FrameOptions extends WindowFrame {
    private static final long serialVersionUID = 1L;

    JSlider speedSlider;

    public FrameOptions() {
        super("Options", false);
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        JPanel simulationPanel = buildSimulationPanel();
        JPanel visualizationPanel = buildGraphicsPanel();
        tabs.addTab("Simulation", null, simulationPanel);
        tabs.addTab("Visualization", null, visualizationPanel);
        tabs.addTab("Learning", null, new LearningPanel());

        container.add(tabs, BorderLayout.CENTER);
        //container.add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(container);
        pack();
        UIManager.addPropertyChangeListener(new UISwitchListener(container));
    }

    private JPanel buildSimulationPanel() {
        JPanel simulationPanel = new JPanel();
        simulationPanel.setLayout(new GridLayout(1, 0));

        JPanel speedPanel = new JPanel();

        speedPanel.setLayout(new ColumnLayout());
        speedPanel.setBorder(new TitledBorder("Simulation"));
        speedSlider = new JSlider(0, 100, Options.getInstance().getSpeed());
        speedSlider.setBorder(new TitledBorder("Simulation speed: " + speedSlider.getValue()));
        speedSlider.setMajorTickSpacing(1);
        speedSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Options.getInstance().setSpeed(speedSlider.getValue());
                ((TitledBorder) speedSlider.getBorder())
                        .setTitle("Simulation speed: " + speedSlider.getValue());
            }
        });
        speedPanel.add(speedSlider);
        simulationPanel.add(speedPanel);

        return simulationPanel;
    }

    private JPanel buildGraphicsPanel() {
        JPanel graphicsPanel = new JPanel();
        graphicsPanel.setLayout(new GridLayout(1, 0));

        JPanel fieldDrawingPanel = new JPanel();
        fieldDrawingPanel.setLayout(new ColumnLayout());
        fieldDrawingPanel.setBorder(new TitledBorder("Field options"));

        JPanel chartsPanel = new JPanel();
        chartsPanel.setLayout(new ColumnLayout());
        chartsPanel.setBorder(new TitledBorder("Charts"));
        JLabel chartLabel = new JLabel("Chart type");
        JComboBox chartType = new JComboBox();
        chartType.addItem("Teams histogram");
        chartsPanel.add(chartLabel);
        chartsPanel.add(chartType);
        JButton generate = new JButton("Generate chart");
        generate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateChartPressed();
            }
        });
        chartsPanel.add(new JSeparator());
        chartsPanel.add(generate);

        JPanel attachmentPanel = new JPanel();
        JLabel attachmentLabel = new JLabel("Attachments");
        JComboBox attach = new JComboBox();
        attach.addItem("Download Always");
        attach.addItem("Ask size > 1 Meg");
        attach.addItem("Ask size > 5 Meg");
        attach.addItem("Ask Always");
        attachmentPanel.add(attachmentLabel);
        attachmentPanel.add(attach);

        final JCheckBox showBorderDist = new JCheckBox("Show distances to borders");
        showBorderDist.setSelected(FieldGraphic.isDrawBorderDist());
        showBorderDist.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                FieldGraphic.setDrawBorderDist(showBorderDist.isSelected());
            }

        });
        final JCheckBox showNearestFellow = new JCheckBox("Show nearest fellow");
        showNearestFellow.setSelected(FieldGraphic.isDrawNearestFellow());
        showNearestFellow.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                FieldGraphic.setDrawNearestFellow(showNearestFellow.isSelected());
            }

        });
        final JCheckBox showNearestOpponent = new JCheckBox("Show nearest opponent");
        showNearestOpponent.setSelected(FieldGraphic.isDrawNearestOpponent());
        showNearestOpponent.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                FieldGraphic.setDrawNearestOpponent(showNearestOpponent.isSelected());
            }

        });
        final JCheckBox showPlayerInfo = new JCheckBox("Show player info");
        showPlayerInfo.setSelected(FieldGraphic.isDrawPlayerVariables());
        showPlayerInfo.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                FieldGraphic.setDrawPlayerVariables(showPlayerInfo.isSelected());
            }

        });
        final JCheckBox showVectorToBall = new JCheckBox("Show vector to ball");
        showVectorToBall.setSelected(FieldGraphic.isDrawVectorToBall());
        showVectorToBall.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                FieldGraphic.setDrawVectorToBall(showVectorToBall.isSelected());
            }

        });
        final JCheckBox showTraces = new JCheckBox("Show traces");
        showTraces.setSelected(FieldGraphic.isDrawTraces());
        showTraces.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                FieldGraphic.setDrawTraces(showTraces.isSelected());
            }

        });
        //connectPanel.add(protoPanel);
        //connectPanel.add(attachmentPanel);
        fieldDrawingPanel.add(showBorderDist);
        //TODO: what does it exactly show...
        //fieldDrawingPanel.add(showVectorToBall);
        fieldDrawingPanel.add(showNearestFellow);
        fieldDrawingPanel.add(showNearestOpponent);
        fieldDrawingPanel.add(showPlayerInfo);
        fieldDrawingPanel.add(showTraces);
        graphicsPanel.add(fieldDrawingPanel);
        graphicsPanel.add(chartsPanel);
        return graphicsPanel;
    }

    /*

     protected void centerDialog() {
     Dimension screenSize = this.getToolkit().getScreenSize();
     Dimension size = this.getSize();
     int y = screenSize.height - size.height - 110;
     int x = screenSize.width - size.width - 120;
     this.setLocation(x,y);
     }
     *
     *
     */
    private JInternalFrame oldChart = null;

    private void generateChartPressed() {
        if (oldChart != null) {
            try {
                oldChart.dispose();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        //Facade.getInstance().desktopPane.openFrame(oldChart = new FrameTeamsHistogram());
    }

}
