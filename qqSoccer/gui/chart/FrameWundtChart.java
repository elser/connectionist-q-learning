package gui.chart;

import config.Options;
import facade.Facade;
import game.CuriPlayer;
import gui.common.AbstractImageFrame;
import module.brain.perception.MyPerception;

import java.awt.*;

public class FrameWundtChart extends AbstractImageFrame {
    private static final long serialVersionUID = 1L;

    public FrameWundtChart() {
        super("Wundt Chart", true, true, false, false);
        setSize(200, 200);
        start();
    }

    protected long getSleepMillis() {
        return Options.getInstance().getFieldDispRate();
    }

    protected void draw() throws NullPointerException {
        //Match match = Facade.getInstance().getScenario().match;
        CuriPlayer player = (CuriPlayer) Facade.getSelectedPlayer();
        if (player != null) {
            MyPerception perc = player.getPerception();
            clear(Color.white);
            g.setColor(Color.gray);
            int y0 = chartToY(0);
            g.drawLine(0, y0, width, y0);
            g.setColor(Color.black);
            int lastY = 0;
            for (int x = 0; x < width; x++) {
                double wc = perc.wundtCurve(xToChart(x));
                int y = chartToY(wc);
                if (x > 0) {
                    g.drawLine(x - 1, lastY, x, y);
                }
                lastY = y;
            }
            g.setColor(Color.red);
            double n = perc.getNovelty();
            double wc = perc.wundtCurve(n);
            int yGr = chartToY(wc);
            int xGr = chartToX(n);
            g.drawLine(0, yGr, xGr, yGr);
            g.drawLine(xGr, yGr, xGr, height);
        }
    }

    private double getXScale() {
        return width * 20;
    }

    private int chartToX(double n) {
        return (int) (n * getXScale());
    }

    private int chartToY(double wc) {
        return (int) -(wc * height) + height / 2;
    }

    private double xToChart(int x) {
        return x / getXScale();
    }

    protected void imageClicked(int x, int y, int button) {
    }

    protected void imageMouseDragged(int x, int y, int button) {
    }

    protected void imageMouseMoved(int x, int y) {

    }
}
