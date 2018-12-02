package prj.shtelo.inminic.client.cameraobject;

import prj.shtelo.inminic.client.Root;
import prj.shtelo.inminic.client.root.Color;
import prj.shtelo.inminic.client.root.TextFormat;
import prj.shtelo.inminic.client.rootobject.RootObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends RootObject {
    private final TextFormat TEXT_FORMAT = new TextFormat(".\\res\\font\\D2Coding.ttc", 18, Color.text);
    private int width = 32, height = 64;

    private double x, y;
    private String name;
    private Camera camera;
    private Root root;

    private BufferedImage[] images;

    private int form = 0;
    private boolean watchingRight;

    public Player(double x, double y, String name, Camera camera, Root root) {
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

        watchingRight = true;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics graphics) {
        BufferedImage image = images[form];
        int width = (int) (this.width * camera.getZoom());
        int height = (int) (this.height * camera.getZoom());
        int x = (int) ((this.x - camera.getX()) * camera.getZoom() + (root.getDisplay().getWidth() - width) / 2);
        int y = (int) ((this.y - camera.getY()) * camera.getZoom() + (root.getDisplay().getHeight() - height) / 2);

        if (watchingRight)
            graphics.drawImage(image, x, y, width, height, null);
        else
            graphics.drawImage(image, x + width, y, -width, height, null);
        graphics.setFont(TEXT_FORMAT.getFont());
        graphics.drawString(name, (width - graphics.getFontMetrics().stringWidth(name)) / 2 + x, (int) (y - TEXT_FORMAT.getSize()));
    }

    private BufferedImage cropImage(BufferedImage src, int x, int y) {
        return src.getSubimage(x, y, 32, 64);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWatchingRight(boolean watchingRight) {
        this.watchingRight = watchingRight;
    }

    public void setForm(int form) {
        this.form = form;
    }

    public String getName() {
        return name;
    }
}
