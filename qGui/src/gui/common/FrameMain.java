package gui.common;

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import facade.Facade;
import gui.metalworks.UISwitchListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * This is the main frame for application presentation
 *
 * @author Elser
 */
public class FrameMain extends JFrame {
    private static final long serialVersionUID = 1L;
    JMenuBar menuBar;
    JInternalFrame toolPalette;
    JCheckBoxMenuItem showToolPaletteMenuItem;

    public FrameMain() {
        super("Soccer");
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setJMenuBar(new Menu());
        UIManager.addPropertyChangeListener(new UISwitchListener(getRootPane()));
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Facade.getInstance().close();
            }
        });
        setSize(900, 900);
        setVisible(true);
    }

    public void openDocument() {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(this);
    }
}
