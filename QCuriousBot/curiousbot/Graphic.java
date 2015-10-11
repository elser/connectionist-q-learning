package curiousbot;

import curiousbot.brain.MyPerception;
import pl.gdan.elsy.qconf.curiosity.Curiosity;

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
    private int lastAvgRewardY2;


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
        if (world.getTime() % 4 == 0) {
            gChart.drawImage(biChart, -1, 0, null);
            gChart.setColor(Color.white);
            gChart.fillRect(0, 0, 100, 20);
            gChart.setColor(Color.black);
            gChart.drawString("Average reward:", 2, 10);
            Player player = world.getPlayer();
            int avgY = getChartY(player.getReward());
            gChart.drawLine(width - 2, lastAvgRewardY, width - 2, avgY);
            lastAvgRewardY = avgY;
            gChart.setColor(Color.red);
            int avgY2 = getChartY(player.getNovelty() * 2);
            gChart.drawLine(width - 2, lastAvgRewardY2, width - 2, avgY2);
            lastAvgRewardY2 = avgY2;
        }
    }

    private int getChartY(double avgReward) {
        return (int) (CHART_HEIGHT / 2 - avgReward * 5 * CHART_HEIGHT / 2);
    }

    /**
     * Draws player on the Graphics g in the position given by parameters.
     *
     * @param id
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
        drawCuriosity(player.getPerception().getCuriosity());
    }

    private void drawCuriosity(Curiosity curiosity) {
        int ySize = 10;
        g.setColor(Color.blue);
        drawArray("Curiosity NN input", curiosity.getInputBkp(), 100, 0, 5, ySize);
        g.setColor(Color.green);
        drawArray("Curiosity NN desired output", curiosity.getDesOutputBkp(), 100, 40, 5, ySize);
        g.setColor(Color.red);
        drawArray("Curiosity NN actual output", curiosity.getOutputBkp(), 100, 80, 5, ySize);
    }

    private void drawArray(String title, double[] output, int x0, int y0, int xSize, int ySize) {
        g.drawString(title, x0, y0 + ySize + 15);
        for (int i = 0; i < output.length; i++) {
            double o = output[i];
            int x = i * xSize + x0;
            if (o > 0) {
                g.fillRect(x, (int) (y0 + ySize - o * ySize * 2) + 5, xSize - 1, (int) (o * ySize));
            } else {
                g.fillRect(x, (y0 + ySize), xSize - 1, (int) (-o * ySize));
            }
        }
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
                g.fillRect(i * 10, (100), 9, (int) (-o * 100));
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
        int i = 1;
        for (int d = MyPerception.RADAR_D0; d <= MyPerception.RADAR_DISTS; d++) {
            for (int a = -MyPerception.RADAR_ANGLES; a <= MyPerception.RADAR_ANGLES; a++) {
                MyPerception perc = player.getPerception();
                xp = scaledX(perc.xPerc(d, a));
                yp = scaledY(perc.yPerc(d, a));
                double out = perc.getOutput()[i++];
                g.setColor(out > 0.5 ? Color.red : Color.blue);
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
