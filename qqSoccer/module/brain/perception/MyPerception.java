package module.brain.perception;

import cosmos.FieldDimensions;
import cosmos.PosXY;
import game.CuriGameManager;
import game.Player;
import pl.gdan.elsy.qconf.curiosity.CuriousPlayerPerception;
import tool.Mat;

public class MyPerception extends CuriousPlayerPerception {
    private static final long serialVersionUID = 1L;
    private Player player;
    private static final boolean KNOW_KEEP_BALL = true;
    private static final boolean KNOW_ABSOLUTE_POS = true;
    private static final boolean KNOW_ENERGY = false;
    private static final boolean KNOW_GOALS_POS = false;
    private static final boolean KNOW_BORDERS = false;

    public MyPerception(Player player) {
        this.player = player;
        this.setForeseePerc(new MyForeseePerc());
    }

    @Override
    public double getReward() {
        return
                (player.getFitness().controlsBall() && !player.getFitness().keepsBall() ? 1 : 0) +
                        (player.getTeam().controlsBall() && !player.getTeam().keepsBall() ? 0.1 : 0) +
                        (player.getOpponentTeam() != null && player.getOpponentTeam().controlsBall() && !player.getOpponentTeam().keepsBall() ? -0.5 : 0);
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Prepares input for brain - it is later given on input of the first NN layer.
     */
    public void updateInputValues() {
        //setNextValue(Rand.d(-2,2), 1);
        prepareInputNumeric();
        prepareInputGeometric();
        //inputRadarIter++;inputRadarIter++;
        prepareInputBoolean();
        if (KNOW_BORDERS) {
            borderDistInput();
        }
    }

    private void prepareInputBoolean() {
        if (KNOW_KEEP_BALL) {
            setNextValue(player.getFitness().keepsBall());
            setNextValue(player.getFitness().controlsBall());
            setNextValue(player.getTeam().controlsBall());
            setNextValue((player.getOpponentTeam() != null && player.getOpponentTeam().controlsBall()));
            setNextValue(player.getTeam().keepsBall());
            setNextValue((player.getOpponentTeam() != null && player.getOpponentTeam().keepsBall()));
        }
    }

    private void prepareInputNumeric() {
        if (KNOW_ABSOLUTE_POS) {
            super.setNextValue(player.x / FieldDimensions.OUTER_X * ((player.getTeam().teamColor == 0) ? (-1) : 1));
        }
        if (KNOW_ABSOLUTE_POS) {
            super.setNextValue(player.y / FieldDimensions.OUTER_Y * ((player.getTeam().teamColor == 1) ? (-1) : 1));
        }
        if (KNOW_ENERGY) {
            super.setNextValue(getReward());
        }
    }

    private void prepareInputGeometric() {
        if (player != null && player.getTeam() != null && player.getTeam().match != null) {
            geometricInput(player.getTeam().match.getBall());
            for (Player matchPlayer : player.getTeam().match.players) {
                if (matchPlayer != player) {
                    geometricInput(matchPlayer);
                }
            }
        } else {
            geometricInput(null);
            for (int i = 0; i < CuriGameManager.PLAYERS_NO * 2 - 1; i++) {
                geometricInput(null);
            }
        }
        if (KNOW_GOALS_POS) {
            geometricInput(PosXY.goal[player.getTeam().teamColor]);
            geometricInput(PosXY.goal[1 - player.getTeam().teamColor]);
        }
    }

    private void borderDistInput() {
        for (int a = 1; a < 4; a++) {
            int angle = player.angh90 + 270 + a * 45;
            double minDist = Mat.minDist(
                    distToBorderX(-FieldDimensions.OUTER_Y, angle),
                    distToBorderX(FieldDimensions.OUTER_Y, angle),
                    distToBorderY(-FieldDimensions.OUTER_X, angle),
                    distToBorderY(FieldDimensions.OUTER_X, angle)
            ) / FieldDimensions.MAX_DISTANCE;
//			System.out.println("border="+(minDist-0.6));
            setNextValue(minDist * 2 - 1.2);
        }
    }

    private double distToBorderX(double yBorder, int angle) {
        double sin = Mat.sin(angle);
        if (sin == 0) {
            return FieldDimensions.MAX_DISTANCE;
        }
        return (yBorder - player.y) / sin;
    }

    private double distToBorderY(double xBorder, int angle) {
        double cos = Mat.cos(angle);
        if (cos == 0) {
            return FieldDimensions.MAX_DISTANCE;
        }
        return (xBorder - player.x) / cos;
    }

    private void geometricInput(PosXY point) {
        if (point == null) {
            super.setNextValue(0);
            super.setNextValue(0);
            super.setNextValue(0);
        } else {
            double distTo = player.distTo(point);
            super.setNextValue((distTo / FieldDimensions.OUTER_X) - 1);
            super.setNextValue(player.left_right(point, distTo));
            super.setNextValue(player.ahead_back(point, distTo));
        }
    }

    public boolean isUnipolar() {
        return false;
    }

}
