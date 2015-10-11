/*
 * Created on 2005-06-08
 */
package game;

import cosmos.Body;
import cosmos.FieldDimensions;
import cosmos.PosXY;
import module.moveControl.RadialVehicle;
import tool.Mat;

/**
 * @author Dominik
 */
public class Ball extends Body {
    private static final double BOUNCE_A = 0.5;

    private static final double BOUNCE_B = 0.5;

    private static final long serialVersionUID = 1L;

    private Match match;

    private boolean isKept;

    private double vx;

    private double vy;

    private double vh;

    public PosXY lastKeepPos;

    public static final double LEG_LENGTH = 5;

    public static final double LEG_RADIUS = 10;

    public static final double KICK_FORCE = 2.0;

    public static final double KICK_FORCE_VERT = 0.1;

    public static final double KEEP_BALL_FORCE = 0.08;

    public static final double PICKUP_FORCE = 0.0;

    public static final double PICKUP_RADIUS = 1.0;

    public static final double PLAYER_HEIGHT = 4.0;

    public static final double FADEOUT = 0.98;

    public static final double FADEOUT_KEEPBALL = 0.8;

    public static final double FADEOUT_FLY = 0.99;

    public static double MAX_SPEED = 400;

    private double distToGoal[] = new double[2];

    private double distToGoalProgress[] = new double[2];

    public int flyTime;

    private boolean isKicked;

    public Ball(Match match) {
        super();
        this.match = match;
        this.lastKeepPos = new PosXY();
    }

    public void live() {
        for (int i = 0; i < distToGoal.length; i++) {
            double currDistToGoal = this.distTo(PosXY.goal[i]);
            distToGoalProgress[i] = currDistToGoal - distToGoal[i];
            distToGoal[i] = currDistToGoal;
        }
        if (isKept) {
            this.lastKeepPos.set(this);
            flyTime = 0;
        } else {
            flyTime++;
        }
        isKept = false;
        isKicked = false;
        for (int i = 0; i < match.players.length; i++) {
            Player player = match.players[i];
            if (h <= PLAYER_HEIGHT) {
                double r = this.distTo(player);
                if (r > 0 && r < LEG_RADIUS + LEG_LENGTH) {
                    countPickups(player, r);
                    countBallKeepingAndKicking(player);
                } else {
                    player.getFitness().lostBall();
                }
            } else {
                player.getFitness().lostBall();
            }
        }
        fadeout();
        addVelocity(vx, vy, vh);
        if (h < 0.0) {
            seth(-h);
            if (vh < -0.04) {
                vh = -vh * 0.8;
            } else {
                vh = 0;
            }
        } else if (h > 0.1) {
            vh += -0.01;
        }
        if (!isKept && Fitness.ballIsControlled() && flyTime > 30) {
            Fitness.resetControlBits();
        }
    }

    private double getSqrVelocity() {
        return vx * vx + vy * vy;
    }

    private void fadeout() {
        if (isKept && !isKicked) {
            vx *= FADEOUT_KEEPBALL;
            vy *= FADEOUT_KEEPBALL;
        } else if (h > 0.1) {
            vx *= FADEOUT_FLY;
            vy *= FADEOUT_FLY;
            vh *= FADEOUT_FLY;
        } else {
            vx *= FADEOUT;
            vy *= FADEOUT;
        }
        vx = Mat.lim(vx, MAX_SPEED);
        vy = Mat.lim(vy, MAX_SPEED);
    }

    /**
     * Liczy odbicia pi?ki
     *
     * @param player
     */
    private double countPickups(Player player, double r) {
        double dx = x - player.x;
        double dy = y - player.y;
        if (r > 0 && r < PICKUP_RADIUS) {
            vx += PICKUP_FORCE * dx / (r + PICKUP_RADIUS);
            vy += PICKUP_FORCE * dy / (r + PICKUP_RADIUS);
        }
        return r;
    }

    /**
     * @param player
     * @return
     */
    private boolean countBallKeepingAndKicking(Player player) {
        double sinH = player.sin(), cosH = player.cos();
        RadialVehicle vehicle = player.getVehicle();
        if (!isKicked) {
            double dx = (player.x + LEG_LENGTH * cosH) - x;
            double dy = (player.y + LEG_LENGTH * sinH) - y;
            double r = dx * dx + dy * dy;
            if (r < LEG_RADIUS * LEG_RADIUS && r > 0) {
                // keep the ball
                if (flyTime == 0 || flyTime > 80) {
                    if (!vehicle.isKicking()) {
                        double sqrt = Mat.fastSqrt(r);
                        double multi = KEEP_BALL_FORCE * sqrt;
                        vx += multi * dx;
                        vy += multi * dy;
                    }
                    isKept = true;
                }
                player.getFitness().gotBall();
            } else {
                player.getFitness().lostBall();
            }
        } else {
            player.getFitness().lostBall();
        }
        if (flyTime <= 1 && player.getFitness().keepsBall() && vehicle.isKicking()) {
            vx = cosH * KICK_FORCE;
            vy = sinH * KICK_FORCE;
            vh += KICK_FORCE_VERT;
            flyTime++;
            isKicked = true;
        }
        return player.getFitness().keepsBall();
    }

    /**
     * Makes ball pick up from the walls surrounding the soccer field.
     */
    public void bounce() {
        if (!Mat.inside(x, FieldDimensions.OUTER_X)) {
            vx = -vx * BOUNCE_A;
            vy = vy * BOUNCE_B;
            setxLimited(x, 0);
        }
        if (!Mat.inside(y, FieldDimensions.OUTER_Y)) {
            vx = vx * BOUNCE_B;
            vy = -vy * BOUNCE_A;
            setyLimited(y, 0);
        }
    }

    public void stop() {
        vx = 0;
        vy = 0;
        vh = 0;
        isKept = false;
        flyTime = 0;
        this.lastKeepPos.set(this);
    }

    public double speed() {
        return Math.sqrt(getSqrVelocity());
    }

    public double distToGoalProgress(int teamColor) {
        return distToGoalProgress[teamColor];
    }
}
