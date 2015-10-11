/*
 * Created on 1990-12-21
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package tool;

import cosmos.FieldDimensions;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * @author Elser
 *         <p>
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Mat {
    public static final DecimalFormat format = new DecimalFormat("0.000");
    private static final double sqrtPrecomputedLimit = 20000.0;
    private static double sqrtPrecomputedTo100[];
    private static double sqrtPrecomputedMore100[];
    private static double sinPrecomputed[];

    static {
        sqrtPrecomputedTo100 = new double[1000];
        for (int i = 0; i < sqrtPrecomputedTo100.length; i++) {
            sqrtPrecomputedTo100[i] = Math.sqrt(0.1 * i);
        }
        sqrtPrecomputedMore100 = new double[2000];
        for (int i = 0; i < sqrtPrecomputedMore100.length; i++) {
            sqrtPrecomputedMore100[i] = Math.sqrt(i * 10 + sqrtPrecomputedTo100.length / 10 - 95);
        }
        sinPrecomputed = new double[360];
        for (int i = 0; i < sinPrecomputed.length; i++) {
            sinPrecomputed[i] = Math.sin(Math.toRadians(i));
        }
    }

    public static double sigmoidBi(double d) {
        return 2.0 / (1.0 + Math.exp(-d)) - 1.0;
    }

    public static double sigmoidUni(double d) {
        return 1.0 / (1.0 + Math.exp(-d));
    }

    public static double min(double x1, double x2, double x3, double x4) {
        double minRet = x1;
        if (x2 < minRet) {
            minRet = x2;
        }
        if (x3 < minRet) {
            minRet = x3;
        }
        if (x4 > minRet) {
            return x4;
        }
        return minRet;
    }

    public static int min(int x1, int x2, int x3, int x4) {
        int minRet = x1;
        if (x2 < minRet) {
            minRet = x2;
        }
        if (x3 < minRet) {
            minRet = x3;
        }
        if (x4 > minRet) {
            return x4;
        }
        return minRet;
    }

    public static int max(int x1, int x2, int x3, int x4) {
        int maxRet = x1;
        if (x2 > maxRet) {
            maxRet = x2;
        }
        if (x3 > maxRet) {
            maxRet = x3;
        }
        if (x4 < maxRet) {
            return x4;
        }
        return maxRet;
    }

    static public double lim(double x, double d) {
        return lim(x, -d, d);
    }

    static public double lim(double x, double d, double u) {
        if (x < d) {
            return d;
        }
        if (x > u) {
            return u;
        }
        return x;
    }

    static public int lim(int x, int d, int u) {
        if (x < d) {
            return d;
        }
        if (x > u) {
            return u;
        }
        return x;
    }

    static public double sqr(double d) {
        return d * d;
    }

    static public int sqr(int i) {
        return i * i;
    }

    public static int sgn(int x) {
        if (x < 0) {
            return -1;
        }
        if (x > 0) {
            return 1;
        }
        return 0;
    }

    public static double sgn(double x) {
        if (x < 0) {
            return -1;
        }
        if (x > 0) {
            return 1;
        }
        return 0;
    }

    /**
     * @param x
     * @param d
     * @param hbound
     * @return
     */
    public static boolean inside(double x, double dMin, double dMax) {
        return x > dMin && x < dMax;
    }

    public static boolean inside(double x, double dMax) {
        return x > -dMax && x < dMax;
    }

    public static boolean insideInclusive(double x, double dMax) {
        return x >= -dMax && x <= dMax;
    }

    public static double normalSqrt(double x) {
        return Math.sqrt(x);
    }

    public static double fastSqrt(double x) {
        //return Math.sqrt(x);
        //*
        if (x < 100) {
            return sqrtPrecomputedTo100[(int) (x * 10)];
        } else if (x < sqrtPrecomputedLimit) {
            return sqrtPrecomputedMore100[(int) (x / 10)];
        } else {
            return Math.sqrt(x);
        }/**/
    }

    public static double sin(int i) {
        return sinPrecomputed[i % 360];
    }

    public static double cos(int i) {
        return sinPrecomputed[(i + 90) % 360];
    }

    public static String bitsToString(int bits) {
        StringBuffer sb = new StringBuffer();
        boolean printStarted = true;
        for (int i = 0; i < 32; i++) {
            boolean bitOn = (bits & 0x80000000) != 0;
            printStarted |= bitOn;
            if (printStarted) {
                sb.append(
                        bitOn ? '1' : '0'
                );
            }
            bits = bits << 1;
        }
        return sb.toString();
    }

    public static double minDist(double a, double b, double c, double d) {
        double min = FieldDimensions.MAX_DISTANCE;
        if (a > 0 && a < min) {
            min = a;
        }
        if (b > 0 && b < min) {
            min = b;
        }
        if (c > 0 && c < min) {
            min = c;
        }
        if (d > 0 && d < min) {
            min = d;
        }
        return min;
    }

    /*
     * Refactored 18.03.2006 - current implementation seems to be most efficient one
     * Tested with JVM from 1.6 JDK with -server option
     */
    public static int countBits(int bits) {
        return (bits & 1) +
                ((bits >> 1) & 1) +
                ((bits >> 2) & 1) +
                ((bits >> 3) & 1) +
                ((bits >> 4) & 1) +
                ((bits >> 5) & 1) +
                ((bits >> 6) & 1) +
                ((bits >> 7) & 1) +
                ((bits >> 8) & 1) +
                ((bits >> 9) & 1) +
                ((bits >> 10) & 1) +
                ((bits >> 12) & 1) +
                ((bits >> 13) & 1) +
                ((bits >> 14) & 1) +
                ((bits >> 15) & 1) +
                ((bits >> 16) & 1) +
                ((bits >> 17) & 1) +
                ((bits >> 18) & 1) +
                ((bits >> 19) & 1) +
                ((bits >> 20) & 1) +
                ((bits >> 21) & 1) +
                ((bits >> 22) & 1) +
                ((bits >> 23) & 1) +
                ((bits >> 24) & 1) +
                ((bits >> 25) & 1) +
                ((bits >> 26) & 1) +
                ((bits >> 27) & 1) +
                ((bits >> 28) & 1) +
                ((bits >> 29) & 1) +
                ((bits >> 30) & 1) +
                ((bits >> 31) & 1);
    }

    public static DecimalFormat df = new DecimalFormat("####0.000", new DecimalFormatSymbols(Locale.US));
    public static DecimalFormat df2 = new DecimalFormat("####0.00000", new DecimalFormatSymbols(Locale.US));
}
