/* Created on 1990-12-21 */
package cosmos;

import pl.gdan.elsy.tool.Rand;
import tool.Mat;

import java.io.Serializable;

/**
 * @author Elser
 *         <p>
 *         Describes the position of an object. X Y Z and the angles -Horizontal |Vertical
 */
public class PosXY implements Serializable {
    private static final long serialVersionUID = 1L;
    public double x, y, h;             //position
    public int angh90;        //angle (0..360)
    private double sin = 0.0, cos = 1.0;

    public static PosXY zeroZero = new PosXY(0, 0, 270/*up*/);
    public static PosXY goal[] = new PosXY[]{
            new PosXY(0, FieldDimensions.OUTER_Y + 4, 270),
            new PosXY(0, -FieldDimensions.OUTER_Y - 4, 270)
    };

    public PosXY() {
        x = 0;
        y = 0;
        h = 0;
        angh90 = 0;
    }

    public PosXY(double x, double y) {
        this.x = x;
        this.y = y;
        this.h = 0;
    }

    public PosXY(double x, double y, int angh) {
        this(x, y);
        this.angh90 = angh;
    }

    public String toString() {
        return "(x:" + x + ", y:" + y + ", angh90:" + angh90 + ")";
    }

    /**
     * @param vx
     * @param vy
     */
    public void move(double vx, double vy) {
        setx(x + vx);
        sety(y + vy);
    }

    public double distTo(PosXY point) {
        return tool.Mat.fastSqrt(Mat.sqr(this.x - point.x) + Mat.sqr(this.y - point.y));
    }

    public void setXY(PosXY setter) {
        this.x = setter.x;
        this.y = setter.y;
        this.h = setter.h;
    }

    public void set(PosXY setter) {
        setXY(setter);
        this.angh90 = setter.angh90;
    }

    public double angleTo(PosXY point, double dist, int angle) {
        if (dist == 0) {
            dist = 0.0001;
        }
        while (this.angh90 < 3600) {
            this.angh90 += 7200;
        }
        return (Mat.sin(this.angh90 + angle) * (this.x - point.x) -
                Mat.cos(this.angh90 + angle) * (this.y - point.y)) / dist;
    }

    public double left_right(PosXY point, double dist) {
        if (dist == 0) {
            return 0.0;
        }
        return (sin() * (this.x - point.x) - cos() * (this.y - point.y)) / dist;
    }

    public double ahead_back(PosXY point, double dist) {
        if (dist == 0) {
            return 0.0;
        }
        return (-cos() * (this.x - point.x) - sin() * (this.y - point.y)) / dist;
    }

    public double left_right(PosXY posXY) {
        return left_right(posXY, distTo(posXY));
    }

    public void setx(double x) {
        this.x = Mat.lim(x, FieldDimensions.INNER_X);
    }

    public void sety(double y) {
        this.y = Mat.lim(y, FieldDimensions.INNER_Y);
    }

    public void seth(double h) {
        this.h = h;
    }

    public void setAng(int ang) {
        this.angh90 = ang;
    }

    public void setXYA(double x, double y, int h) {
        setx(x);
        sety(y);
        setAng(h);
    }

    public void setxLimited(double x) {
        this.x = Mat.lim(x, FieldDimensions.INNER_X);
    }

    public void setyLimited(double y) {
        this.y = Mat.lim(y, FieldDimensions.INNER_Y);
    }

    public void setAtOtherSideNear(PosXY setter, double radius) {
        this.x = -setter.x + Rand.gauss(radius);
        this.y = -setter.y + Rand.gauss(radius);
        this.angh90 = Rand.i(360);
    }

    public void setNear(PosXY setter, int radius) {
        this.x = setter.x + Rand.gauss(radius);
        this.y = setter.y + Rand.gauss(radius);
        this.angh90 = Rand.i(360);
    }

    public double sin() {
        while (this.angh90 < 3600) {
            this.angh90 += 7200;
        }
        sin = Mat.sin(this.angh90);
        cos = Mat.cos(this.angh90);
        return sin;
    }

    public double cos() {
        while (this.angh90 < 3600) {
            this.angh90 += 7200;
        }
        sin = Mat.sin(this.angh90);
        cos = Mat.cos(this.angh90);
        return cos;
    }

    public void addVelocity(double vx, double vy, double vh) {
        setx(x + vx);
        sety(y + vy);
        seth(h + vh);
    }

    public double angledX(int a, double r) {
        return x + Mat.cos(this.angh90 + a) * r;
    }

    public double angledY(int a, double r) {
        return y + Mat.sin(this.angh90 + a) * r;
    }

    public void invert() {
        x = -x;
        y = -y;
        angh90 += 180;
    }
}