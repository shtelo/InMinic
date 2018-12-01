package prj.shtelo.inminic.client;

import prj.shtelo.inminic.client.cameraobject.Camera;
import prj.shtelo.inminic.client.cameraobject.Character;
import prj.shtelo.inminic.client.cameraobject.Map;
import prj.shtelo.inminic.client.root.Color;
import prj.shtelo.inminic.client.root.Display;
import prj.shtelo.inminic.client.root.KeyManager;
import prj.shtelo.inminic.client.root.TextFormat;
import prj.shtelo.inminic.client.rootobject.HUD;
import prj.shtelo.inminic.client.rootobject.RootObject;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Root implements Runnable {
    private String title;
    private int width, height, fps;

    private Display display;
    private Thread thread;
    private HUD hud;

    private boolean running;

    private KeyManager keyManager;
    private Camera camera;
    private Character character;

    Root(String title, int width, int height, int fps) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.fps = fps;

        init();
    }

    private void init() {
        keyManager = new KeyManager();
        camera = new Camera(0, 0, 2, this);
        hud = new HUD(new TextFormat(".\\res\\font\\D2Coding.ttc", 15, Color.text), camera, this);

        display = new Display(title, width, height, fps, this);
        thread = new Thread(this);

        RootObject.add(new Map("test001", camera, this));

        character = new Character(11, 0, "sch_0q0", camera, this);
        RootObject.add(character);
    }

    private void tick() {
        keyManager.tick();
        camera.tick();
        for (RootObject rootObject : RootObject.objects) {
            rootObject.tick();
        }
        hud.tick();
    }

    private void render() {
        BufferStrategy bufferStrategy = display.getCanvas().getBufferStrategy();
        if (bufferStrategy == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        Graphics graphics = bufferStrategy.getDrawGraphics();

        graphics.setColor(Color.background);
        graphics.fillRect(0, 0, display.getWidth(), display.getHeight());

        for (RootObject rootObject : RootObject.objects) {
            rootObject.render(graphics);
        }

        hud.render(graphics);

        bufferStrategy.show();
        graphics.dispose();
    }

    @Override
    public void run() {
        double timePerLoop = 1_000_000_000. / display.getFps();
        double delta = 0;
        long now, pnow = System.nanoTime();
        long loop = System.nanoTime(), ploop;

        while (running) {
            now = System.nanoTime();
            delta += (now - pnow) / timePerLoop;
            pnow = now;

            if (delta >= 1) {
                delta--;

                ploop = loop;
                loop = System.nanoTime();

                display.setDisplayFps(1. / (loop - ploop) * 1_000_000_000);

                tick();
                render();
            }
        }

        stop();
    }

    void start() {
        if (running)
            return;
        running = true;
        thread.start();
    }

    private void stop() {
        if (!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }

    public Display getDisplay() {
        return display;
    }

    public Character getCharacter() {
        return character;
    }
}
