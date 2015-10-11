package gui.chart;

import config.Options;
import facade.Facade;
import game.CuriPlayer;
import gui.common.AbstractImageFrame;

import java.awt.*;

public class FrameTimelineChart extends AbstractImageFrame {
    private static final long serialVersionUID = 1L;

    private int lastAvgRewardY;

    private int lastAvgRewardY2;

    public FrameTimelineChart() {
        super("Team energies", true, true, false, false);
        setSize(400, 100);
        iWasResized();
        start();
    }

    protected long getSleepMillis() {
        return Options.getInstance().getListDispRate();
    }

    protected void draw() throws NullPointerException {
        //Match match = Facade.getInstance().getScenario().match;
        CuriPlayer player = (CuriPlayer) Facade.getSelectedPlayer();
        if (player != null) {
            g.drawImage(bi, -1, 0, null);
            g.setColor(Color.white);
            g.fillRect(0, 0, 100, 20);
            g.setColor(Color.black);
            g.drawString("Average reward:", 2, 10);
            int avgY = getChartY(player.getPerception().getReward());
            g.drawLine(width - 2, lastAvgRewardY, width - 2, avgY);
            lastAvgRewardY = avgY;
            g.setColor(Color.red);
            g.drawString("Novelty:", 2, 20);
            int avgY2 = getChartY(player.getPerception().getNovelty() * 2);
            g.drawLine(width - 2, lastAvgRewardY2, width - 2, avgY2);
            lastAvgRewardY2 = avgY2;
        }
    }

    private int getChartY(double avgReward) {
        return (int) (height / 2 - avgReward * 5 * height / 2);
    }

    protected void imageClicked(int x, int y, int button) {
    }

    protected void imageMouseDragged(int x, int y, int button) {
    }

    protected void imageMouseMoved(int x, int y) {

    }
}
