package game;

import cosmos.FieldDimensions;

import java.io.Serializable;

public class Fitness implements Serializable {
    private static final long serialVersionUID = 1L;

    private int passesToFellows;
    private double passesToFDistance;
    private double passesToODistance;
    private double passesToFDistanceSum;
    private double passesToODistanceSum;
    private int passesToOpponents;
    private Player player;

    private static int keepBits;
    private static int controlBits;
    private static int keepBitsPrev;
    private static int controlBitsPrev;
    private int keepingTime;
    private int controlTime;
    private double ballProgress;

    private int myBit;

    private int teamId;
    private static int teamMask[] = new int[2];
    private int takeOvers;
    private double takeOverDistance;
    private double walkDistance;
    private int rotationSum;
    private int outs;

    private boolean crashBorder;

    public Fitness(Player player) {
        this.player = player;
        reset();
    }

    public void count() {
        Ball ball = player.getTeam().match.getBall();
        passesToFDistance = 0;
        passesToODistance = 0;
        ballProgress = 0;
        if (controlledBallPrev() && keepBitsPrev == 0 && keepBits != 0) {
            if ((keepBits & (teamMask[teamId] & ~myBit)) != 0) {
                if (GameManager.getInstance().getTime() > 0) {
                    passesToFellows++;
                }
                passesToFDistance = ball.distTo(ball.lastKeepPos);
                passesToFDistanceSum += passesToFDistance;
            }
            if ((keepBits & teamMask[1 - teamId]) != 0) {
                passesToOpponents++;
                passesToODistance = ball.distTo(ball.lastKeepPos);
                passesToODistanceSum += passesToODistance;
            }
        }
        if (keepsBall()) {
            keepingTime++;
        }
        if (controlsBall()) {
            controlTime++;
            ballProgress = ball.distToGoalProgress(player.getTeam().teamColor);
        }
        crashBorder = Math.abs(player.x) == FieldDimensions.OUTER_X || Math.abs(player.y) == FieldDimensions.OUTER_Y;
    }

    public void reset() {
        myBit = 1 << player.getIdInMatch();
        teamId = player.getTeam().teamColor;
        keepingTime = 0;
        controlTime = 0;
        passesToFellows = 0;
        passesToFDistanceSum = 0;
        passesToODistanceSum = 0;
        passesToOpponents = 0;
        ballProgress = 0;
        takeOvers = 0;
        takeOverDistance = 0;
        walkDistance = 0;
        rotationSum = 0;
        outs = 0;
    }

    public void gotBall() {
        Ball ball = player.getTeam().match.getBall();
        takeOverDistance = 0;
        if (!controlsBall()) {
            takeOvers++;
            takeOverDistance = ball.distTo(ball.lastKeepPos);
        }
        keepsBall(true);
        controlsBall(true);
        controlBits &= keepBits;
    }

    private void controlsBall(boolean b) {
        if (b) {
            controlBits |= myBit;
        } else {
            controlBits &= ~myBit;
        }
    }

    private void keepsBall(boolean b) {
        if (b) {
            keepBits |= myBit;
        } else {
            keepBits &= ~myBit;
        }
    }

    public void lostBall() {
        keepsBall(false);
    }

    public static void resetStatic(Team[] teams) {
        keepBits = 0;
        controlBits = 0;
        for (int c = 0; c < teams.length; c++) {
            teamMask[c] = 0;
            for (int i = 0; i < teams[c].players.length; i++) {
                teamMask[c] |= 1 << (i + c * teams[c].players.length);
            }
        }
    }

    public static void staticCount() {
        keepBitsPrev = keepBits;
        controlBitsPrev = controlBits;
    }

    public boolean controlsBall() {
        return (controlBits & myBit) != 0;
    }

    public boolean keepsBall() {
        return (keepBits & myBit) != 0;
    }

    private boolean controlledBallPrev() {
        return (controlBitsPrev & myBit) != 0;
    }

    public static boolean teamControlsBall(int teamColor) {
        return (controlBits & teamMask[teamColor]) != 0;
    }

    static boolean teamKeepsBall(int teamColor) {
        return (keepBits & teamMask[teamColor]) != 0;
    }

    public int getControlTime() {
        return controlTime;
    }

    public int getKeepingTime() {
        return keepingTime;
    }

    public double getPassesToFDistanceSum() {
        return passesToFDistanceSum;
    }

    public int getPassesToFellows() {
        return passesToFellows;
    }

    public int getPassesToOpponents() {
        return passesToOpponents;
    }

    public double getBallProgress() {
        return ballProgress;
    }

    /**
     * @return takeovers
     */
    public int takeOvers() {
        return takeOvers;
    }

    public double getWalkDistance() {
        return walkDistance;
    }

    public int getRotationSum() {
        return rotationSum;
    }

    public int getOuts() {
        return outs;
    }

    /**
     *
     */
    public static void resetControlBits() {
        controlBits = 0;
        keepBits = 0;
    }

    public double getPassesToODistanceSum() {
        return passesToODistanceSum;
    }

    public double getPassesToFDistance() {
        return passesToFDistance;
    }

    public double getPassesToODistance() {
        return passesToODistance;
    }

    public double getTakeOverDistance() {
        return takeOverDistance;
    }

    /**
     *
     */
    public void countPre() {
        takeOverDistance = 0;
    }

    public static boolean ballIsControlled() {
        return controlBits != 0;
    }

    public void setCrashBorder(boolean b) {
        this.crashBorder = b;
    }

    public boolean isCrashBorder() {
        return crashBorder;
    }
}
