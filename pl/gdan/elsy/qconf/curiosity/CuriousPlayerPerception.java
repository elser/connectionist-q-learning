package pl.gdan.elsy.qconf.curiosity;

import pl.gdan.elsy.qconf.Perception;

public abstract class CuriousPlayerPerception extends Perception {
    private static double rMin = 0.003;
    private static double rMax = 0.325;
    private static double rSlope = 420;
    private static double pMin = 0.014;
    private static double pMax = 0.370;
    private static double pSlope = 560;
    private Curiosity curiosity;
    private double novelty;
    private Perception foreseePerc = this;

    public double getReward() {
        this.novelty = curiosity.getAvgError();
        double wundtCurve = wundtCurve(this.novelty);
        //System.out.println(wundtCurve);
        return wundtCurve;
    }

    public double wundtCurve(double n) {
        double R = func(n, rMin, rMax, rSlope);
        double P = func(n, pMin, pMax, pSlope);
        return R - P - 0.1;
    }

    private double func(double n, double min, double max, double slope) {
        return max / (1 + Math.exp(-slope * (n - min)));
    }

    public void start() {
        super.start();
        if (foreseePerc != this) {
            foreseePerc.start();
        }
    }

    public void perceive() {
        super.perceive();
        if (getOutput() != null && foreseePerc != this) {
            foreseePerc.perceive();
        }
    }

    public Curiosity getCuriosity() {
        return curiosity;
    }

    public void setCuriosity(Curiosity curiosity) {
        this.curiosity = curiosity;
    }

    public double getNovelty() {
        return novelty;
    }

    public double[] getForeseeOutput() {
        return foreseePerc.getOutput();
    }

    public Perception getForeseePerc() {
        return foreseePerc;
    }

    public void setForeseePerc(Perception foreseePerc) {
        this.foreseePerc = foreseePerc;
        this.foreseePerc.setInputPerception(this);
    }

    public static double getPMax() {
        return pMax;
    }

    public static void setPMax(double max) {
        pMax = max;
    }

    public static double getPMin() {
        return pMin;
    }

    public static void setPMin(double min) {
        pMin = min;
    }

    public static double getRMax() {
        return rMax;
    }

    public static void setRMax(double max) {
        rMax = max;
    }

    public static double getRMin() {
        return rMin;
    }

    public static void setRMin(double min) {
        rMin = min;
    }

    public static double getPSlope() {
        return pSlope;
    }

    public static void setPSlope(double slope) {
        pSlope = slope;
    }

    public static double getRSlope() {
        return rSlope;
    }

    public static void setRSlope(double slope) {
        rSlope = slope;
    }
}
