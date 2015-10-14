package wanderbot;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Wanderbot extends Applet implements Runnable {
    private static final long serialVersionUID = 1L;
    protected static final int SPEEDS = 3;
    private int speed = 1;
    protected Thread runner = null;
    private World world;
    public static boolean running = false;
    private Graphic graphic;

    public Wanderbot() {
        world = new World();
        graphic = new Graphic(this, world);
        addMouseListener(new MouseListener() {
            public void mouseExited(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseClicked(MouseEvent e) {
                speed = (speed + 1) % SPEEDS;
            }
        });
    }

    public void run() {
        Thread me = Thread.currentThread();
        for (int i = 0; i < 10000; i++) {
            world.count();
        }
        while (runner == me) {
            try {
                switch (speed) {
                    case 0:
                        Thread.sleep(500);
                        world.count();
                        break;
                    case 1:
                        Thread.sleep(2);
                        world.count();
                        break;
                    case 2:
                        for (int i = 0; i < 1000; i++) {
                            world.count();
                        }
                        break;

                    default:
                        break;
                }
                graphic.drawAll();
            } catch (InterruptedException e) {
                stop();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        if (runner != null) {
            new Exception("Double start invocation").printStackTrace();
        }
        runner = new Thread(this);
        runner.start();
    }

    public void stop() {
        runner = null;
    }

    public static void main(String[] args) {
        final Wanderbot app = new Wanderbot();
        Frame frame = new Frame("Wanderbot");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                app.stop();
                app.destroy();
                System.exit(0);
            }
        });
        frame.add(app, BorderLayout.CENTER);
        frame.setSize(Graphic.WINDOW_W + 8, Graphic.WINDOW_H + 30);
        app.init();
        app.start();
        frame.setVisible(true);
    }
}
