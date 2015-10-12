package gui;

import cosmos.FieldDimensions;
import facade.Facade;
import game.Ball;
import game.CuriPlayer;
import game.GameManager;
import game.Match;
import game.Player;
import module.moveControl.RadialVehicle;
import pl.gdan.elsy.tool.Rand;
import tool.Mat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;

public class FieldGraphic {
    private static final int SHADOW_LEVELS = 40;
    private static final String SOCCER_FIELD_GIF = "soccer_field_small.gif";
    private static final int SELECTION_RING_RADIUS = 14;
    private static boolean drawPlayerVariables = false;
    private static boolean drawBorderDist = false;
    private static boolean drawVectorToBall = false;
    private static boolean drawTraces = false;
    private Image backgroundImage;
    private BufferedImage bi;
    private BufferedImage backgroundImageTransparent;
    int width;
    int height;
    float scaleX;
    float scaleY;
    private Graphics g;
    /**
     * Draws the whole population on the BufferedImage bi.
     */
    private double scale;
    private FrameField frameField;
    private Color[][] colorTable;
    private static boolean transpDone;
    public static double UGLY_GLOBAL;

    public Image toImage() {
        Match match = Facade.match();
        Player[] players = match.players;
        Player selected = Facade.getSelectedPlayer();
        Ball ball = match.getBall();
        Image toDraw =
                (!drawTraces) ?
                        backgroundImage :
                        backgroundImageTransparent;
        if (!g.drawImage(toDraw, 0, 0, null)) {
            return bi;
        } else {
            scale = (float) (bi.getHeight() / FieldDimensions.OUTER_Y / 2);
            if (!transpDone) {
                transp();
                transpDone = true;
            }
        }
        if (false) {
            drawGaussTest();
        }
        g.drawString("" + UGLY_GLOBAL + " kb=" + selected.getFitness().keepsBall() + " cb=" + selected.getFitness().controlsBall() + " tkb=" +
                selected.getTeam().keepsBall() + " tcb=" + selected.getTeam().controlsBall(), 10, 10);
        drawTimeline(match);
        if (false) {
            drawMatchBallPosition(match);
        }
        if (false) {
            drawGeneralText();
        }
        //draw radar output
        if (selected != null) {
            if (drawBorderDist) {
                drawBorderDist(selected);
            }
        }
        drawPassDist(match);

        //draw players and connections between them
        for (int i = 0; players != null && i < players.length; i++) {
            drawPlayer(players[i], i);
        }
        drawBall(ball);
        drawGoals();
        return bi;
    }

    private void drawGoals() {
        for (int i = 0; i < 2; i++) {
            int flip = i * 2 - 1;
            final int goalDepth = 10;
            drawRect(-FieldDimensions.GOAL_SIZE, flip * FieldDimensions.INNER_Y + ((i-1) * goalDepth),
                    2 * FieldDimensions.GOAL_SIZE, goalDepth);
        }
    }

