/**
 *
 */
package game.rules;

import game.Ball;
import game.Player;

import java.io.Serializable;

/**
 * @author Dominik
 */
public class BallAndGoalRule extends GameRules implements Serializable {
    private static final long serialVersionUID = 1L;

    public void applyRules() {
        Ball ball = match.getBall();
        ball.bounce();
    }

    protected void resetPlayersPositions() {
    }

    protected boolean matchFinished() {
        return false;
    }

    public int maxTime() {
        return 2000;
    }

    public double getScore(Player player, boolean matchEnd) {
        return 0;
    }

    protected boolean divByMatchTime() {
        return false;
    }

    public double minScoreToWin() {
        return 0.5;
    }

    public double getScoreTimeIndependent(Player player) {
        return 0;
    }
}
