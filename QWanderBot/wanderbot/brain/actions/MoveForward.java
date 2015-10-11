package wanderbot.brain.actions;

import pl.gdan.elsy.qconf.Action;
import wanderbot.Player;

public class MoveForward extends Action {
    private static final long serialVersionUID = 1L;
    private Player player;

    public MoveForward(Player player) {
        this.player = player;
    }

    public int execute() {
        return player.moveForward(Player.STEP_SIZE);
    }
}
