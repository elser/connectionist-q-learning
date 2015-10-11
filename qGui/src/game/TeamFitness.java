package game;

import java.io.Serializable;

public class TeamFitness implements Serializable {
    private static final long serialVersionUID = 1L;

    public String toString() {
        String ret = "";
        ret += "passesToFellows: " + passesToFellows() + "\n";
        ret += "passesToOpponents: " + passesToOpponents() + "\n";
        ret += "passesToFellowsDistSum: " + passesToFellowsDistSum() + "\n";
        ret += "getSumaricBallPosition: " + getAvgBallPosition() + "\n";
        return ret;
    }

    private Team team;

    private double ballPosition;

    public TeamFitness(Team team) {
        this.team = team;
    }

    public int passesToFellows() {
        int ret = 0;
        for (int i = 0; i < team.players.length; i++) {
            ret += team.players[i].getFitness().getPassesToFellows();
        }
        return ret;
    }

    public int passesToOpponents() {
        int ret = 0;
        for (int i = 0; i < team.players.length; i++) {
            ret += team.players[i].getFitness().getPassesToOpponents();
        }
        return ret;
    }

    public void countPre() {
        for (int i = 0; i < team.players.length; i++) {
            team.players[i].getFitness().countPre();
        }
    }

    public void countPost() {
        for (int i = 0; i < team.players.length; i++) {
            team.players[i].getFitness().count();
        }
        ballPosition += team.match.getBall().y;
    }

    public int controlTime() {
        int ret = 0;
        for (int i = 0; i < team.players.length; i++) {
            ret += team.players[i].getFitness().getControlTime();
        }
        return ret;
    }

    public double passesToFellowsDistSum() {
        double ret = 0;
        for (int i = 0; i < team.players.length; i++) {
            ret += team.players[i].getFitness().getPassesToFDistanceSum();
        }
        return ret;
    }

    public boolean keepsBall() {
        return Fitness.teamKeepsBall(team.teamColor);
    }

    public boolean controlsBall() {
        return Fitness.teamControlsBall(team.teamColor);
    }

    /**
     *
     */
    public void reset() {
        ballPosition = 0;
    }

    public double getAvgBallPosition() {
        return ballPosition / GameManager.getInstance().getTime();
    }

    public double passesToOpponentsDistSum() {
        double ret = 0;
        for (int i = 0; i < team.players.length; i++) {
            ret += team.players[i].getFitness().getPassesToODistanceSum();
        }
        return ret;
    }
}
 