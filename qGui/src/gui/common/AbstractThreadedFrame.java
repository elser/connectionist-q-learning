package gui.common;


public abstract class AbstractThreadedFrame extends WindowFrame implements Runnable {
    private static final long serialVersionUID = 1L;
    protected Thread runner = null;
    public static boolean running = false;

    protected abstract long getSleepMillis();

    protected abstract void threadAction();

    public AbstractThreadedFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }

    public void run() {
        Thread me = Thread.currentThread();
        while (runner == me) {
            try {
                Thread.sleep(getSleepMillis());
                if (running) {
                    threadAction();
                }
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
}
