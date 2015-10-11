package curiousbot.brain.actions;

import curiousbot.Player;
import pl.gdan.elsy.qconf.Action;

public class MoveForward extends Action {
    private static final long serialVersionUID = 1L;
    private Player player;

    public MoveForward(Player player) {
        this.player = player;
    }

    public int execute() {
        player.moveForward(Player.STEP_SIZE);
        return 0;
    }
}
