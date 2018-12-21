package prj.shtelo.inminic.client.cameraobject;

import prj.shtelo.inminic.client.Root;
import prj.shtelo.inminic.client.cameraobject.map.MapManager;
import prj.shtelo.inminic.client.discordrpc.DiscordRPCManager;
import prj.shtelo.inminic.client.rootobject.RootObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Map extends RootObject {
    private String name;
    private DiscordRPCManager discordRPCManager;
    private Root root;

    private MapManager mapManager;
    private BufferedImage image;

    public Map(String name, DiscordRPCManager discordRPCManager, Root root) {
        this.name = name;
        this.discordRPCManager = discordRPCManager;
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

        discordRPCManager.setMapInformation(name);
        if (root.getClient().isConnected()) {
            discordRPCManager.setSmallImageKey("multiplay");
        } else {
            discordRPCManager.setSmallImageKey("singleplay");
        }
    }

    @Override
    public void render(Graphics graphics) {
        int x = (int) (-root.getCamera().getX() * root.getCamera().getZoom() + root.getDisplay().getWidth() / 2);
        int y = (int) (-root.getCamera().getY() * root.getCamera().getZoom() + root.getDisplay().getHeight() / 2);
        int width = (int) (image.getWidth() * root.getCamera().getZoom());
        int height = (int) (image.getHeight() * root.getCamera().getZoom());

        graphics.drawImage(image, x, y, width, height, null);
    }

    public void changeMapName(String name) {
        this.name = name;

        init();
    }

    MapManager getMapManager() {
        return mapManager;
    }
}
