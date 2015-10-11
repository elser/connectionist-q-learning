package game;

import cosmos.FieldDimensions;
import game.rules.GameRules;

import java.io.Serializable;

public class Match implements Serializable {
    private static final long serialVersionUID = 1L;
    // Match objects

    public Player players[] = null;
    public Team teams[] = null;
    private Ball ball = null;

    public GameRules gameRules;

    // Match properties
    public static final int REDS = 0;
    public static final int BLUES = 1;
    public int score[] = null;

    /**
     * whether players move while invoking live()
     */
    public boolean playersPlay = true;

    // methods
    public Match(GameRules gameRules, Team[] teams) {
        ball = new Ball(this);
        if (gameRules != null) {
            this.gameRules = gameRules;
            this.gameRules.attachToMatch(this);
        }
        startNew(teams);
    }

    public void live() {
        for (int i = 0; i < teams.length; i++) {
            teams[i].live();
        }
        for (int i = 0; i < teams.length; i++) {
            teams[i].getTeamFitness().countPre();
        }
        ball.live();
        for (int i = 0; i < teams.length; i++) {
            teams[i].getTeamFitness().countPost();
        }
        Fitness.staticCount();
        gameRules.applyRules();
    }


    private void createPlayersList() {
        int playersSize = teams[0].players.length * teams.length;
        if (this.players == null || this.players.length != playersSize) {
            this.players = new Player[playersSize];
        }
        int i = 0;
        for (int j = 0; j < teams.length; j++) {
            for (int k = 0; k < teams[j].players.length; k++) {
                this.players[i] = teams[j].players[k];
                this.players[i].setIdInMatch(i);
                i++;
            }
        }
    }

    public void startNew(Team[] teams) {
        Fitness.resetStatic(teams);
        this.teams = teams;
        for (int i = 0; i < teams.length; i++) {
            teams[i].reset();
        }
        createPlayersList();
        for (int i = 0; i < teams.length; i++) {
            teams[i].setMatch(this, i);
        }
        this.gameRules.start();
        ball.stop();
    }

    public Player getPlayer(int row) {
        if (row >= 0 && row < players.length) {
            return players[row];
        }
        return null;
    }

    /**
     * @param b
     */
    public void setPlayersPlay(boolean b) {
        playersPlay = b;
    }

    public Player nearestOpponentTo(Player player) {
        if (teams.length < 2) {
            return null;
        }
        Team opponentTeam = teams[1 - player.getTeam().teamColor];
        double minDist = Double.MAX_VALUE;
        Player ret = null;
        for (int i = 0; i < opponentTeam.players.length; i++) {
            Player opponent = opponentTeam.players[i];
            double dist = player.distTo(opponent);
            if (dist < minDist) {
                minDist = dist;
                ret = opponent;
            }
        }
        return ret;
    }

    public Player nearestFellowTo(Player player) {
        Team fellowTeam = player.getTeam();
        double minDist = FieldDimensions.MAX_DISTANCE * 2;
        Player ret = null;
        for (int i = 0; i < fellowTeam.players.length; i++) {
            Player fellow = fellowTeam.players[i];
            if (fellow.id != player.id) {
                double dist = player.distTo(fellow);
                if (dist < minDist) {
                    minDist = dist;
                    ret = fellow;
                }
            }
        }
        return ret;
    }

    public Ball getBall() {
        return ball;
    }

    public void movePlayersAway(int OUT_RADIUS, int exceptPlayer) {
        for (int i = 0; i < players.length; i++) {
            if (i != exceptPlayer) {
                Player player = players[i];
                double dx = player.x - ball.x;
                double dy = player.y - ball.y;
                double r = Math.sqrt(dx * dx + dy * dy) + 0.01;
                if (r < OUT_RADIUS) {
                    double vx = OUT_RADIUS * dx / r;
                    double vy = OUT_RADIUS * dy / r;
                    player.setxLimited(player.x + vx - dx);
                    player.setyLimited(player.y + vy - dy);
                }
            }
        }
    }

    public void reset() {
        ball.reset();
        for (Team team : teams) {
            team.reset();
        }
    }
}

