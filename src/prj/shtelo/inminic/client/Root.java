package prj.shtelo.inminic.client;

import prj.shtelo.inminic.client.root.Display;
import prj.shtelo.inminic.client.root.Text;
import prj.shtelo.inminic.client.rootobject.Image;
import prj.shtelo.inminic.client.rootobject.RootObject;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Root implements Runnable {
    private String title;
    private int width, height;

    private Display display;
    private Thread thread;

    private boolean running;

    private Graphics graphics;
    private BufferStrategy bufferStrategy;

    public Root(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;

        init();
    }

    private void init() {
        display = new Display(title, width, height);
        thread = new Thread(this);
    }

    private void tick() {
        for (RootObject rootObject : RootObject.objects) {
            rootObject.tick();
        }
    }

    private void render() {
        bufferStrategy = display.getCanvas().getBufferStrategy();
        if (bufferStrategy == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        graphics = bufferStrategy.getDrawGraphics();

        graphics.setColor(Color.background);
        graphics.fillRect(0, 0, width, height);

        for (RootObject rootObject : RootObject.objects) {
            rootObject.render(graphics);
        }

        bufferStrategy.show();
        graphics.dispose();
    }

    @Override
    public void run() {
        while (running) {
            tick();
            render();
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
}
