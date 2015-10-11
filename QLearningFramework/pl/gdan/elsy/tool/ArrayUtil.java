/*
 * Created on 2006-04-03
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pl.gdan.elsy.tool;


/**
 * @author Dominik
 *         <p>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ArrayUtil {

    /**
     * @param parentsLocal
     * @param randomTeam
     * @return
     */
    public static boolean contains(Object[] parentsLocal, Object randomTeam) {
        for (int i = 0; i < parentsLocal.length; i++) {
            if (parentsLocal[i].equals(randomTeam)) {
                return true;
            }
        }
        return false;
    }

    public static void copy(double[] to, double[] from) {
        if (to.length != from.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        for (int i = 0; i < to.length; i++) {
            to[i] = from[i];
        }
    }

    public static void copy(int[] to, int[] from) {
        for (int i = 0; i < to.length; i++) {
            to[i] = from[i];
        }
    }

}
