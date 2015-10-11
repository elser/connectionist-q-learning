package gui.common;

import gui.metalworks.MetalworksHelp;

import javax.swing.*;

/**
 * To be used in Applet as well as in Frame(Window).
 *
 * @author Dominik
 */
public class CommonDesktopPane extends JDesktopPane {
    private static final long serialVersionUID = 1L;
    protected static final int PREFS_LAYER = 4;
    protected static final int DOC_LAYER = 5;
    protected static final int TOOL_LAYER = 6;
    protected static final int HELP_LAYER = 7;
    protected static final String ABOUT_MSG =
            "Brainer \n \nAn application written to enable observing evolving population.";

    public CommonDesktopPane() {
    }

    public void openFrame(JInternalFrame doc) {
        this.add(doc, DOC_LAYER);
        doc.setVisible(true);
        doc.setFocusable(true);
    }

    public void openHelpWindow() {
        JInternalFrame help = new MetalworksHelp();
        this.add(help, HELP_LAYER);
        try {
            help.setVisible(true);
            help.setSelected(true);
        } catch (java.beans.PropertyVetoException e2) {
        }
    }

    public void showAboutBox() {
        JOptionPane.showMessageDialog(this, ABOUT_MSG);
    }
}
