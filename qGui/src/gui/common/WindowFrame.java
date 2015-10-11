/**
 *
 */
package gui.common;

import javax.swing.*;

/**
 * @author dkapusta
 */
public class WindowFrame extends JInternalFrame {
    private static final long serialVersionUID = -5772961601439687215L;

    /**
     * @param title
     * @param resizable
     * @param closable
     * @param maximizable
     * @param iconifiable
     */
    public WindowFrame(String title, boolean resizable, boolean closable,
                       boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }

    /**
     * @param title
     * @param resizable
     */
    public WindowFrame(String title, boolean resizable) {
        super(title, resizable);
    }

    public void placeOnTheRightOf(JInternalFrame frame) {
        this.setLocation(frame.getX() + frame.getWidth(), frame.getY());
    }

    public void placeAtTheBottomOf(JInternalFrame frame) {
        this.setLocation(frame.getX(), frame.getY() + frame.getHeight());
    }

}
