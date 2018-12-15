package prj.shtelo.inminic.client.cameraobject;

import prj.shtelo.inminic.client.Root;
import prj.shtelo.inminic.client.cameraobject.map.MapManager;
import prj.shtelo.inminic.client.rootobject.RootObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Map extends RootObject {
    private String name;
    private Camera camera;
    private Root root;

    private MapManager mapManager;
    private BufferedImage image;

    public Map(String name, Camera camera, Root root) {
        this.name = name;
        this.camera = camera;
        this.root = root;

        init();
    }

    private void init() {
        mapManager = new MapManager(name);

        image = new BufferedImage(mapManager.getWidth(), mapManager.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < mapManager.getHeight(); y++) {
            for (int x = 0; x < mapManager.getWidth(); x++) {
                image.setRGB(x, y, mapManager.getColor(x, y).getRGB());
            }
        }

        root.getDiscordRPCManager().setMapInformation(name);
        if (root.getClient().isConnected()) {
            root.getDiscordRPCManager().setSmallImageKey("multiplay");
            root.getDiscordRPCManager().setState(root.getClient().getIP());
        } else {
            root.getDiscordRPCManager().setSmallImageKey("singleplay");
            root.getDiscordRPCManager().setState("싱글 플레이어");
        }
    }

    @Override
    public void render(Graphics graphics) {
        int x = (int) (-camera.getX() * camera.getZoom() + root.getDisplay().getWidth() / 2);
        int y = (int) (-camera.getY() * camera.getZoom() + root.getDisplay().getHeight() / 2);
        int width = (int) (image.getWidth() * camera.getZoom());
        int height = (int) (image.getHeight() * camera.getZoom());

        graphics.drawImage(image, x, y, width, height, null);
    }

    MapManager getMapManager() {
        return mapManager;
    }
}
