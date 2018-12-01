package prj.shtelo.inminic.client.cameraobject;

import prj.shtelo.inminic.client.Root;
import prj.shtelo.inminic.client.cameraobject.map.MapManager;
import prj.shtelo.inminic.client.rootobject.RootObject;

import java.awt.*;

public class Map extends RootObject {
    private String name;
    private Camera camera;
    private Root root;

    private MapManager mapManager;

    public Map(String name, Camera camera, Root root) {
        this.name = name;
        this.camera = camera;
        this.root = root;

        init();
    }

    private void init() {
        mapManager = new MapManager(name);
    }

    @Override
    public void render(Graphics graphics) {
        for (int y = 0; y < mapManager.getHeight(); y++) {
            for (int x = 0; x < mapManager.getWidth(); x++) {
                graphics.setColor(mapManager.getColor(x, y));
                int xp = (int) (x * camera.getZoom() - camera.getX() * camera.getZoom() + (root.getDisplay().getWidth() / 2));
                int yp = (int) (y * camera.getZoom() - camera.getY() * camera.getZoom() + (root.getDisplay().getHeight() / 2));
                graphics.fillRect(xp, yp, (int) camera.getZoom() + 2, (int) camera.getZoom() + 2);
            }
        }
    }
}
