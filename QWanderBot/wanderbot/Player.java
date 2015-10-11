package wanderbot;

import pl.gdan.elsy.qconf.Action;
import pl.gdan.elsy.qconf.Brain;
import pl.gdan.elsy.tool.Rand;
import wanderbot.brain.MyPerception;
import wanderbot.brain.actions.MoveForward;
import wanderbot.brain.actions.TurnLeft;
import wanderbot.brain.actions.TurnRight;

public class Player {
    public static final double TURNING_ANGLE = 0.5;
    public static final double STEP_SIZE = 10.0;
    public static final double AVG_FORGET = 0.01;
    private static final int MOVE_FORWARD = 0;
    private static final int TURN_LEFT = 1;
    private static final int TURN_RIGHT = 2;
    private MyPerception perception;
    private Brain brain;

    public double x;
    public double y;
    public double r;
    private double avgReward;
    public double angle;
    private World world;

    public Player(World world) {
        this.world = world;
        r = 8;
        Action actionArray[] = new Action[3];
        actionArray[MOVE_FORWARD] = new MoveForward(this);
        actionArray[TURN_LEFT] = new TurnLeft(this);
        actionArray[TURN_RIGHT] = new TurnRight(this);
        perception = new MyPerception(this);
        brain = new Brain(perception, actionArray);
        brain.setAlpha(0.1);
        brain.setGamma(0.9);
        brain.setLambda(0.5);
        brain.setRandActions(10);
    }

    public void count() {
        perception.perceive();
        avgReward = avgReward * (1 - AVG_FORGET) + perception.getReward() * AVG_FORGET;
        brain.count();
        brain.executeAction();
    }

    public void turn(double delta) {
        angle += delta;
    }

    public int moveForward(double step) {
        double xOld = x;
        double yOld = y;
        x += Math.cos(angle) * step;
        y += Math.sin(angle) * step;
        if (world.isCollision()) {
            x = xOld;
            y = yOld;
            return 0;
        }
        return 1;
    }

    public void randomizePosition() {
        while (world.isCollision()) {
            x = Rand.d(-World.SIZE, World.SIZE);
            y = Rand.d(-World.SIZE, World.SIZE);
        }
    }

    public World getWorld() {
        return world;
    }

    public boolean movingForwardWell() {
        return (brain.getAction() == MOVE_FORWARD) && (brain.getExecutionResult() == 1);
    }

    public boolean collides() {
        return (brain.getAction() == MOVE_FORWARD) && (brain.getExecutionResult() == 0);
    }

    public MyPerception getPerception() {
        return perception;
    }

    public Brain getBrain() {
        return brain;
    }

    public double getAvgReward() {
        return avgReward;
    }
}
