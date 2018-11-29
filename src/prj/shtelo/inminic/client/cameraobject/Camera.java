package prj.shtelo.inminic.client.cameraobject;

public class Camera {
    private double x, y, zoom;

    public Camera(double x, double y, double zoom) {
        this.x = x;
        this.y = y;
        this.zoom = zoom;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    double getZoom() {
        return zoom;
    }
}