    private void transp() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int col = backgroundImageTransparent.getRGB(x, y);
                col = (col & 0xFFFFFF) | (32 << (6 * 4));
                backgroundImageTransparent.setRGB(x, y, col);
            }
        }
    }

    private void drawGeneralText() {
        g.setColor(Color.white);
        int y = 10;
        String descr;
        descr = "xxx";
        g.drawString(descr, 10, y += 10);
    }

    private void drawTimeline(Match match) {
        g.setColor(Color.white);
        g.drawLine(0, 1, bi.getWidth() * GameManager.getInstance().getTime() / match.gameRules.maxTime(), 1);
    }

    private void drawMatchBallPosition(Match match) {
        g.setColor(Color.orange);
        drawLine(0, 0, 0, match.teams[0].teamFitness.getAvgBallPosition());
    }

    private void drawPassDist(Match match) {
        try {
            g.setColor(Color.red);
            drawLine(-1, -FieldDimensions.OUTER_Y, -1, match.teams[0].teamFitness.passesToFellowsDistSum() * FieldDimensions.OUTER_Y / 400 - FieldDimensions.OUTER_Y);
            g.setColor(Color.blue);
            drawLine(1, -FieldDimensions.OUTER_Y, 1, match.teams[0].teamFitness.passesToOpponentsDistSum() * FieldDimensions.OUTER_Y / 400 - FieldDimensions.OUTER_Y);


            g.setColor(Color.red);
            drawLine(1, FieldDimensions.OUTER_Y, 1, -match.teams[1].teamFitness.passesToOpponentsDistSum() * FieldDimensions.OUTER_Y / 400 + FieldDimensions.OUTER_Y);
            g.setColor(Color.blue);
            drawLine(-1, FieldDimensions.OUTER_Y, -1, -match.teams[1].teamFitness.passesToFellowsDistSum() * FieldDimensions.OUTER_Y / 400 + FieldDimensions.OUTER_Y);
        } catch (Exception e) {
        }
    }

    private void drawBall(Ball ball) {
        int ballRadius = 7;
        if (ball.h > 0.001) {
            g.setColor(Color.black);
            fillCircle(
                    ball.x + ball.h,
                    ball.y + ball.h,
                    ballRadius);
        }
        g.setColor(Color.yellow);
        drawCircle(ball.lastKeepPos.x, ball.lastKeepPos.y, ballRadius);
        fillCircle(ball.x, ball.y, ballRadius);
    }

    private void drawBorderDist(Player selected) {
        for (int a = 0; a < 5; a++) {
            double d = 1;//selected.brain.getPerception().input[a+18];
            drawLine(selected.x, selected.y,
                    selected.x + Mat.cos(selected.angh90 - 90 + a * 45) * d,
                    selected.y + Mat.sin(selected.angh90 - 90 + a * 45) * d);
        }
    }

    private void drawGaussTest() {
        int gau[] = new int[200];
        int i70 = 30;
        for (int i = 0; i < 10000; i++) {
            gau[(int) Mat.lim(Rand.gauss(i70) + gau.length / 2, 0, gau.length - 1)]++;
        }
        for (int i = 0; i < gau.length; i++) {
            if (Math.abs(gau.length / 2 - i) == i70) {
                g.setColor(Color.white);
            } else {
                g.setColor(Color.red);
            }
            g.drawLine(i + 50, 500, i + 50, 500 - gau[i]);
        }
    }

    private void drawLine(double x1, double y1, double x2, double y2) {
        g.drawLine(scaledX(x1), scaledY(y1), scaledX(x2), scaledY(y2));
    }

    protected void drawRect(double x, double y, double w, double h) {
        g.drawRect(scaledX(x), scaledY(y), scaled(w), scaled(h));
    }

    private void drawCircle(double x, double y, int r) {
        g.drawOval(scaledX(x) - r, scaledY(y) - r, 2 * r, 2 * r);
    }

    private void fillCircle(double x, double y, int r) {
        g.fillOval(scaledX(x) - r, scaledY(y) - r, 2 * r, 2 * r);
    }

    private int scaledX(double x) {
        return (int) ((FieldDimensions.OUTER_X + x) * scale);
    }

    private int scaledY(double y) {
        return (int) ((FieldDimensions.OUTER_Y + y) * scale + 3);
    }

    private int scaled(double d) {
        return (int) (d * scale);
    }

    static int[] x = new int[4];
    static int[] y = new int[4];
    static int[] x2 = new int[4];
    static int[] y2 = new int[4];

    /**
     * Draws player on the Graphics g in the position given by parameters.
     *
     * @param id
     */
    private void drawPlayer(Player player, int id) {
        int xp = scaledX(player.x);
        int yp = scaledY(player.y);
        if (drawPlayerVariables) {
            CuriPlayer curiPlayer = (CuriPlayer) player;
            g.setColor(Color.green);
            g.drawString("" + curiPlayer.getPerception().getReward(), xp - 5, yp - 20);
        }
        double R = 15;
        double sin = player.sin(), cos = player.cos();
        x[0] = (int) (xp + cos * R);
        y[0] = (int) (yp + sin * R);
        x[1] = (int) (xp + sin * R / 2 - cos * R / 2);
        y[1] = (int) (yp - cos * R / 2 - sin * R / 2);
        x[2] = (int) (xp - sin * R / 2 - cos * R / 2);
        y[2] = (int) (yp + cos * R / 2 - sin * R / 2);
        draw3DTriangle(xp, yp, 0, 1, 2, player.getTeam().teamColor);
        draw3DTriangle(xp, yp, 1, 2, 0, player.getTeam().teamColor);
        draw3DTriangle(xp, yp, 2, 0, 1, player.getTeam().teamColor);
        RadialVehicle movCtrl = player.getVehicle();
        if (movCtrl.isKicking()) {
            g.setColor(Color.white);
            drawCircle(player.x, player.y, 10);
        }
        if (Facade.getSelectedPlayerId() == id) {
            drawCircle(player.x, player.y, SELECTION_RING_RADIUS);
            drawCircle(player.x, player.y, SELECTION_RING_RADIUS + 2);
        }
    }

    private void draw3DTriangle(int xp, int yp, int i0, int i1, int i2, int teamColor) {
        x2[0] = x[i0];
        y2[0] = y[i0];
        x2[1] = x[i1];
        y2[1] = y[i1];
        x2[2] = xp;
        y2[2] = yp;
        double R = dist(x2[0] - x2[1], y2[1] - y2[0]);
        double s = (x2[0] - x2[1] + y2[1] - y2[0]) / R / 2;
        g.setColor(this.colorTable[teamColor][(int) ((s + 1) * (SHADOW_LEVELS / 2))]);
        g.fillPolygon(x2, y2, 3);
    }

    private double dist(int i, int j) {
        return Math.sqrt(i * i + j * j);
    }


    public FieldGraphic(FrameField frameField) {
        this.frameField = frameField;
        if (Facade.getInstance().getUrl().length() > 0) {
            URL u = null;
            try {
                u = new URL(Facade.getInstance().getUrl() + SOCCER_FIELD_GIF);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            backgroundImage = Toolkit.getDefaultToolkit().getImage(u);
        } else {
            backgroundImage = Toolkit.getDefaultToolkit().getImage("qqSoccer/" + SOCCER_FIELD_GIF);
        }
        try {
            while (backgroundImage.getHeight(frameField) == -1) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e1) {
        }
        height = backgroundImage.getHeight(frameField);
        width = backgroundImage.getWidth(frameField);
        scaleX = (float) (width / FieldDimensions.OUTER_X / 2);
        scaleY = (float) (height / FieldDimensions.OUTER_Y / 2);
        backgroundImageTransparent = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        try {
            while (backgroundImageTransparent.getHeight(frameField) == -1) {
                Thread.sleep(100);
            }
            Thread.sleep(100);
            backgroundImageTransparent.getGraphics().drawImage(backgroundImage, 0, 0, null);
            Thread.sleep(100);
        } catch (InterruptedException e1) {
        }
        bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g = bi.getGraphics();
        makeColorTable();
    }

    private void makeColorTable() {
        this.colorTable = new Color[2][SHADOW_LEVELS];
        for (int teamColor = 0; teamColor < colorTable.length; teamColor++) {
            for (int j = 0; j < SHADOW_LEVELS; j++) {
                double s = (double) (j * 2 - SHADOW_LEVELS) / SHADOW_LEVELS;
                float c1 = 0;
                float c2 = 0;
                c1 = (float) Mat.lim(s + 1, 0.0, 1.0);
                c2 = (float) Mat.lim(s * s * s + 0.3, 0.0, 1.0);
                this.colorTable[teamColor][j] = new Color((teamColor == 0) ? c1 : c2, c2, (teamColor == 1) ? c1 : c2);
            }
        }
    }

    public boolean canDraw() {
        return Facade.match() != null && bi != null && backgroundImage.getHeight(frameField) > 0;
    }

    public int getWidth() {
        return bi.getWidth();
    }

    public int getHeight() {
        return bi.getHeight();
    }

    public static boolean isDrawBorderDist() {
        return drawBorderDist;
    }

    public static void setDrawBorderDist(boolean drawBorderDist) {
        FieldGraphic.drawBorderDist = drawBorderDist;
    }

    public static boolean isDrawVectorToBall() {
        return drawVectorToBall;
    }

    public static void setDrawVectorToBall(boolean drawVectorToBall) {
        FieldGraphic.drawVectorToBall = drawVectorToBall;
    }

    public static boolean isDrawPlayerVariables() {
        return drawPlayerVariables;
    }

    public static void setDrawPlayerVariables(boolean drawPlayerVariables) {
        FieldGraphic.drawPlayerVariables = drawPlayerVariables;
    }

    public static boolean isDrawTraces() {
        return drawTraces;
    }

    public static void setDrawTraces(boolean drawTraces) {
        FieldGraphic.drawTraces = drawTraces;
    }
}
