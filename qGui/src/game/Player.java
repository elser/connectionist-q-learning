/* Created on 1990-12-22 */
package game;


import cosmos.Body;
import cosmos.PosXY;
import module.moveControl.RadialVehicle;

import java.awt.*;
import java.io.Serializable;

/**
 * @author Elser
 */
public abstract class Player extends Body implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final double PUSH_RADIUS = 8.0;
    private static final double PUSH_FORCE = 0.9;
    private Fitness fitness;
    private Team team;
    public int idInTeam;
    private Player nearestFellow;
    private Player nearestOpponent;
    private int idInMatch;
    protected RadialVehicle vehicle;

    public Player(Team team) {
        super();
        this.team = team;
        this.fitness = new Fitness(this);
        vehicle = new RadialVehicle(this);
    }

    public void live() {
        nearestFellow = team.match.nearestFellowTo(this, nearestFellow);
        nearestOpponent = team.match.nearestOpponentTo(this, nearestOpponent);
        countPushingOthers();
        vehicle.count();
    }

    private void countPushingOthers() {
        if (nearestFellow != null) {
            push(nearestFellow);
        }
        if (nearestOpponent != null) {
            push(nearestOpponent);
        }
    }

    private void push(PosXY otherPlayer) {
        double dx = x - otherPlayer.x;
        double dy = y - otherPlayer.y;
        double r = distTo(otherPlayer);
        if (r > 0 && r < PUSH_RADIUS) {
            double vx = PUSH_FORCE * dx / r;
            double vy = PUSH_FORCE * dy / r;
            addVelocity(vx, vy, 0);
            otherPlayer.addVelocity(-vx, -vy, 0);
        }
    }

    /**
     * @return id
     */
    public int getId() {
        return idInTeam;
    }

    public Color getColorRGB() {
        return (team.teamColor == 0) ? Color.red : Color.blue;
    }

    public void prepareToMatch() {
        nearestFellow = null;
        nearestOpponent = null;
        fitness.reset();
    }

    public void resetEvaluation() {
        fitness.reset();
    }

    public void setIdInMatch(int i) {
        this.idInMatch = i;
    }

    int getIdInMatch() {
        return idInMatch;
    }

    public Team getOpponentTeam() {
        if (team == null || team.match == null || team.match.teams == null
                || team.match.teams.length < 2) {
            return null;
        }
        return team.match.teams[1 - team.teamColor];
    }

    public Fitness getFitness() {
        return fitness;
    }

    public Player getNearestFellow() {
        return nearestFellow;
    }

    public Player getNearestOpponent() {
        return nearestOpponent;
    }

    public void place(double xx, double yy, int ang) {
        setXYA(xx, yy, ang);
    }

    public RadialVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(RadialVehicle movCtrl) {
        this.vehicle = movCtrl;
    }

    /**
     * @return the idInTeam
     */
    public int getIdInTeam() {
        return idInTeam;
    }

    /**
     * @param idInTeam the idInTeam to set
     */
    public void setIdInTeam(int idInTeam) {
        this.idInTeam = idInTeam;
    }

    /**
     * @return the team
     */
    public Team getTeam() {
        return team;
    }
}
