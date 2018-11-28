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

        frame = new JFrame();
        frame.setTitle(title);
        frame.setSize(width + 16, height + 39);  // 아직 이 문제에 대한 깔끔한 해결법이 발견되지 않음.
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
