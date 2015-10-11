package pl.gdan.elsy.tool;

import java.text.DecimalFormat;

public class Benchmark {
    private static final int ALL = 0;
    private static final int NN_COUNT = 1;
    private static final int NN_COUNT_0 = 2;
    private static final int SPOTS = 3;
    private static String name[] = new String[]{"ALL", "NN.Count 7", "NN.3"};
    private static final int MAX[] = new int[]{1, 1, 1};
    /*
     * Don't change below this line
     */
    private static long startTime[] = new long[SPOTS];
    private static long totalTime[] = new long[SPOTS];
    private static int iter[] = new int[SPOTS];
    private static long invocations[] = new long[SPOTS];
    private static boolean finished = false;

    static {
        for (int i = 0; i < SPOTS; i++) {
            totalTime[i] = 0;
            iter[i] = 0;
            invocations[i] = 0;
        }
    }

    private static void startTimer(int spot) {
        startTime[spot] = System.currentTimeMillis();
    }

    private static void stopTimer(int spot) {
        totalTime[spot] += System.currentTimeMillis() - startTime[spot];
        invocations[spot]++;
    }

    public static void finish() {
        //while(iter[ALL]>0) {}
        if (invocations[ALL] == 0) {
            return;
        }
        finished = true;
        System.out.println("Benchmarking results:");
        DecimalFormat df = new DecimalFormat("0.000");
        double ALL_TIME_SCALED = 0;
        for (int i = 1; i < SPOTS; i++) {
            ALL_TIME_SCALED += ((double) totalTime[i]) / (invocations[i] * MAX[i]);
        }
        //double allTime = ((double)totalTime[ALL])/(invocations[ALL]*MAX[ALL]);
        for (int i = 0; i < SPOTS; i++) {
            System.out.println(
                    "   " +
                            StrOper.bake(name[i], 15, false) + ": " +
                            StrOper.bake(df.format(((double) totalTime[i]) / (invocations[i] * MAX[i])), 8, false) + " ms " +
                            StrOper.bake(df.format(((double) totalTime[i] * 100) / (MAX[i] * ALL_TIME_SCALED)), 8, false) + "% " +
                            "  invocations: " + invocations[i] / invocations[ALL] + "  " + totalTime[i]
            );
        }
    }

    private static boolean loop(int spot) {
        //System.out.print(""+spot+" : ");
        if (finished) {
            return false;
        } else if (iter[spot] == 0) {
            startTimer(spot);
            iter[spot]++;
            return (iter[spot] <= MAX[spot]);
        } else if (iter[spot] < MAX[spot]) {
            iter[spot]++;
            return true;
        } else {
            stopTimer(spot);
            iter[spot] = 0;
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            while (Benchmark.loop(ALL)) {
                for (int j = 0; j < 3; j++) {
                    while (Benchmark.loop(NN_COUNT_0)) {
                        Thread.sleep(10);
                    }
                }
                for (int j = 0; j < 7; j++) {
                    while (Benchmark.loop(NN_COUNT)) {
                        Thread.sleep(10);
                    }
                }
            }
        }
        finish();
    }
}
