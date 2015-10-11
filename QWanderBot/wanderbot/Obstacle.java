package wanderbot;

import pl.gdan.elsy.tool.Rand;

import java.awt.*;

public class Obstacle {
    private static final double MIN_SIZE = 10;
    private static final double MAX_SIZE = 30;
    public double x;
    public double y;
    public double r;
    public Color c;

    public Obstacle() {
        x = Rand.d(-World.SIZE, World.SIZE);
        y = Rand.d(-World.SIZE, World.SIZE);
        r = Rand.d(MAX_SIZE - MIN_SIZE) + MIN_SIZE;
        c = Color.orange;
    }

    public boolean circleCollides(double x2, double y2, double r2) {
        double dist = getDistance(x2, y2);
        return dist - r2 < r;
    }

    public boolean pointCollides(double x2, double y2) {
        return circleCollides(x2, y2, 0);
    }

    private double getDistance(double x2, double y2) {
        double xd = (x2 - x);
        double yd = (y2 - y);
        double dist = Math.sqrt(xd * xd + yd * yd);
        return dist;
    }

}
