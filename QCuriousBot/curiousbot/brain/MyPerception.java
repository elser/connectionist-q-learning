package curiousbot.brain;

import curiousbot.Player;
import pl.gdan.elsy.qconf.curiosity.CuriousPlayerPerception;

public class MyPerception extends CuriousPlayerPerception {
    public static final int RADAR_ANGLES = 7;
    public static final int RADAR_DISTS = 4;
    private static final long serialVersionUID = 1L;
    public static final double RADAR_R = 0.4;
    public static final double RADAR_D = 10;
    public static final int RADAR_D0 = 1;
    private Player player;

    public MyPerception(Player player) {
        this.player = player;
        // we omit the prediction of the farthest radar input
        this.setForeseePerc(new MyForeseePerc());
    }

    protected void updateInputValues() {
        for (int d = RADAR_D0; d <= RADAR_DISTS; d++) {
            for (int a = -RADAR_ANGLES; a <= RADAR_ANGLES; a++) {
                double xPerc = xPerc(d, a);
                double yPerc = yPerc(d, a);
                setNextValue(player.getWorld().pointInObstacle(xPerc, yPerc));
            }
        }
    }

    public double yPerc(int d, int a) {
        return player.y + Math.sin(player.angle - a * RADAR_R) * d * RADAR_D;
    }

    public double xPerc(int d, int a) {
        return player.x + Math.cos(player.angle - a * RADAR_R) * d * RADAR_D;
    }

    public boolean isUnipolar() {
        return false;
    }

    public Player getPlayer() {
        return player;
    }

}
