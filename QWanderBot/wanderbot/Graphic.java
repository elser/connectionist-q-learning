package wanderbot;

import wanderbot.brain.MyPerception;

import java.applet.Applet;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Graphic {
    public static final int CHART_HEIGHT = 100;
    public static final int WINDOW_W = World.SIZE * 2;
    public static final int WINDOW_H = World.SIZE * 2 + CHART_HEIGHT;
    static int[] x = new int[4];
    static int[] y = new int[4];
    static int[] x2 = new int[4];
    static int[] y2 = new int[4];
    private World world;
    private Graphics g;
    private Applet applet;
    private int width = -1;
    private int height = -1;
    private BufferedImage bi;
    private BufferedImage biChart;
    private Graphics gChart;
    private int lastAvgRewardY;


    public Graphic(Applet applet, World world) {
        this.g = applet.getGraphics();
        this.applet = applet;
        this.world = world;
    }

    public void drawAll() {
        if (applet.getWidth() > 0) {
            if (width == -1) {
                makeBufferedImage();
            }
            clear();
            drawObstacles();
            drawPlayer();
            drawChart();
            Graphics graphics = applet.getGraphics();
            if (graphics != null) {
                graphics.drawImage(bi, 0, 0, null);
                graphics.drawImage(biChart, 0, bi.getHeight(), null);
            }
        }
    }

    private void drawChart() {
        if (world.getTime() % 30 == 0) {
            gChart.drawImage(biChart, -1, 0, null);
            gChart.setColor(Color.white);
            gChart.fillRect(0, 0, 100, 20);
            gChart.setColor(Color.black);
            gChart.drawString("Average reward:", 2, 10);
            Player player = world.getPlayer();
            double avgReward = player.getAvgReward();
            int avgY = getChartY(avgReward);
            gChart.drawLine(width - 2, lastAvgRewardY, width - 2, avgY);
            lastAvgRewardY = avgY;
        }
    }

    private int getChartY(double avgReward) {
        return (int) (CHART_HEIGHT / 2 - avgReward * 5 * CHART_HEIGHT / 2);
    }

    /**
     * Draws player on the Graphics g in the position given by parameters.
     */
    private void drawPlayer() {
        Player player = world.getPlayer();
        int xp = scaledX(player.x);
        int yp = scaledY(player.y);
        double R = player.r;
        double sin = Math.sin(player.angle), cos = Math.cos(player.angle);
        x[0] = (int) (xp + cos * R);
        y[0] = (int) (yp + sin * R);
        x[1] = (int) (xp + sin * R / 2 - cos * R / 2);
        y[1] = (int) (yp - cos * R / 2 - sin * R / 2);
        x[2] = (int) (xp - sin * R / 2 - cos * R / 2);
        y[2] = (int) (yp + cos * R / 2 - sin * R / 2);
        g.setColor(Color.red);
        g.fillPolygon(x, y, 3);
        drawRadar(player);
        drawOutput(player);
    }

    private void drawOutput(Player player) {
        double[] output = player.getBrain().getOutput();
        int i;
        g.drawString("Brain output:", 2, 10);
        g.drawString("1 - Move forward", 2, 20);
        g.drawString("2 - Turn left", 2, 30);
        g.drawString("3 - Turn right", 2, 40);
        for (i = 0; i < output.length; i++) {
            g.setColor(Color.blue);
            double o = output[i];
            if (o > 0) {
                g.fillRect(i * 10, (int) (100 - o * 100), 9, (int) (o * 100));
            } else {
                g.fillRect(i * 10, (int) (100), 9, (int) (-o * 100));
            }
            g.setColor(Color.yellow);
            g.drawString("" + (i + 1), i * 10, 98);
        }
        MyPerception perception = player.getPerception();
        double reward = perception.getReward();
        g.fillRect(i * 10, (int) (100 - reward * 100), 10, (int) (reward * 100));
    }

    private void drawRadar(Player player) {
        int xp;
        int yp;
        g.setColor(Color.blue);
        for (int d = 1; d <= 3; d++) {
            for (int a = -1; a <= 1; a++) {
                xp = scaledX(player.getPerception().xPerc(d, a));
                yp = scaledY(player.getPerception().yPerc(d, a));
                g.drawOval(xp - 1, yp - 1, 3, 3);
            }
        }
    }

    private void makeBufferedImage() {
        width = World.SIZE * 2;
        height = World.SIZE * 2;
        applet.setSize(WINDOW_W, WINDOW_H);
        bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        biChart = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g = bi.getGraphics();
        gChart = biChart.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        gChart.setColor(Color.white);
        gChart.fillRect(0, 0, width, CHART_HEIGHT);
        gChart.setColor(Color.lightGray);
        gChart.drawLine(0, CHART_HEIGHT / 2, width, CHART_HEIGHT / 2);
        gChart.setColor(Color.cyan);
        gChart.drawLine(0, getChartY(0.1), width, getChartY(0.1));
        lastAvgRewardY = CHART_HEIGHT / 2;
    }

    private void drawObstacles() {
        Obstacle[] obstacles = world.getObstacles();
        for (int i = 0; i < obstacles.length; i++) {
            g.setColor(obstacles[i].c);
            int r = (int) obstacles[i].r;
            g.fillOval(
                    scaledX(obstacles[i].x - r),
                    scaledY(obstacles[i].y - r),
                    r * 2,
                    r * 2);
        }
    }

    private void clear() {
        g.setColor(Color.white);
        g.fillRect(0, 0, applet.getWidth(), applet.getHeight());
    }

    private int scaledX(double x) {
        return (int) (x + width / 2);
    }

    private int scaledY(double y) {
        return (int) (y + height / 2);
    }
}
