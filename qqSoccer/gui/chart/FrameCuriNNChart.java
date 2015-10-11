package gui.chart;

import config.Options;
import facade.Facade;
import game.CuriPlayer;
import gui.common.AbstractImageFrame;
import pl.gdan.elsy.qconf.curiosity.Curiosity;
import pl.gdan.elsy.qconf.curiosity.CuriousBrain;

import java.awt.*;

public class FrameCuriNNChart extends AbstractImageFrame {
    private static final long serialVersionUID = 1L;

    public FrameCuriNNChart() {
        super("Brain Signals", true, true, false, false);
        setSize(400, 800);
        start();
    }

    protected long getSleepMillis() {
        return Options.getInstance().getFieldDispRate();
    }

    protected void draw() throws NullPointerException {
        //Match match = Facade.getInstance().getScenario().match;
        CuriPlayer player = (CuriPlayer) Facade.getSelectedPlayer();
        if (player != null) {
            final CuriousBrain brain = player.getBrain();
            Curiosity curiosity = brain.getCuriosity();
            int ySize = 10;
            int yStep = 40;
            int step = 0;
            clear(Color.white);
            int xSize = 5;
            drawArray("Curiosity NN input", Color.blue, curiosity.getInputBkp(), 0, (step++) * yStep, xSize, ySize);
            drawArray("Curiosity NN desired output", Color.green, curiosity.getDesOutputBkp(), 0, (step++) * yStep, xSize, ySize);
            drawArray("Curiosity NN actual output", Color.red, curiosity.getOutputBkp(), 0, (step++) * yStep, xSize, ySize);
            drawArray("QLearning net output", Color.black, brain.getOutput(), 0, (step++) * yStep, xSize, ySize);

            step = 0;
            yStep = 40;
            for (double[][] ww : brain.getW()) {
                for (double[] www : ww) {
                    drawNet(Color.red, www, 200, (step++) * yStep, xSize, ySize, 1);
                }
            }
            step = 0;
            for (double[][] ww : brain.getE()) {
                for (double[] www : ww) {
                    drawNet(Color.magenta, www, 300, (step++) * yStep, xSize, ySize, 100000);
                }
            }
            step = 5;
            yStep = 40;
            for (double[] ww : brain.getActivation()) {
                    drawNet(Color.blue, ww, 50, (step++) * yStep, xSize, ySize, 1);
            }

        }
    }

    private void drawNet(Color color, double[] output, int x0, int y0, int xSize, int ySize, int scale) {
        int y1 = y0 + ySize * 2;
        g.setColor(Color.gray);
        g.drawLine(x0, y1, x0 + output.length * xSize, y1);
        g.drawLine(x0, y1 - ySize * 2, x0 + output.length * xSize, y1 - ySize * 2);
        g.setColor(color);
        for (int i = 0; i < output.length; i++) {
            double o = output[i] * scale;
            int x = x0 + i * xSize;
            final int y = (int) (y1 - o * ySize * 2) + 1;
            g.drawLine(x, y, x + xSize - 1, y);
        }
    }

    private void drawArray(String title, Color color, double[] output, int x0, int y0, int xSize, int ySize) {
        int y1 = y0 + ySize * 2;
        g.setColor(Color.black);
        g.drawString(title, x0, y1 + 15);
        g.setColor(Color.gray);
//        g.drawLine(x0, y1, x0 + output.length * xSize, y1);
//        g.drawLine(x0, y1 - ySize * 2, x0 + output.length * xSize, y1 - ySize * 2);
        g.setColor(color);
        for (int i = 0; i < output.length; i++) {
            double o = output[i];
            int x = x0 + i * xSize;
            final int y = (int) (y1 - o * ySize * 2) + 1;
            g.drawLine(x, y, x + xSize - 1, y);
//            if(o>0) {
////                g.fillRect(x, (int)(y1 - o*ySize*2)+1, xSize-1, (int)(o*ySize));
//            } else {
//                g.fillRect(x, (y1)+1, xSize-1, (int)(-o*ySize));
//            }
        }
    }

    protected void imageClicked(int x, int y, int button) {
    }

    protected void imageMouseDragged(int x, int y, int button) {
    }

    protected void imageMouseMoved(int x, int y) {

    }
}
