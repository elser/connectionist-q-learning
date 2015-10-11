/*
 * Created on Jul 21, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package game;

import tool.Mat;

import java.io.Serializable;

/**
 * @author elser
 *         <p>
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class Team implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int MAX_ENERGY = 1000000;
    public Player players[];
    public int teamColor = 0;
    public TeamFitness teamFitness;

    public Match match;
    private double energy;
    private int childrenNo;
    private int brainType;

    public Team(int nPlayers) {
        energy = 0;
        players = new Player[nPlayers];
        teamFitness = new TeamFitness(this);
        for (int i = 0; i < nPlayers; i++) {
            players[i] = newPlayer();
            players[i].setIdInTeam(i);
        }
        reset();
    }

    protected abstract Player newPlayer();

    public void setMatch(Match match, int teamColor) {
        this.match = match;
        this.teamColor = teamColor;
        for (int i = 0; i < players.length; i++) {
            players[i].prepareToMatch();
        }
    }

    public void live() {
        for (int i = 0; i < players.length; i++) {
            players[i].live();
        }
    }

    public void setEnergy(double d) {
        energy = Mat.lim(d, 0, MAX_ENERGY);
    }

    public double getEnergy() {
        return energy;
    }

    public void reset() {
        //Fitness.resetStatic();
        teamFitness.reset();
        for (int i = 0; i < players.length; i++) {
            players[i].getFitness().reset();
        }
    }

    public void resetEvaluation() {
        energy = 0;
        for (int i = 0; i < players.length; i++) {
            players[i].resetEvaluation();
        }
    }

    public boolean controlsBall() {
        return Fitness.teamControlsBall(teamColor);
    }

    public boolean keepsBall() {
        return Fitness.teamKeepsBall(teamColor);
    }

    public TeamFitness getTeamFitness() {
        return teamFitness;
    }

    public int getChildrenNo() {
        return childrenNo;
    }

    public void setChildrenNo(int childrenNo) {
        this.childrenNo = childrenNo;
    }

    public int getBrainType() {
        return brainType;
    }

    public void setBrainType(int brainType) {
        this.brainType = brainType;
    }

}
