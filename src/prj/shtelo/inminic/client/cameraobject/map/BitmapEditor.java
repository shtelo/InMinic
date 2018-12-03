package prj.shtelo.inminic.client.cameraobject.map;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BitmapEditor {
    private static String PATH = ".\\res\\map\\";
    private static int RGB_BLACK = -16777216;

    private int width, height;

    public Pixel[][] load() {
        BufferedImage colorImg;
        BufferedImage collisionImg;

        try {
            colorImg = ImageIO.read(new File(PATH + "color.png"));
            collisionImg = ImageIO.read(new File(PATH + "collision.png"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        int width = colorImg.getWidth();
        int height = colorImg.getHeight();
        this.width = width;
        this.height = height;

        Pixel[][] pixels = new Pixel[height][width];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixelColor = colorImg.getRGB(x, y);
                int red = (pixelColor & 0x00ff0000) >> 16;
                int green = (pixelColor & 0x0000ff00) >> 8;
                int blue = pixelColor & 0x000000ff;
                boolean collision = collisionImg.getRGB(x, y) == RGB_BLACK;
                pixels[y][x] = new Pixel(new Color(red, green, blue), collision);
            }
        }
        return pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
