package prj.shtelo.inminic.client.cameraobject;

import prj.shtelo.inminic.client.Root;
import prj.shtelo.inminic.client.discordrpc.DiscordRPCManager;
import prj.shtelo.inminic.client.root.Color;
import prj.shtelo.inminic.client.root.TextFormat;
import prj.shtelo.inminic.client.rootobject.RootObject;
import prj.shtelo.inminic.client.rootobject.Text;
import prj.shtelo.inminic.client.state.State;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Character extends RootObject {
    private final static int collisionBoxX = 4, collisionBoxY = 16, collisionBoxWidth = 24, collisionBoxHeight = 48;
    private final TextFormat TEXT_FORMAT = new TextFormat("./res/font/D2Coding.ttc", 18, Color.text);
    private int width = 32, height = 64;

    private double collisionBoxStartX;
    private double collisionBoxStartY;

    private double x, y;
    private String name;
    private DiscordRPCManager discordRPCManager;
    private Root root;

    private BufferedImage[] images;

    private int form = 0;
    private int formDelay;
    private boolean watchingRight;

    private double velocity;

    private double previousX, previousY;
    private int previousForm;

    public Character(double x, double y, String name, DiscordRPCManager discordRPCManager, Root root) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.discordRPCManager = discordRPCManager;
        this.root = root;

        init();
    }

    private void init() {
        BufferedImage image;
        
        try {
            image = ImageIO.read(new File("./res/character/" + name + ".png"));
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

        formDelay = 0;
        watchingRight = true;

        discordRPCManager.setPlayerInformation(name.toUpperCase());
    }

    @Override
    public void tick() {
        collisionBoxStartX = x - width / 2. + collisionBoxX;
        collisionBoxStartY = y - height / 2. + collisionBoxY;

        if (root.getStateManager().getState() == State.Main) {
            discordRPCManager.setState("가만히 있음");

            double moveSpeed = 60. / root.getDisplay().getDisplayFps();
            if (root.getKeyManager().isShift()) moveSpeed *= 2;

            if (root.getKeyManager().getMove()[1]) {
                if (getCoverHeight(moveSpeed) <= 2) {
                    x += moveSpeed;
                    y -= getCoverHeight(moveSpeed);
                } else if (getDeltaRight() > 0) {
                    x += Math.min(getDeltaRight(), moveSpeed);
                }
                discordRPCManager.setState(root.getKeyManager().isShift() ? "오른쪽으로 달림" : "오른쪽으로 걸음");
                watchingRight = true;
            }
            if (root.getKeyManager().getMove()[0]) {
                if (getCoverHeight(-moveSpeed) <= 2) {
                    x -= moveSpeed;
                    y -= getCoverHeight(-moveSpeed);
                } else if (getDeltaLeft() > 0) {
                    x -= Math.min(getDeltaLeft(), moveSpeed);
                }
                discordRPCManager.setState(root.getKeyManager().isShift() ? "왼쪽으로 달림" : "왼쪽으로 걸음");
                watchingRight = false;
            }
            if (root.getKeyManager().getMove()[0] && root.getKeyManager().getMove()[1]) {
                discordRPCManager.setState(root.getKeyManager().isShift() ? "양쪽으로 달림" : "양쪽으로 걸음");
            }
        } else {
            discordRPCManager.setState("채팅 입력중");
        }

        int maxDelay = (int) (root.getDisplay().getDisplayFps() / 8);
        if (x != previousX || y != previousY || root.getKeyManager().isMoveStop()) {
            formDelay++;

            if (form == 0) form = 4;

            if (formDelay >= maxDelay) {
                if (form == 4) form = 5;
                else if (form == 5) form = 6;
                else if (form == 6) form = 7;
                else form = 4;
                formDelay -= maxDelay;
            }
        } else {
            formDelay = 0;
            form = 0;
        }

        gravityAction();

        if (root.getClient().isConnected())
            if (x != previousX || y != previousY || form != previousForm)
                root.getClient().send("move\t" + toString());
        previousX = x;
        previousY = y;
        previousForm = form;
    }

    private double py;

    private void gravityAction() {
        if (getDeltaY() > 0) {
            velocity += 1411.2 / root.getDisplay().getDisplayFps() / root.getDisplay().getDisplayFps();
            if (velocity > getDeltaY()) {
                velocity = getDeltaY();
            }
            discordRPCManager.setState("공중에 뜸");
        } else {
            velocity = 0;
        }

        if (root.getStateManager().getState() == State.Main) {
            if (root.getKeyManager().isJumping() && getDeltaY() == 0)
                velocity -= 300 / root.getDisplay().getDisplayFps();
        }

        y += velocity;

        if (y == py) {
            y = (int) y;
        }
        py = y;
    }

    private int getCoverHeight(double xOffset) {
        for (int y = (int) collisionBoxStartY; y < collisionBoxStartY + collisionBoxHeight; y++) {
            if (y < 0 || y >= root.getMap().getMapManager().getHeight()) continue;
            for (int x = (int) (collisionBoxStartX + xOffset); x < collisionBoxStartX + collisionBoxWidth + xOffset; x++) {
                if (x < 0 || x >= root.getMap().getMapManager().getWidth()) continue;
                if (root.getMap().getMapManager().getPixels()[y][x].isCollide()) {
                    return (int) (collisionBoxHeight - (y - collisionBoxStartY));
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    private int getDeltaY() {
        int offsetY = 0;
        for (int y = (int) (collisionBoxStartY + collisionBoxHeight); y + offsetY < root.getMap().getMapManager().getHeight(); offsetY++) {
            for (int x = (int) collisionBoxStartX; x < collisionBoxStartX + collisionBoxWidth; x++) {
                if (x < 0 || x >= root.getMap().getMapManager().getWidth() || y + offsetY < 0 || y + offsetY >= root.getMap().getMapManager().getHeight()) {
                    continue;
                }
                if (root.getMap().getMapManager().getPixels()[y + offsetY][x].isCollide()) {
                    return offsetY;
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    private int getDeltaRight() {
        int startX = (int) (collisionBoxStartX + collisionBoxWidth);
        for (int x = startX; x < root.getMap().getMapManager().getWidth(); x++) {
            if (x < 0 || x >= root.getMap().getMapManager().getWidth()) continue;
            for (int y = (int) collisionBoxStartY; y < collisionBoxStartY + collisionBoxHeight; y++) {
                if (y < 0 || y >= root.getMap().getMapManager().getHeight()) continue;
                if (root.getMap().getMapManager().getPixels()[y][x].isCollide()) {
                    return x - startX;
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    private int getDeltaLeft() {
        int startX = (int) collisionBoxStartX;
        for (int x = startX; x > 0; x--) {
            if (x >= root.getMap().getMapManager().getWidth()) continue;
            for (int y = (int) collisionBoxStartY; y < collisionBoxStartY + collisionBoxHeight; y++) {
                if (y < 0 || y >= root.getMap().getMapManager().getHeight()) continue;
                if (root.getMap().getMapManager().getPixels()[y][x].isCollide()) {
                    return startX - x;
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public void render(Graphics graphics) {
        BufferedImage image = images[form];
        int width = (int) (this.width * root.getCamera().getZoom());
        int height = (int) (this.height * root.getCamera().getZoom());
        int x = (int) ((this.x - root.getCamera().getX()) * root.getCamera().getZoom() + (root.getDisplay().getWidth() - width) / 2);
        int y = (int) ((this.y - root.getCamera().getY()) * root.getCamera().getZoom() + (root.getDisplay().getHeight() - height) / 2);

        if (watchingRight)
            graphics.drawImage(image, x, y, width, height, null);
        else
            graphics.drawImage(image, x + width, y, -width, height, null);

        int textWidth = TEXT_FORMAT.getStringWidth(name);
        int textX = ((width - textWidth) / 2 + x);
        int textY = (int) (y - TEXT_FORMAT.getSize());
        new Text(textX, textY, name, TEXT_FORMAT).render(graphics);
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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return name + "\t" + x + "\t" + y + "\t" + watchingRight + "\t" + form;
    }
}
