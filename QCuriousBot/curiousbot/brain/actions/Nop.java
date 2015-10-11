package curiousbot.brain.actions;

import pl.gdan.elsy.qconf.Action;

public class Nop extends Action {
    private static final long serialVersionUID = 1L;

    public int execute() {
        return 0;
    }
}
