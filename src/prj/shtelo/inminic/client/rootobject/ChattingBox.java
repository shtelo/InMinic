package prj.shtelo.inminic.client.rootobject;

import prj.shtelo.inminic.client.root.KeyManager;
import prj.shtelo.inminic.client.root.TextFormat;
import prj.shtelo.inminic.client.state.State;
import prj.shtelo.inminic.client.state.StateManager;
import prj.shtelo.inminic.client.telecommunication.Client;

import java.awt.*;
import java.util.ArrayList;

public class ChattingBox extends RootObject {
    private int x, y;
    private StateManager stateManager;
    private KeyManager keyManager;
    private Client client;

    private ArrayList<String> messages;
    private String inserting;

    private TextFormat textFormat;

    public ChattingBox(int x, int y, StateManager stateManager, KeyManager keyManager, Client client) {
        this.x = x;
        this.y = y;
        this.stateManager = stateManager;
        this.keyManager = keyManager;
        this.client = client;

        init();
    }

    private void init() {
        messages = new ArrayList<>();
        inserting = "";

        textFormat = new TextFormat("./res/font/D2Coding.ttc", 16, Color.white);
    }

    public void add(String chatting) {
        messages.add(chatting);
    }

    @Override
    public void tick() {
        if (stateManager.getState() == State.Chatting) {
            inserting += keyManager.getQueue();

            if (keyManager.isBackspace() && !inserting.isEmpty())
                inserting = inserting.substring(0, inserting.length() - 1);

            if (keyManager.isToggleChatting()) {
                if (client.isConnected() && !inserting.equals(""))
                    client.chatting(inserting);
                inserting = "";
            }
        } else {
            keyManager.resetQueue();
        }
    }

    @Override
    public void render(Graphics graphics) {
        if (stateManager.getState() == State.Chatting) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            graphics2D.fillRect(x, y, 1000, 20);

            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            new Text(x, (int) (y + textFormat.getSize()), inserting, textFormat).render(graphics);
        }

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        graphics2D.setColor(Color.black);
        graphics2D.fillRect(x, y + 20, 1000, 20 * messages.size());

        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        for (int i = 0; i < messages.size(); i++) {
            int y = (int) (this.y + 20 * (i + 1) + textFormat.getSize());
            new Text(x, y, messages.get(i), textFormat).render(graphics);
        }
    }
}