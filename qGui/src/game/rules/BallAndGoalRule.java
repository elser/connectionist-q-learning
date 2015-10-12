/**
 *
 */
package game.rules;

import cosmos.FieldDimensions;
import game.Ball;
import game.Player;
import game.Team;

import java.io.Serializable;

/**
 * @author Dominik
 */
public class BallAndGoalRule extends GameRules implements Serializable {
    private static final long serialVersionUID = 1L;

    public void applyRules() {
        Ball ball = match.getBall();
        for (Team team : match.teams) {
            if(team.hasScored()) {
                match.reset();
            }
        }
        if (Math.abs(ball.y) >= FieldDimensions.INNER_Y - Player.PUSH_RADIUS) {
            if (Math.abs(ball.x) <= FieldDimensions.GOAL_SIZE) {
                getGoalTeam().setScored(true);
            }
        }
        ball.bounce();
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
