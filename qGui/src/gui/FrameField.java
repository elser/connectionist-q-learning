package gui;

import config.Options;
import cosmos.FieldDimensions;
import cosmos.PosXY;
import facade.Facade;
import gui.common.AbstractImageFrame;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;

public class FrameField extends AbstractImageFrame implements KeyListener {
    private static final long serialVersionUID = 1L;
    private static FieldGraphic fieldGraphic;

    public FrameField() {
        super("Soccer field", false, true, false, false);
        fieldGraphic = new FieldGraphic(this);
        setSize(fieldGraphic.getWidth(), fieldGraphic.getHeight());
        this.addKeyListener(this);
        super.start();
        try {
            setSelected(true);
        } catch (PropertyVetoException e1) {
            e1.printStackTrace();
        }
    }

    protected void threadAction() {
        if (fieldGraphic != null && fieldGraphic.canDraw() && getGraphics() != null) {
            drawImage(fieldGraphic.toImage());
        }
    }

    protected long getSleepMillis() {
        return Options.getInstance().getFieldDispRate();
    }

    private void imageClicked(PosXY clickedHere) {
        double maxDist = 20;
        double dist;
        Facade.setSelectedPlayerId(-1);
        for (int i = 0; i < Facade.match().players.length; i++) {
            dist = clickedHere.distTo(Facade.match().players[i]);
            if (maxDist > dist) {
                maxDist = dist;
                Facade.setSelectedPlayerId(i);
            }
        }
    }

    protected void imageMouseDragged(int x, int y, int button) {
        if (fieldGraphic == null) {
            return;
        }
        PosXY clickedHere = new PosXY(x / fieldGraphic.scaleX - FieldDimensions.OUTER_X,
                y / fieldGraphic.scaleY - FieldDimensions.OUTER_Y);
        if ((button & InputEvent.BUTTON1_MASK) > 0) {
            if (Facade.getSelectedPlayer() != null) {
                Facade.getSelectedPlayer().setXY(clickedHere);
            }
        }
    }

    /**
     * @param button
     * @param i
     * @param j
     */
    protected void imageClicked(int x, int y, int button) {
        if (fieldGraphic == null) {
            return;
        }
        PosXY clickedHere = new PosXY(x / fieldGraphic.scaleX - FieldDimensions.OUTER_X, y / fieldGraphic.scaleY - FieldDimensions.OUTER_Y);
        if (button == MouseEvent.BUTTON1) { //left-click
            imageClicked(clickedHere);
        } else if (button == MouseEvent.BUTTON3) { //right-click
            clickedHere.seth(20);
            Facade.match().getBall().setXY(clickedHere);
        }
    }

    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    protected void imageMouseMoved(int x, int y) {
    }

    protected void draw() {
    }
}
