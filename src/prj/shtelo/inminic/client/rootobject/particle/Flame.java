package prj.shtelo.inminic.client.rootobject.particle;

import prj.shtelo.inminic.client.cameraobject.Camera;
import prj.shtelo.inminic.client.root.Display;
import prj.shtelo.inminic.client.rootobject.RootObject;

import java.awt.*;
import java.util.Random;

public class Flame extends RootObject {
    private double x, y;
    private Display display;
    private Camera camera;

    private Color color;
//    private double velX, velY;
    private ParticleMotion particleMotion;
    private double maximumSize, size;
    private double lastTime;
    private double maximumLifeTime;
    private double lifeTime;

    public Flame(int x, int y, Display display, Camera camera) {
        this.x = x;
        this.y = y;
        this.display = display;
        this.camera = camera;

        init();
    }

    private void init() {
        Random random = new Random();

//        velX = (random.nextDouble() - 0.5) * 30 / display.getDisplayFps();
//        velY = -random.nextDouble() * 60 / display.getDisplayFps();

        particleMotion = new ParticleMotion(
                (random.nextDouble() - 0.5) * 30 * 5 / display.getDisplayFps(),
                -random.nextDouble() * 60 * 5 / display.getDisplayFps());

        lastTime = System.currentTimeMillis();
        maximumLifeTime = random.nextDouble() * 1500;
        lifeTime = 0;

        maximumSize = random.nextDouble() * 4;

        color = new Color(255, 0, 0);
    }

    @Override
    public void tick() {
        x = particleMotion.moveX(x);
        y = particleMotion.moveY(y);

        lifeTime += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();

        size = maximumSize * Math.max(1 - (lifeTime / maximumLifeTime), 0);
        color = new Color(255, (int) (255 * Math.max(1 - (lifeTime / maximumLifeTime), 0)), 0);

        if (Math.max(1 - (lifeTime / maximumLifeTime), 0) <= 0) {
            destroy();
        }
    }

    @Override
    public void render(Graphics graphics) {
        int x = (int) camera.getRelativeX(this.x);
        int y = (int) camera.getRelativeY(this.y);

        if (x < 0 || x > display.getWidth() || y < 0 || y > display.getHeight()) return;

        int size = (int) (this.size * camera.getZoom());

        graphics.setColor(color);
        graphics.fillRect(x, y, size, size);
    }
}
