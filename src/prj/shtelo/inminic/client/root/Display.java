package prj.shtelo.inminic.client.root;

import javax.swing.*;
import java.awt.*;

public class Display {
    private String title;
    private int width, height;
    private JFrame frame;
    private Canvas canvas;

    public Display(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;

        init();
    }

    private void init() {
        Dimension size = new Dimension(width, height);

        String os = System.getProperty("os.name");

        frame = new JFrame();
        frame.setTitle(title);
        if (os.equalsIgnoreCase("Windows 8.1")) {
            frame.setSize(width + 16, height + 39);
        } else if (os.equalsIgnoreCase("Mac OS X")) {
            frame.setSize(width, height + 22);
        } else {
            frame.setSize(size);
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        canvas = new Canvas();
        canvas.setPreferredSize(size);
        canvas.setMinimumSize(size);
        canvas.setMaximumSize(size);
        frame.add(canvas);

        frame.setVisible(true);
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
