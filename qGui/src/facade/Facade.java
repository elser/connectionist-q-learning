/*
 * Created on 2005-09-05
 */
package facade;

import config.Options;
import game.GameManager;
import game.Match;
import game.Player;
import gui.common.AbstractThreadedFrame;
import gui.common.CommonDesktopPane;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dominik
 */
public class Facade {
    private static Facade instance;
    public CommonDesktopPane desktopPane = null;
    private GameManager gameManager = null;
    private Container contentPane;
    private String url = "";

    private Facade(Container container, GameManager gameManager) {
        instance = this;
        this.contentPane = container;
        this.gameManager = gameManager;
    }

    public Facade(JApplet applet, GameManager gameManager) {
        this(applet.getContentPane(), gameManager);
    }

    public Facade(JFrame frame, GameManager gameManager) {
        this(frame.getContentPane(), gameManager);
    }

    public void startNewEvolution() {
        GameManager.getInstance();
    }

    public synchronized void attachGUIToScenario(CommonDesktopPane desktopPane) {
        this.desktopPane = desktopPane;
        contentPane.add(desktopPane);
        AbstractThreadedFrame.running = true;
    }

    public static Facade getInstance() {
        return instance;
    }

    public void initGUI() {
        try {
            javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new javax.swing.plaf.metal.DefaultMetalTheme());
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("Metal Look & Feel not supported on this platform. \nProgram Terminated");
            System.exit(0);
        } catch (IllegalAccessException e) {
            System.out.println("Metal Look & Feel could not be accessed. \nProgram Terminated");
            System.exit(0);
        } catch (ClassNotFoundException e) {
            System.out.println("Metal Look & Feel could not found. \nProgram Terminated");
            System.exit(0);
        } catch (InstantiationException e) {
            System.out.println("Metal Look & Feel could not be instantiated. \nProgram Terminated");
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Unexpected error. \nProgram Terminated");
            e.printStackTrace();
            System.exit(0);
        }
    }

    private static int selectedPlayerId = 0;

    public static Player getSelectedPlayer() {
        if (selectedPlayerId == -1 || Facade.match() == null || Facade.match().players == null) {
            return null;
        }
        try {
            return Facade.match().players[selectedPlayerId];
        } catch (Exception e) {
            return null;
        }
    }

    public static void setSelectedPlayerId(int i) {
        selectedPlayerId = i;
    }

    public static int getSelectedPlayerId() {
        return selectedPlayerId;
    }

    public static Match match() {
        return getInstance().getMatch();
    }

    public Match getMatch() {
        return gameManager.getMatch();
    }

    public void close() {
        if (url.length() == 0) {
            GameManager.running = false;
            AbstractThreadedFrame.running = false;
            Options.save();
            GameManager.getInstance().saveAll();
        }
        System.exit(0);
    }

    public void loadScenario() {
        try {
            GameManager.load();
            gameManager = GameManager.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            startNewEvolution();
        }
    }

    public void initOptions() {
        Options.load();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
