package curiousbot;

import curiousbot.brain.MyPerception;
import curiousbot.brain.actions.MoveBackward;
import curiousbot.brain.actions.MoveForward;
import curiousbot.brain.actions.Nop;
import curiousbot.brain.actions.TurnLeft;
import curiousbot.brain.actions.TurnRight;
import pl.gdan.elsy.qconf.Action;
import pl.gdan.elsy.qconf.Brain;
import pl.gdan.elsy.qconf.ErrorBackpropagationNN;
import pl.gdan.elsy.qconf.curiosity.CuriousBrain;
import pl.gdan.elsy.tool.Rand;

public class Player {
    public static final double TURNING_ANGLE = MyPerception.RADAR_R;
    public static final double STEP_SIZE = MyPerception.RADAR_D;
    private static final int MOVE_FORWARD = 0;
    private static final int MOVE_BACKWARD = 1;
    private static final int TURN_LEFT = 2;
    private static final int TURN_RIGHT = 3;
    private static final int NOP = 4;
    private MyPerception perception;
    private CuriousBrain brain;

    public double x;
    public double y;
    public double r;
    public double angle;
    private World world;

    public Player(World world) {
        this.world = world;
        r = 8;
        Action actionArray[] = new Action[4];
        actionArray[MOVE_FORWARD] = new MoveForward(this);
        actionArray[MOVE_BACKWARD] = new MoveBackward(this);
        actionArray[TURN_LEFT] = new TurnLeft(this);
        actionArray[TURN_RIGHT] = new TurnRight(this);
        perception = new MyPerception(this);
        perception.setAddRandomInput(true);
        brain = new CuriousBrain(perception, actionArray, new int[]{10}, new int[]{30});
        brain.setAlpha(0.1);
        brain.setGamma(0.5);
        brain.setLambda(0.5);
        brain.setRandActions(1);
        ErrorBackpropagationNN predictionNN = brain.getCuriosity().getNn();
        predictionNN.setAlpha(0.5);
        predictionNN.setMomentum(0.2);
        //CuriousPlayerPerception.setRMin(0.005);
    }

    public void count() {
        brain.count();
    }

    public void turn(double delta) {
        angle += delta;
    }

    public void moveForward(double step) {
        double xOld = x;
        double yOld = y;
        x += Math.cos(angle) * step;
        y += Math.sin(angle) * step;
        if (world.isCollision()) {
            x = xOld;
            y = yOld;
        }
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
        return brain != null && (brain.getAction() == MOVE_FORWARD) && !world.isCollision();
    }

    public boolean collides() {
        return brain != null && (brain.getAction() == MOVE_FORWARD) && world.isCollision();
    }

    public MyPerception getPerception() {
        return perception;
    }

    public Brain getBrain() {
        return brain;
    }

    public double getNovelty() {
        return perception.getNovelty();
    }

    public double getReward() {
        return perception.getReward();
    }
}
