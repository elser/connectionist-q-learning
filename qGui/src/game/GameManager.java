package game;

import config.Options;
import facade.Facade;
import game.rules.BallAndGoalRule;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public abstract class GameManager extends Thread implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final boolean ALLOW_MUTATION = true;
    private int time = 0;
    private static long lastSaveTime;
    private static final long TEMP_SAVE_INTERVAL = 120 * 60 * 1000; //120 minutes
    private static GameManager instance;
    private Match match;
    public static boolean running;
    public Team teams[];
    private static final String ZIP_FILENAME = "newPop.zip";

    public GameManager() {
        instance = this;
        teams = new Team[getTeamsNo()];
        for (int i = 0; i < teams.length; i++) {
            teams[i] = newTeam();
        }
        time = 0;
        match = new Match(new BallAndGoalRule(), teams);
        match.startNew(teams);
    }

    public abstract Team newTeam();

    public abstract int getTeamsNo();

    public static void load() throws Exception {
        loadAll(ZIP_FILENAME);
        instance.start();
    }

    public void run() {
        lastSaveTime = System.currentTimeMillis();
        while (running) {
            try {
                sleep(Options.getInstance().getSpeedWait());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; running && i < config.Options.getInstance().getSpeedIter(); i++) {
                time++;
                match.live();
            }
            // perform periodical autosave
            if (Facade.getInstance().getUrl().length() == 0 && lastSaveTime + TEMP_SAVE_INTERVAL < System.currentTimeMillis()) {
                lastSaveTime = System.currentTimeMillis();
                saveAll("tempPop.zip");
            }
        }
    }

    /**
     * Start the evolution
     */
    public void start() {
        running = true;
        super.interrupt();
        super.start();
    }


    public static void loadAll(String name) throws Exception {
        ObjectInputStream in;
        if (Facade.getInstance().getUrl().length() > 0) {
            URL u = new URL(Facade.getInstance().getUrl() + "newPop.zip");
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            in = new ObjectInputStream(new GZIPInputStream(connection.getInputStream()));
        } else {
            if (name.indexOf(".zip") > 0) {
                in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(name)));
            } else {
                in = new ObjectInputStream(new FileInputStream(name));
            }
        }
        instance = (GameManager) in.readObject();
        in.close();
    }

    public void saveAll(String name) {
        if (name.length() == 0) {
            return;
        }
        System.out.print("Saving... ");
        ObjectOutputStream out;
        try {
            if (name.indexOf(".zip") > 0) {
                out = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(name)));
            } else {
                out = new ObjectOutputStream(new FileOutputStream(name));
            }
            out.writeObject(instance);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("done.");
    }

    public void halt() {
        running = false;
    }

    public static GameManager getInstance() {
        if (instance == null) {
            System.out.println("Scenario instance not initialized -> using default");
            instance.start();
        }
        return instance;
    }

    public void saveAll() {
        saveAll(ZIP_FILENAME);
    }

    public int getTime() {
        return time;
    }

    /**
     * @return the match
     */
    public Match getMatch() {
        return match;
    }

}
