package prj.shtelo.inminic.client.cameraobject;

import prj.shtelo.inminic.client.Root;

public class Camera {
    private double x, y, zoom;
    private Root root;

    public Camera(double x, double y, double zoom, Root root) {
        this.x = x;
        this.y = y;
        this.zoom = zoom;
        this.root = root;
    }

    public void tick() {
        x = root.getCharacter().getX();
        y = root.getCharacter().getY();

        if (root.getKeyManager().isZoomUp()) zoom += 0.5;
        if (root.getKeyManager().isZoomDown()) zoom -= 0.5;

        if (zoom < 0.5) {
            zoom = 0.5;
        } else if (zoom > 10) {
            zoom = 10;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZoom() {
        return zoom;
    }
}
