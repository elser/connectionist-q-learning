package curiousbot.brain.actions;

import curiousbot.Player;
import pl.gdan.elsy.qconf.Action;

public class TurnLeft extends Action {
    private static final long serialVersionUID = 1L;
    private Player player;

    public TurnLeft(Player player) {
        this.player = player;
    }

    public int execute() {
        player.turn(-Player.TURNING_ANGLE);
        return 0;
    }

}
