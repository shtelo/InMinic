package prj.shtelo.inminic.client.rootobject;

import prj.shtelo.inminic.client.Root;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Character extends RootObject {
    private int hitboxX = 4, hitboxY = 16, hitboxWidth = 24, hitBoxHeight = 48;

    private int x, y;
    private String name;
    private Root root;

    private double offset = 5;

    private BufferedImage[] images;

    private int nowMode = 0;

    public Character(int x, int y, String name, Root root) {
        this.x = x;
        this.y = y;
        this.name = name;
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
    }

    @Override
    public void tick() {
        if (root.getKeyManager().getMove()[1]) x -= offset;
        if (root.getKeyManager().getMove()[3]) x += offset;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(images[nowMode], x, y, null);
    }

    private BufferedImage cropImage(BufferedImage src, int x, int y) {
        return src.getSubimage(x, y, 32, 64);
    }
}
