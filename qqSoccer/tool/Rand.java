/*
 * Created on 1990-12-21
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package tool;

import java.util.Random;

/**
 * @author Elser
 *         <p>
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Rand {
    private static final int arrD1Size = 997;
    private static final int arrPercentSize = 97397;
    private static double arrD1[];
    private static double arrPercent[];
    static Random random = null;
    private static int iD1 = 0;

    static {
        random = new Random();
        arrD1 = new double[arrD1Size];
        arrPercent = new double[arrPercentSize];
        setArrD1();
        setArrPercent();
    }

    /**
     * Returns random double from range (from,to)
     *
     * @param from to
     * @return
     */
    public static double d(double from, double to) {
        if (to < from) {
            return 0;
        }
        return (to - from) * (random.nextDouble()) + from;
    }

    private static void setArrD1() {
        for (int i = 0; i < arrD1.length; i++) {
            arrD1[i] = Rand.d(-1, 1);
        }
    }

    private static void setArrPercent() {
        for (int i = 0; i < arrPercent.length; i++) {
            arrPercent[i] = Rand.d(100);
            //if(arrPercent[i]<1)System.out.println(arrPercent[i]);
        }
    }

    /**
     * Returns random integer
     *
     * @param range
     * @return
     */
    public static int i(int range) {
        return random.nextInt(range);
    }

    public static int i(int from, int to) {
        if (to < from) {
            return 0;
        }
        return (Math.abs(random.nextInt()) % (to - from)) + from;
    }

    /**
     * Returns random boolean
     *
     * @return
     */
    public static boolean b() {
        return random.nextBoolean();
    }

    public static void main(String[] args) {
        System.out.println(d(5));/*
        int ok=0;
		for (double j = 0.001; j < 1; j*=2) {
			ok=0;
			int loop = 100000;
			for (int i = 0; i < loop; i++) {
				if(successWithPercent(j)) {
					ok++;
				}
			}
			System.out.println(j+" : "+(double)ok*100/loop);
		}*/
    }

    /**
     * @param sum
     * @return
     */
    public static double d(double sum) {
        return random.nextDouble() * sum;
    }

    private static int kD1 = 0;
    private static int iPerc = 0;
    private static int kPerc = 0;

    public static double fastD1() {
        iD1++;
        if (iD1 >= arrD1Size) {
            kD1++;
            iD1 = 0;
            if (kD1 > 2000) {
                setArrD1();
                kD1 = 0;
            }
        }
        return arrD1[iD1];
    }

    public static double gauss() {
        double nextGaussian = random.nextGaussian();
        return nextGaussian;
    }

    public static int i() {
        return random.nextInt();
    }

    public static boolean successWithPercent(double percent) {
        iPerc++;
        if (iPerc >= arrPercent.length) {
            kPerc++;
            iPerc = 0;
            if (kPerc > 20) {
                setArrPercent();
                kPerc = 0;
            }
        }
        return arrPercent[iPerc] < percent;
        //return Rand.d(100) < percent;
    }

    public static double gauss(double scale) {
        return random.nextGaussian() * scale;
    }

    public static double gaussAbs(double scale, double offset) {
        return Math.abs(gauss(scale) + offset);
    }
}
