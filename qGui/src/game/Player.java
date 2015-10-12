/* Created on 1990-12-22 */
package game;


import cosmos.Body;
import cosmos.FieldDimensions;
import cosmos.PosXY;
import module.moveControl.RadialVehicle;

import java.io.Serializable;

/**
 * @author Elser
 */
public abstract class Player extends Body implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final double PUSH_RADIUS = 10.0;
    private static final double PUSH_FORCE = 1.1;
    private Fitness fitness;
    private Team team;
    public int idInTeam;
    private int idInMatch;
    protected RadialVehicle vehicle;

    public Player(Team team) {
        super();
        this.team = team;
        this.fitness = new Fitness(this);
        vehicle = new RadialVehicle(this);
    }

    public void live() {
        countPushingOthers();
        vehicle.count();
    }

    private void countPushingOthers() {
        Player nearestFellow = team.match.nearestFellowTo(this);
        Player nearestOpponent = team.match.nearestOpponentTo(this);
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

    public void prepareToMatch() {
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

    public void reset() {
        getFitness().reset();
        x = (idInTeam - 0.5 * team.players.length + 0.5) * FieldDimensions.INNER_X / 2;
        y = (team.teamColor - 0.5) * FieldDimensions.INNER_Y / 2;
    }
}
