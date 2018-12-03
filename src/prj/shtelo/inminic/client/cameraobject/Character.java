package prj.shtelo.inminic.client.cameraobject;

import prj.shtelo.inminic.client.Root;
import prj.shtelo.inminic.client.cameraobject.map.Pixel;
import prj.shtelo.inminic.client.root.Color;
import prj.shtelo.inminic.client.root.TextFormat;
import prj.shtelo.inminic.client.rootobject.RootObject;
import prj.shtelo.inminic.client.rootobject.Text;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Character extends RootObject {
    private final static int collisionBoxX = 4, collisionBoxY = 16, collisionBoxWidth = 24, collisionBoxHeight = 48;
    private final TextFormat TEXT_FORMAT = new TextFormat(".\\res\\font\\D2Coding.ttc", 18, Color.text);
    private int width = 32, height = 64;

    private double collisionBoxStartX;
    private double collisionBoxStartY;

    private double x, y;
    private String name;
    private Camera camera;
    private Map map;
    private Root root;

    private BufferedImage[] images;

    private int form = 0;
    private int delay;
    private boolean watchingRight;

    private double velocity;

    private double previousX, previousY;

    public Character(double x, double y, String name, Camera camera, Map map, Root root) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.camera = camera;
        this.map = map;
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

        delay = 0;
        watchingRight = true;

        if (root.getClient().getConnected())
            root.getClient().send("playerName\t" + name);
    }

    @Override
    public void tick() {
         collisionBoxStartX = x - width / 2. + collisionBoxX;
         collisionBoxStartY = y - height / 2. + collisionBoxY;

        double offset = 60. / root.getDisplay().getDisplayFps();
        int maxDelay = (int) (root.getDisplay().getDisplayFps() / 8);

        if (root.getKeyManager().getMove()[0]) {
            ArrayList<int[]> collisions = getLeftCollision();
            if (collisions.isEmpty()) {
                x -= offset;
            } else if (collisions.get(0)[1] == (int) (collisionBoxStartY + collisionBoxHeight - 1)) {
                x -= 1;
                y -= 1;
            }
            watchingRight = false;
        } if (root.getKeyManager().getMove()[1]) {
            ArrayList<int[]> collisions = getRightCollision();
            if (collisions.isEmpty()) {
                x += offset;
            } else if (collisions.get(0)[1] == (int) (collisionBoxStartY + collisionBoxHeight - 1)) {
                x += 1;
                y -= 1;
            }
            watchingRight = true;
        }

        boolean moving = root.getKeyManager().getMove()[0] || root.getKeyManager().getMove()[1];
        if (moving) {
            delay++;

            if (form == 0) form = 4;

            if (delay >= maxDelay) {
                if (form == 4) form = 5;
                else if (form == 5) form = 6;
                else if (form == 6) form = 7;
                else form = 4;
                delay -= maxDelay;
            }
        } else {
            delay = 0;
            form = 0;
        }

        try {
            gravityAction();
        } catch (NullPointerException ignored) {}

        if (root.getClient().getConnected())
            if (x != previousX || y != previousY || root.getKeyManager().isMoveStop())
                root.getClient().send("move\t" + toString());
        previousX = x;
        previousY = y;
    }

    private void gravityAction() {
        if (getDeltaY() > 0) {
            velocity += 1411.2 / root.getDisplay().getDisplayFps() / root.getDisplay().getDisplayFps();
            if (velocity > getDeltaY()) {
                velocity = getDeltaY();
            }
        } else {
            velocity = 0;
        }

        if (root.getKeyManager().getKeys()[KeyEvent.VK_SPACE] && getDeltaY() == 0) {
            velocity -= 300 / root.getDisplay().getDisplayFps();
        }

        y += velocity;
    }

    private int getDeltaY() {
        int offsetY = 0;
        for (int y = (int) (collisionBoxStartY + collisionBoxHeight); y + offsetY < map.getMapManager().getHeight(); offsetY++) {
            for (int x = (int) collisionBoxStartX; x < collisionBoxStartX + collisionBoxWidth; x++) {
                if (x < 0 || x >= map.getMapManager().getWidth() || y + offsetY < 0 || y + offsetY >= map.getMapManager().getHeight()) {
                    continue;
                }
                if (map.getMapManager().getPixels()[y + offsetY][x].isCollide()) {
                    return offsetY;
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    private ArrayList<int[]> getLeftCollision() {
        Pixel[][] map = this.map.getMapManager().getPixels();
        ArrayList<int[]> positions = new ArrayList<>();
        for (int y = (int) collisionBoxStartY; y < collisionBoxStartY + collisionBoxHeight - 1; y++) {
            if (collisionBoxStartX - 1 < 0 || y < 0)
                continue;
            if (map[y][(int) (collisionBoxStartX - 1)].isCollide())
                positions.add(new int[]{(int) (collisionBoxStartX - 1), y});
        }
        return positions;
    }

    private ArrayList<int[]> getRightCollision() {
        Pixel[][] map = this.map.getMapManager().getPixels();
        ArrayList<int[]> positions = new ArrayList<>();
        for (int y = (int) collisionBoxStartY; y < collisionBoxStartY + collisionBoxHeight - 1; y++) {
            if (y < 0)
                continue;
            if (map[y][(int) (collisionBoxStartX + collisionBoxWidth + 1)].isCollide())
                positions.add(new int[]{(int) (collisionBoxStartX + collisionBoxWidth + 1), y});
        }
        return positions;
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

        new Text((width - graphics.getFontMetrics().stringWidth(name)) / 2 + x, (int) (y - TEXT_FORMAT.getSize()), name, TEXT_FORMAT).render(graphics);
    }

    private BufferedImage cropImage(BufferedImage src, int x, int y) {
        return src.getSubimage(x, y, 32, 64);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "\t" + x + "\t" + y + "\t" + watchingRight + "\t" + form;
    }
}
