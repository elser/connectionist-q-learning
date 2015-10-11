package curiousbot.brain.actions;

import curiousbot.Player;
import pl.gdan.elsy.qconf.Action;

public class TurnRight extends Action {
    private static final long serialVersionUID = 1L;
    private Player player;

    public TurnRight(Player player) {
        this.player = player;
    }

    public int execute() {
        player.turn(Player.TURNING_ANGLE);
        return 0;
    }

}
