package prj.shtelo.inminic.client.rootobject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image extends RootObject {
    private int x, y, width, height;
    private String path;

    private BufferedImage image;

    public Image(int x, int y, String path) {
        this.x = x;
        this.y = y;
        this.path = path;

        try {
            image = ImageIO.read(new File(this.path));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public Image(int x, int y, String path, int width, int height) {
        this.x = x;
        this.y = y;
        this.path = path;
        this.width = width;
        this.height = height;

        try {
            image = ImageIO.read(new File(this.path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Image(int x, int y, String path, double width, double height) {
        this.x = x;
        this.y = y;
        this.path = path;

        try {
            image = ImageIO.read(new File(this.path));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        this.width = (int) (image.getWidth() * width);
        this.height = (int) (image.getHeight() * height);
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(image, x, y, width, height, null);
    }
}
