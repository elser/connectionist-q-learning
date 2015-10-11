package main;

import facade.Facade;
import game.CuriGameManager;
import gui.CuriWindow;
import gui.common.Menu;

import javax.swing.*;

public class WWWApplet extends JApplet {
    private static final long serialVersionUID = 1L;
    static Facade facade;

    public void init() {
        facade = new Facade(this, new CuriGameManager());
        facade.setUrl("http://www.elsy.gdan.pl/pliki/brainer/q/");
        facade.initOptions();
        facade.initGUI();
        setJMenuBar(new Menu());
        switch (1) {
            case 0:
                facade.loadScenario();
                break;
            case 1:
                facade.startNewEvolution();
                break;
        }
        facade.attachGUIToScenario(new CuriWindow());
        System.out.println("Application started");
    }

}
