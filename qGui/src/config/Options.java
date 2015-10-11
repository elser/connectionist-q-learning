/* Created on 1991-02-16 */
package config;

import facade.Facade;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * @author unknown
 */
public class Options implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Options instance;
    private static String OPTIONS_FILE_NAME = "options.dat";

    private int speed = 1;
    private int speedWait = 1;
    private int speedIter = 1;
    private long fieldDispRate = 20;/**/
    private long listDispRate = 1000;

    //private final double mutationOnRebirthRate = 0.2;
    //private final double mutationOnBirthRate = 0.2;

    /**
     * speed is in range 1-100
     */
    public void setSpeed(int value) {
        speed = value;
        if (speed < 40) {
            speedIter = speed == 0 ? 0 : speed / 4 + 1;
            speedWait = (44 - speed) * 3;
            fieldDispRate = 20;
            listDispRate = 20;
        } else if (speed < 100) {
            speedIter = speed * 2 - 70;
            fieldDispRate = (speed - 40) * speed / 50;
            speedWait = 1;
            listDispRate = 1000;
        } else {
            speedIter = 10000;
            fieldDispRate = 2000;
            speedWait = 1;
            listDispRate = 4000;
        }
    }

    public static Options getInstance() {
        if (instance == null) {
            instance = new Options();
        }
        return instance;
    }

    public static void load() {
        try {
            ObjectInputStream in = null;
            if (Facade.getInstance().getUrl().length() > 0) {
                URL u = new URL(Facade.getInstance().getUrl() + OPTIONS_FILE_NAME);
                HttpURLConnection connection = (HttpURLConnection) u.openConnection();
                in = new ObjectInputStream(connection.getInputStream());
            } else {
                in = new ObjectInputStream(new FileInputStream(OPTIONS_FILE_NAME));
            }
            instance = (Options) in.readObject();
            in.close();
        } catch (Exception e) {
            System.out.println(Facade.getInstance().getUrl() + OPTIONS_FILE_NAME + " file open error, starting with default options.");
        }
    }

    public static void save() {
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(new FileOutputStream(OPTIONS_FILE_NAME));
            out.writeObject(instance);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * speed is in range 1-100
     */
    public int getSpeed() {
        return speed;
    }

    public long getFieldDispRate() {
        return fieldDispRate;
    }

    public long getListDispRate() {
        return listDispRate;
    }

    public int getSpeedIter() {
        return speedIter;
    }

    public int getSpeedWait() {
        return speedWait;
    }
}
