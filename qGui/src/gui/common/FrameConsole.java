package gui.common;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * This is a subclass of JInternalFrame which displays a system console.
 *
 * @author Elser
 */
public class FrameConsole extends JInternalFrame {
    private static final long serialVersionUID = 1L;

    public FrameConsole() {
        super("System console", true, true, true, true);
        JPanel top = new JPanel();
        top.setBorder(new EmptyBorder(10, 10, 10, 10));
        top.setLayout(new BorderLayout());

        final JTextArea content = new JTextArea(15, 30);
        content.setBorder(new EmptyBorder(0, 5, 0, 5));
        content.setLineWrap(true);


        final JScrollPane textScroller = new JScrollPane(content,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        top.add(textScroller, BorderLayout.CENTER);


        setContentPane(top);
        pack();/*
        System.setOut(new PrintStream(new OutputStream() {
			private void write(int arg0) throws IOException {
			}
		}) {
			private void print(String arg0) {
				content.append(arg0);
			}

			private void println(String arg0) {
				content.append(arg0 + "\n");
				//textScroller.getVerticalScrollBar().setValue(textScroller.getVerticalScrollBar().getMaximum());
			}
		});*/
        System.out.println("Console started");
        setSize(500, 200);
        setLocation(50, 400);
    }
}
