package main;

import facade.Facade;
import game.CuriGameManager;
import gui.CuriWindow;
import gui.common.FrameMain;


public class Soccer {
    public static final boolean ALLOW_EVOLUTION = true;
    static Facade facade;

    /*
     * For best performance run on JVM from JDK 1.6 with -server option
     */
    public static void main(String[] args) {
        facade = new Facade(new FrameMain(), new CuriGameManager());
        int opt = 1;
        if (args != null && args.length > 0) {
            opt = Integer.parseInt(args[0]);
        }
        switch (opt) {
            case 0:
                facade.loadScenario();
                break;
            case 1:
                facade.startNewEvolution();
                break;
        }
        facade.initOptions();
        facade.initGUI();
        final CuriWindow curiWindow = new CuriWindow();
        facade.attachGUIToScenario(curiWindow);
    }
}
