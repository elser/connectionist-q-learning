package wanderbot.brain;

import pl.gdan.elsy.qconf.Perception;
import wanderbot.Player;

public class MyPerception extends Perception {
    private static final long serialVersionUID = 1L;
    private static final double RADAR_R = 0.5;
    private static final double RADAR_D = 10;
    private Player player;

    public MyPerception(Player player) {
        this.player = player;
    }

    public double getReward() {
        if (player.movingForwardWell()) {
            return 0.1;
        }
        if (player.collides()) {
            return -0.2;
        }
        return 0.0;
    }

    protected void updateInputValues() {
        for (int d = 1; d <= 3; d++) {
            for (int a = -1; a <= 1; a++) {
                double xPerc = xPerc(d, a);
                double yPerc = yPerc(d, a);
                setNextValue(player.getWorld().pointInObstacle(xPerc, yPerc) ? 5 : -5);
            }
        }
    }

    public double yPerc(int d, int a) {
        return player.y + Math.sin(player.angle - a * RADAR_R) * d * RADAR_D;
    }

    public double xPerc(int d, int a) {
        return player.x + Math.cos(player.angle - a * RADAR_R) * d * RADAR_D;
    }

    @Override
    public boolean isUnipolar() {
        return true;
    }

}
