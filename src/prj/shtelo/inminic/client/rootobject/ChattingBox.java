package prj.shtelo.inminic.client.rootobject;

import prj.shtelo.inminic.client.Root;
import prj.shtelo.inminic.client.root.TextFormat;
import prj.shtelo.inminic.client.state.State;

import java.awt.*;
import java.util.ArrayList;

public class ChattingBox extends RootObject {
    private int x, y;
    private Root root;

    private ArrayList<String> messages;
    private String inserting;

    private TextFormat textFormat;

    public ChattingBox(int x, int y, Root root) {
        this.x = x;
        this.y = y;
        this.root = root;

        init();
    }

    private void init() {
        messages = new ArrayList<>();
        inserting = "";

        textFormat = new TextFormat("./res/font/D2Coding.ttc", 16, Color.white);
    }

    public void add(String name, String chatting) {
        messages.add(name + ": " + chatting);
    }

    @Override
    public void tick() {
        if (root.getStateManager().getState() == State.Chatting) {
            inserting += root.getKeyManager().getQueue();

            if (root.getKeyManager().isBackspace() && !inserting.isEmpty())
                inserting = inserting.substring(0, inserting.length() - 1);

            if (root.getKeyManager().isToggleChatting()) {
                if (!inserting.equals("")) {
                    while (inserting.charAt(0) == ' ')
                        inserting = inserting.substring(1);
                    while (inserting.charAt(inserting.length() - 1) == ' ')
                        inserting = inserting.substring(0, inserting.length() - 2);

                    if (inserting.charAt(0) != '@') {
                        if (root.getClient().isConnected()) {
                            root.getClient().chatting(inserting);
                        } else {
                            add(root.getCharacter().getName(), inserting);
                        }
                    } else {
                        String[] insertings = inserting.split(" ");

                        if (insertings[0].equalsIgnoreCase("@connect")) {
                            boolean problem = true;
                            
                            if (insertings.length == 2) {
                                problem = root.getClient().connect(insertings[1]);
                            } else if (insertings.length == 3) {
                                try {
                                    problem = root.getClient().connect(insertings[1], Integer.parseInt(insertings[2]));
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                            
                            System.out.println(problem);
                        } else if (insertings[0].equalsIgnoreCase("@disconnect")) {
                            root.getClient().disconnect();
                        } else if (insertings[0].equalsIgnoreCase("@tp")) {
                            if (insertings.length >= 3) {
                                double nextX, nextY;
                                try {
                                    nextX = Double.parseDouble(insertings[1]);
                                    nextY = Double.parseDouble(insertings[2]);

                                    root.getCharacter().setX(nextX);
                                    root.getCharacter().setY(nextY);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    inserting = "";
                }
            }
        } else {
            root.getKeyManager().resetQueue();
        }
    }

    @Override
    public void render(Graphics graphics) {
        if (root.getStateManager().getState() == State.Chatting) {
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
