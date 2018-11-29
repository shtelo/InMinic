package prj.shtelo.inminic.client;

import prj.shtelo.inminic.client.cameraobject.Camera;
import prj.shtelo.inminic.client.root.Display;
import prj.shtelo.inminic.client.cameraobject.Character;
import prj.shtelo.inminic.client.rootobject.RootObject;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Root implements Runnable {
    private String title;
    private int width, height, fps;

    private Display display;
    private Thread thread;

    private boolean running;

    private KeyManager keyManager;
    private Camera camera;

    public Root(String title, int width, int height, int fps) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.fps = fps;

        init();
    }

    private void init() {
        keyManager = new KeyManager();
        camera = new Camera(0, 0, 3);

        display = new Display(title, width, height, fps, this);
        thread = new Thread(this);

        RootObject.add(new Character(0, 0, "sch_0q0", camera, this));
    }

    private void tick() {
        keyManager.tick();
        for (RootObject rootObject : RootObject.objects) {
            rootObject.tick();
        }
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

        bufferStrategy.show();
        graphics.dispose();
    }

    @Override
    public void run() {
        double timePerLoop = 1000000000. / display.getFps();
        double delta = 0;
        long now;
        long pnow = System.nanoTime();

        while (running) {
            now = System.nanoTime();
            delta += (now - pnow) / timePerLoop;
            pnow = now;

            if (delta >= 1) {
                delta--;
                tick();
                render();
            }
        }

        stop();
    }

    public void start() {
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
}
