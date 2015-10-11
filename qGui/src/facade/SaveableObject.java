///* Created on 1990-12-22 */
package facade;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Repository of teams. This is equal to the whole population of species in the process of evolution.
 * One species here is an instance of Team class.
 *
 * @author Elser
 */
public class SaveableObject implements Serializable {
    private static final long serialVersionUID = 1L;

    public void save() {
        System.out.print("Saving... ");
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(zipName())));
            out.writeObject(this);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("done.");
    }

    private static String zipName() {
        return "teamRep.zip";
    }

    public static SaveableObject load() {
        SaveableObject ret = null;
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(zipName())));
            ret = (SaveableObject) in.readObject();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("file open error.");
        }
        return ret;
    }
}
