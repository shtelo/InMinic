package prj.shtelo.inminic.client;

import prj.shtelo.inminic.client.cameraobject.Camera;
import prj.shtelo.inminic.client.cameraobject.Character;
import prj.shtelo.inminic.client.cameraobject.Map;
import prj.shtelo.inminic.client.cameraobject.Player;
import prj.shtelo.inminic.client.discordrpc.DiscordRPCManager;
import prj.shtelo.inminic.client.root.Color;
import prj.shtelo.inminic.client.root.Display;
import prj.shtelo.inminic.client.root.KeyManager;
import prj.shtelo.inminic.client.root.TextFormat;
import prj.shtelo.inminic.client.rootobject.ChattingBox;
import prj.shtelo.inminic.client.rootobject.HUD;
import prj.shtelo.inminic.client.rootobject.RootObject;
import prj.shtelo.inminic.client.rootobject.particle.Flame;
import prj.shtelo.inminic.client.state.State;
import prj.shtelo.inminic.client.state.StateManager;
import prj.shtelo.inminic.client.telecommunication.Client;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Root implements Runnable {
    private String title;
    private int width, height, fps;
    private String name;

    private Display display;
    private Thread thread;
    private HUD hud;
    private Client client;

    private boolean running;

    private KeyManager keyManager;
    private Camera camera;
    private Map map;
    private Character character;
    private ChattingBox chattingBox;
    private StateManager stateManager;

    private DiscordRPCManager discordRPCManager;

    Root(String title, int width, int height, int fps, String name) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.name = name;
    }

    private void init() {
        discordRPCManager = new DiscordRPCManager("523303985478762506", "");

        keyManager = new KeyManager();
        camera = new Camera(0, 0, 2, this);
        hud = new HUD(new TextFormat("./res/font/D2Coding.ttc", 15, Color.text), this);

        display = new Display(title, width, height, fps, this);
        thread = new Thread(this);

        client = new Client(this);

        map = new Map("test001", discordRPCManager, this);
        character = new Character(11, -100, name, discordRPCManager, this);

        stateManager = new StateManager(State.Main);

        chattingBox = new ChattingBox(200, 30, this);
    }

    private void tick() {
        keyManager.tick();

        camera.tick();
        map.tick();
        for (RootObject rootObject : RootObject.objects) {
            rootObject.tick();
        }
        character.tick();
        chattingBox.tick();
        hud.tick();

        if (keyManager.isTesting()) {
            RootObject.add(new Flame(256, 586, display, camera));
        }
        if (keyManager.isToggleChatting()) {
            stateManager.setState(stateManager.getState() == State.Chatting ? State.Main : State.Chatting);
        }

        discordRPCManager.update();
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

        map.render(graphics);

        RootObject[] objects = RootObject.getObjectsClone();
        for (RootObject rootObject : objects) {
            rootObject.render(graphics);
        }
        RootObject.cleanRemoves();
        character.render(graphics);
        chattingBox.render(graphics);
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
        init();

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
        discordRPCManager.shutdown();
    }

    public Player findPlayerByName(String name) {
        for (RootObject object : RootObject.objects) {
            Player player = (Player) object;
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
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

    public Client getClient() {
        return client;
    }

    public Camera getCamera() {
        return camera;
    }

    public String getName() {
        return name;
    }

    public DiscordRPCManager getDiscordRPCManager() {
        return discordRPCManager;
    }

    public StateManager getStateManager() {
        return stateManager;
    }

    public ChattingBox getChattingBox() {
        return chattingBox;
    }

    public Map getMap() {
        return map;
    }
}
