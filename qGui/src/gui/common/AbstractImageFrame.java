package gui.common;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;


public abstract class AbstractImageFrame extends AbstractThreadedFrame implements ComponentListener {
    private static final long serialVersionUID = 1L;
    protected static final int X_MOUSE_OFFSET = 6;
    protected static final int Y_MOUSE_OFFSET = 28;

    protected Graphics g;
    protected BufferedImage bi;
    public int height;
    public int width;

    protected abstract void imageClicked(int x, int y, int button);

    protected abstract void imageMouseDragged(int x, int y, int button);

    protected abstract void imageMouseMoved(int x, int y);

    public AbstractImageFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
        this.addComponentListener(this);
        this.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                imageMouseDragged(e.getPoint().x - X_MOUSE_OFFSET, e.getPoint().y - Y_MOUSE_OFFSET, e.getModifiers());
            }

            public void mouseMoved(MouseEvent e) {
                imageMouseMoved(e.getPoint().x - X_MOUSE_OFFSET, e.getPoint().y - Y_MOUSE_OFFSET);
            }
        });
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                imageClicked(e.getPoint().x - X_MOUSE_OFFSET, e.getPoint().y - Y_MOUSE_OFFSET, e.getButton());
            }
        });
    }


    protected void threadAction() throws NullPointerException {
        if (bi != null) {
            draw();
            repaintMe();
        } else {
            iWasResized();
        }
    }

    protected abstract void draw();

    /**
     * @param bgColor
     */
    protected void clear(Color bgColor) {
        g.setColor(bgColor);
        g.fillRect(0, 0, width, height);
    }

    protected void clear() {
        clear(Color.white);
    }

    public void setSize(int width, int height) {
        super.setSize(width + 10, height + 33);
    }

    protected void drawImage(Image image) {
        if (getContentPane().getGraphics() != null) {
            getContentPane().getGraphics().drawImage(image, 0, 0, null);
        }
    }

    protected void repaintMe() {
        drawImage(bi);
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        iWasResized();
    }

    public void componentShown(ComponentEvent e) {
    }

    protected void iWasResized() {
        if (getContentPane().getWidth() + getContentPane().getHeight() > 0) {
            width = getContentPane().getWidth();
            height = getContentPane().getHeight();
            bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            g = bi.getGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, width, height);
        }
    }

    protected void advanceChart(int speed) {
        g.drawImage(bi, -speed, 0, Color.white, null);

    }

}
