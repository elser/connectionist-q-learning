/**
 *
 */
package game.rules;

import game.GameManager;
import game.Match;
import game.Player;
import game.Team;

import java.io.Serializable;

/**
 * @author Dominik
 */
public abstract class GameRules implements Serializable {
    private static final long serialVersionUID = 1L;
    protected Match match = null;
    private boolean successNoted;

    public void attachToMatch(Match match) {
        this.match = match;
    }

    public abstract void applyRules();

    protected abstract boolean matchFinished();

    protected abstract boolean divByMatchTime();

    public abstract double getScore(Player player, boolean matchEnd);

    public abstract double getScoreTimeIndependent(Player player);

    public abstract double minScoreToWin();

    private int gameTime;

    protected void scoreTeams() {
        if (gameTime == 0) {
            gameTime = GameManager.getInstance().getTime();
        }
        for (int i = 0; i < match.teams.length; i++) {
            double scoreSum = 0;
            for (int j = 0; j < match.teams[i].players.length; j++) {
                scoreSum += getScore(match.teams[i].players[j], true);
            }
            if (divByMatchTime()) {
                scoreSum *= (double) maxTime() / (gameTime + 1);
            }
            for (int j = 0; j < match.teams[i].players.length; j++) {
                scoreSum += getScoreTimeIndependent(match.teams[i].players[j]);
            }
            match.teams[i].setEnergy(scoreSum);
        }
    }

    public boolean isGameOver() {
        if (!successNoted && matchFinished()) {
            successNoted = true;
            gameTime = GameManager.getInstance().getTime();
        }
        return false;
    }

    public boolean goalAchieved() {
        boolean ret = false;
        for (int i = 0; i < match.teams.length; i++) {
            ret |= match.teams[i].getEnergy() >= minScoreToWin();
        }
        return ret;
    }

    protected Team getGoalTeam() {
        return match.teams[(match.getBall().y > 0) ? 0 : 1];
    }

    public void start() {
        successNoted = false;
        gameTime = 0;
    }

    public int maxTime() {
        return 3000;
    }

}
