package prj.shtelo.inminic.client.cameraobject;

import prj.shtelo.inminic.client.Root;
import prj.shtelo.inminic.client.rootobject.RootObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Character extends RootObject {
    private int hitboxX = 4, hitboxY = 16, hitboxWidth = 24, hitBoxHeight = 48;
    private int width = 32, height = 64;

    private int x, y;
    private String name;
    private Camera camera;
    private Root root;

    private BufferedImage[] images;

    private double offset;
    private int nowMode = 0;
    private int maxDelay, delay;
    private boolean watchingRight;

    public Character(int x, int y, String name, Camera camera, Root root) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.camera = camera;
        this.root = root;

        init();
    }

    private void init() {
        BufferedImage image;
        
        try {
            image = ImageIO.read(new File(".\\res\\character\\" + name + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        images = new BufferedImage[8];

        images[0] = cropImage(image, 0, 0);
        images[1] = cropImage(image, 32, 0);
        images[2] = cropImage(image, 64, 0);
        images[3] = cropImage(image, 96, 0);
        images[4] = cropImage(image, 0, 64);
        images[5] = cropImage(image, 32, 64);
        images[6] = cropImage(image, 64, 64);
        images[7] = cropImage(image, 96, 64);

        offset = root.getDisplay().getFps() / 12.;
        maxDelay = root.getDisplay().getFps() / 8;
        delay = 0;
        watchingRight = true;
    }

    @Override
    public void tick() {
        if (root.getKeyManager().getMove()[0]) {
            x -= offset;
            watchingRight = false;
        } if (root.getKeyManager().getMove()[1]) {
            x += offset;
            watchingRight = true;
        }

        boolean moving = root.getKeyManager().getMove()[0] || root.getKeyManager().getMove()[1];
        if (moving) {
            delay++;

            if (nowMode == 0) nowMode = 4;

            if (delay >= maxDelay) {
                if (nowMode == 4) nowMode = 5;
                else if (nowMode == 5) nowMode = 6;
                else if (nowMode == 6) nowMode = 7;
                else nowMode = 4;
                delay -= maxDelay;
            }
        } else {
            delay = 0;
            nowMode = 0;
        }
    }

    @Override
    public void render(Graphics graphics) {
        BufferedImage image = images[nowMode];
        int x = (int) ((this.x - camera.getX()) * camera.getZoom() + root.getDisplay().getWidth() / 2);
        int y = (int) ((this.y - camera.getY()) * camera.getZoom() + root.getDisplay().getHeight() / 2);
        int width = (int) (this.width * camera.getZoom());
        int height = (int) (this.height * camera.getZoom());

        if (watchingRight)
            graphics.drawImage(image, x, y, width, height, null);
        else
            graphics.drawImage(image, x + width, y, -width, height, null);
    }

    private BufferedImage cropImage(BufferedImage src, int x, int y) {
        return src.getSubimage(x, y, 32, 64);
    }
}
