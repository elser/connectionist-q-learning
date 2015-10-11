package wanderbot;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Wanderbot extends Applet implements Runnable {
    private static final long serialVersionUID = 1L;
    protected Thread runner = null;
    private World world;
    public static boolean running = false;
    private Graphic graphic;

    public Wanderbot() {
        world = new World();
        graphic = new Graphic(this, world);
    }

    public void run() {
        Thread me = Thread.currentThread();
        while (runner == me) {
            try {
                Thread.sleep(10);
                threadAction();
            } catch (InterruptedException e) {
                stop();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void threadAction() {
        world.count();
        graphic.drawAll();
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
