package module.brain.perception;

import cosmos.FieldDimensions;
import cosmos.PosXY;
import game.Ball;
import game.CuriGameManager;
import game.Player;
import pl.gdan.elsy.qconf.curiosity.CuriousPlayerPerception;
import tool.Mat;

public class MyPerception extends CuriousPlayerPerception {
    private static final long serialVersionUID = 1L;
    private Player player;

    public MyPerception(Player player) {
        this.player = player;
        this.setForeseePerc(new MyForeseePerc());
    }

    @Override
    public double getReward() {
        double ret = 0;
//        if (player.getFitness().controlsBall() && !player.getFitness().keepsBall()) {
//            ret = 0.05;
//        }
//        if (player.getTeam().controlsBall() && !player.getTeam().keepsBall()) {
//            ret += 0.01;
//        }
        if (player.getTeam().match != null) {
            final Ball ball = player.getTeam().match.getBall();
            ret += (player.getTeam().teamColor == 0 ? 1 : -1) * ball.vy * 0.001;
        }
        if (player.getTeam().hasScored()) {
            ret += 1;
        }
        if (player.getOpponentTeam() != null) {
//            if (player.getOpponentTeam().controlsBall() && !player.getOpponentTeam().keepsBall()) {
//                ret -= 0.01;
//            }
            if (player.getOpponentTeam().hasScored()) {
                ret -= 0.5;
            }
        }
        return ret;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Prepares input for brain - it is later given on input of the first NN layer.
     */
    public void updateInputValues() {
        prepareInputNumeric();
        prepareInputGeometric();
        prepareInputBoolean();
        borderDistInput();
    }

    private void prepareInputBoolean() {
        setNextValue(player.getFitness().keepsBall());
        setNextValue(player.getFitness().controlsBall());
        setNextValue(player.getTeam().controlsBall());
        setNextValue((player.getOpponentTeam() != null && player.getOpponentTeam().controlsBall()));
        setNextValue(player.getTeam().keepsBall());
        setNextValue(player.getOpponentTeam() != null && player.getOpponentTeam().keepsBall());
    }

    private void prepareInputNumeric() {
        setNextValue(player.x / FieldDimensions.INNER_X);
        setNextValue(player.y / FieldDimensions.INNER_Y);
        if (player != null && player.getTeam() != null && player.getTeam().match != null) {
            setNextValue(player.getTeam().match.getBall().x / FieldDimensions.INNER_X);
            setNextValue(player.getTeam().match.getBall().y / FieldDimensions.INNER_Y);
        } else {
            setNextValue(0);
            setNextValue(0);
        }
        setNextValue(getReward());
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
        geometricInput(PosXY.goal[player.getTeam().teamColor]);
        geometricInput(PosXY.goal[1 - player.getTeam().teamColor]);
    }

    private void borderDistInput() {
        for (int a = 1; a < 4; a++) {
            int angle = player.angh90 + 270 + a * 45;
            double minDist = Mat.minDist(
                    distToBorderX(-FieldDimensions.INNER_Y, angle),
                    distToBorderX(FieldDimensions.INNER_Y, angle),
                    distToBorderY(-FieldDimensions.INNER_X, angle),
                    distToBorderY(FieldDimensions.INNER_X, angle)
            ) / FieldDimensions.MAX_DISTANCE;
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
            super.setNextValue((distTo / FieldDimensions.INNER_X) - 1);
            super.setNextValue(player.left_right(point, distTo));
            super.setNextValue(player.ahead_back(point, distTo));
        }
    }

    public boolean isUnipolar() {
        return false;
    }

}
