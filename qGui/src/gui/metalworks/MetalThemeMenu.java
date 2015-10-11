package gui.metalworks;

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class describes a theme using "green" colors.
 *
 * @author Elser
 * @version 1.6 02/06/02
 */
public class MetalThemeMenu extends JMenu implements ActionListener {

    private static final long serialVersionUID = 1L;
    MetalTheme[] themes;

    public MetalThemeMenu(String name, MetalTheme[] themeArray) {
        super(name);
        themes = themeArray;
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < themes.length; i++) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(themes[i].getName());
            group.add(item);
            add(item);
            item.setActionCommand(i + "");
            item.addActionListener(this);
            if (i == 0) {
                item.setSelected(true);
            }
        }

    }

    public void actionPerformed(ActionEvent e) {
        String numStr = e.getActionCommand();
        MetalTheme selectedTheme = themes[Integer.parseInt(numStr)];
        MetalLookAndFeel.setCurrentTheme(selectedTheme);
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception ex) {
            System.out.println("Failed loading Metal");
            System.out.println(ex);
        }

    }

}
