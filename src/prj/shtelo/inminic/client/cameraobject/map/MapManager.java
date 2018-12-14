package prj.shtelo.inminic.client.cameraobject.map;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class MapManager {
    final static char SEPARATOR = ' ';

    private String name, directory;

    private int width, height;

    private Pixel[][] pixels;

    public MapManager(String name) {
        this.name = name;

        init();
    }

    private void init() {
        directory = "./res/map/" + name + "/main.txt";

        read();
    }

    private void read() {
        Scanner scanner;
        try {
            scanner = new Scanner(new File(directory));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        width = scanner.nextInt();
        height = scanner.nextInt();
        pixels = new Pixel[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = scanner.nextInt();
                int g = scanner.nextInt();
                int b = scanner.nextInt();
                int c = scanner.nextInt();

                pixels[y][x] = new Pixel(new Color(r, g, b), c == 1);
            }
        }
    }

    public void write() {
        write(width, height);
    }

    public void write(int width, int height) {
        this.width = width;
        this.height = height;

        PrintStream printStream;

        try {
            printStream = new PrintStream(new File(directory));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        printStream.print(width);
        printStream.print(SEPARATOR);
        printStream.print(height);
        printStream.print(SEPARATOR);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                printStream.print(pixels[y][x].toString());
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getColor(int x, int y) {
        return pixels[y][x].getColor();
    }

    public Pixel[][] getPixels() {
        return pixels;
    }

    public void setPixels(Pixel[][] pixels) {
        this.pixels = pixels;
        this.height = this.pixels.length;
        this.width = this.pixels[0].length;
    }

    public void setName(String name) {
        this.name = name;
    }
}
