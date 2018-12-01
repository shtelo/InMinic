package prj.shtelo.inminic.client.cameraobject;

import prj.shtelo.inminic.client.Root;
import prj.shtelo.inminic.client.rootobject.RootObject;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Map extends RootObject {
    private String name;
    private Camera camera;
    private Root root;

    private int width, height;
    private Color[] colors;
    private Color[][] pixels;

    public Map(String name, Camera camera, Root root) {
        this.name = name;
        this.camera = camera;
        this.root = root;

        init();
    }

    private void init() {
        Scanner scanner;
        try {
            scanner = new Scanner(new File(".\\res\\map\\" + name + ".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        int colorCount = scanner.nextInt();
        colors = new Color[colorCount];
        for (int i = 0; i < colorCount; i++) {
            int r = scanner.nextInt();
            int g = scanner.nextInt();
            int b = scanner.nextInt();

            colors[i] = new Color(r, g, b);
        }

        System.out.println(Arrays.toString(colors));

        width = scanner.nextInt();
        height = scanner.nextInt();
        pixels = new Color[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y][x] = colors[scanner.nextInt()];
            }
        }
    }

    @Override
    public void render(Graphics graphics) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                graphics.setColor(pixels[y][x]);
//                int xp = (int) ((x - camera.getX()) * camera.getZoom() + (root.getDisplay().getWidth() - width) / 2) + x + width;
//                int yp = (int) ((y - camera.getY()) * camera.getZoom() + (root.getDisplay().getHeight() - height) / 2) + y + height;
                int xp = (int) (x * camera.getZoom() - camera.getX() * camera.getZoom() + (root.getDisplay().getWidth() / 2));
                int yp = (int) (y * camera.getZoom() - camera.getY() * camera.getZoom() + (root.getDisplay().getHeight() / 2));
                graphics.fillRect(xp, yp, (int) camera.getZoom() + 2, (int) camera.getZoom() + 2);
            }
        }
    }
}
