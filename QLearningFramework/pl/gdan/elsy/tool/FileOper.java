package pl.gdan.elsy.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * @author elser
 *         <p>
 *         To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class FileOper {

    public static void prepareDir(String path) {
        File files = new File(path);
        prepareDir(files);
    }

    /**
     * Method prepareDir.
     *
     * @param actFiles
     */
    public static void prepareDir(File files) {
        if (!files.isDirectory()) {
            files.mkdir();
        } else {
            File[] toDelete = files.listFiles();
            for (int i = 0; i < toDelete.length; i++) {
                System.out.println("Deleting: " + toDelete[i].getPath());
                toDelete[i].delete();
            }
        }

    }

    /**
     * Method getStrFromFile.
     *
     * @param fileName
     * @return String
     */
    public static String getStrFromFile(String fileName) {
        StringBuffer buf = new StringBuffer();
        int i;

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            while ((i = br.read()) != -1) {
                buf.append((char) i);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return buf.toString();
    }

    public static int saveStrToFile(String fileName, String str) {
        try {
            PrintWriter out =
                    new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
            out.print(str);
            out.close();

        } catch (Throwable e) {
            System.out.println("FileOper>" + e);
        }
        return 1;
    }


    public static String getStrFromResource(Object object, String path) {
        StringBuffer buf = new StringBuffer();
        int i;

        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(
                        object.getClass().getResourceAsStream(path)));
        try {
            while ((i = in.read()) != -1) {
                buf.append((char) i);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return buf.toString();
    }
}
