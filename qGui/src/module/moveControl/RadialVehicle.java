/* Created on 1990-12-21 */
package module.moveControl;

import game.Player;

import java.io.Serializable;

/**
 * @author Elser
 */
public class RadialVehicle implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int TURN_ANGLE = 20;
    private static final double SPEED = 5.0;
    private VehAction[] actions;
    protected Player player;
    private boolean kicking;

    public RadialVehicle(final Player player) {
        VehAction goStraight = new VehAction() {
            private static final long serialVersionUID = 1L;

            public int execute() {
                double speed = SPEED;
                if (!player.getFitness().keepsBall()) {
                    player.setxLimited(player.x + player.cos() * speed, Player.PUSH_RADIUS);
                    player.setyLimited(player.y + player.sin() * speed, Player.PUSH_RADIUS);
                }
                return 0;
            }
        };
        VehAction turnLeft = new VehAction() {
            private static final long serialVersionUID = 1L;

            public int execute() {
                player.angh90 -= TURN_ANGLE;
                return 0;
            }
        };
        VehAction turnRight = new VehAction() {
            private static final long serialVersionUID = 1L;

            public int execute() {
                player.angh90 += TURN_ANGLE;
                return 0;
            }
        };
        VehAction kick = new VehAction() {
            private static final long serialVersionUID = 1L;

            public int execute() {
                kicking = true;
                return 0;
            }
        };
        actions = new VehAction[]{goStraight, turnLeft, turnRight, kick};
    }

    public boolean isKicking() {
        return kicking;
    }

    public void setKicking(boolean kicking) {
        this.kicking = kicking;
    }

    public VehAction[] getActions() {
        return actions;
    }

    public void count() {
        kicking = false;
    }
}
