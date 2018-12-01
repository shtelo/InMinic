package prj.shtelo.inminic.client.root;

import prj.shtelo.inminic.client.Root;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Display {
    private final static double motionSpeed = 10;

    private String title;
    private int width, height, fps;
    private Root root;

    private double displayFps;

    private Dimension size;
    private JFrame frame;
    private Canvas canvas;

    private String os = System.getProperty("os.name");

    public Display(String title, int width, int height, int fps, Root root) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.root = root;

        this.displayFps = fps;

        init();
    }

    private void init() {
        size = new Dimension(width, height);

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
        frame.addKeyListener(root.getKeyManager());
        frame.setLocationRelativeTo(null);

        canvas = new Canvas();
        canvas.setPreferredSize(size);
        canvas.setMinimumSize(size);
        canvas.setMaximumSize(size);
        canvas.setFocusable(false);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setSize(e.getComponent().getSize());
            }
        });
        frame.add(canvas);

        frame.setVisible(true);
    }

    private void setSize(int width, int height) {
        size = new Dimension(width, height);

        setSize(size);
    }

    private void setSize(Dimension size) {
        width = (int) size.getWidth();
        height = (int) size.getHeight();

        if (os.equalsIgnoreCase("Windows 8.1")) {
            size = new Dimension(width - 16, height - 39);
        } else if (os.equalsIgnoreCase("Mac OS X")) {
            size = new Dimension(width, height - 22);
        }

        canvas.setPreferredSize(size);
        canvas.setMinimumSize(size);
        canvas.setMaximumSize(size);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFps() {
        return fps;
    }

    public double getDisplayFps() {
        return displayFps;
    }

    public static double getMotionSpeed() {
        return motionSpeed;
    }

    public void setDisplayFps(double displayFps) {
        this.displayFps = displayFps;
    }
}
