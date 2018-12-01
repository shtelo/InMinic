package prj.shtelo.inminic.client.cameraobject.map;

import java.awt.*;

public class Pixel {
    private Color color;
    private boolean collision;

    Pixel(Color color, boolean collision) {
        this.color = color;
        this.collision = collision;
    }

    Color getColor() {
        return color;
    }

    public boolean isCollide() {
        return collision;
    }

    @Override
    public String toString() {
        return "" + color.getRed() + MapManager.SEPARATOR + color.getGreen() + MapManager.SEPARATOR + color.getBlue() + MapManager.SEPARATOR + (collision ? 1 : 0) + MapManager.SEPARATOR;
    }
}
