/*
 * Created on 2005-09-05
 */
package gui.common;

import facade.Facade;
import gui.FrameField;
import gui.options.FrameOptions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Dominik
 */
public class Menu extends JMenuBar {
    private static final long serialVersionUID = 1L;

    /**
     * @param main
     * @return
     */
    public Menu() {
        setOpaque(true);
        JMenu file = buildFileMenu();
        JMenu edit = buildEditMenu();
        JMenu views = buildViewsMenu();
        JMenu help = buildHelpMenu();

        add(file);
        add(edit);
        add(views);
        add(help);
    }


    protected JMenu buildFileMenu() {
        JMenu file = new JMenu("File");
        JMenuItem newEvol = new JMenuItem("Start new evolution");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem quit = new JMenuItem("Quit");

        newEvol.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Facade.getInstance().startNewEvolution();
            }
        });
        /*		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {frameMain.openDocument();}
		});*/
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Facade.getInstance().close();
            }
        });

        file.add(newEvol);
        file.add(open);
        file.addSeparator();
        file.add(quit);
        return file;
    }

    protected JMenu buildEditMenu() {
        JMenu edit = new JMenu("Edit");
        JMenuItem prefs = new JMenuItem("Options");

        prefs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CommonDesktopPane r = Facade.getInstance().desktopPane;
                FrameOptions dialog = new FrameOptions();
                r.add(dialog, CommonDesktopPane.PREFS_LAYER);
                dialog.show();
            }
        });

        edit.add(prefs);
        return edit;
    }

    protected JMenu buildViewsMenu() {
        JMenu views = new JMenu("Views");
        JMenuItem playerList = new JMenuItem("Player list");
        JMenuItem sectorMap = new JMenuItem("Sector map");
        JMenuItem freeFly = new JMenuItem("Free-fly view");
        JMenuItem console = new JMenuItem("System console");
        sectorMap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Facade.getInstance().desktopPane.openFrame(new FrameField());
            }
        });
        console.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Facade.getInstance().desktopPane.openFrame(new FrameConsole());
            }
        });
        views.add(playerList);
        views.add(sectorMap);
        views.add(freeFly);
        views.addSeparator();
        views.add(console);
        return views;
    }

    protected JMenu buildHelpMenu() {
        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About Metalworks...");
        JMenuItem openHelp = new JMenuItem("Open Help Window");

        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Facade.getInstance().desktopPane.showAboutBox();
            }
        });

        openHelp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Facade.getInstance().desktopPane.openHelpWindow();
            }
        });

        help.add(about);
        help.add(openHelp);

        return help;
    }

}
