package prj.shtelo.inminic.client.rootobject;

import prj.shtelo.inminic.client.Root;
import prj.shtelo.inminic.client.root.TextFormat;

import java.awt.*;
import java.util.ArrayList;

public class HUD extends RootObject {
    private TextFormat textFormat;
    private Root root;

    private ArrayList<Text> texts = new ArrayList<>();

    private boolean showing = false;

    public HUD(TextFormat textFormat, Root root) {
        this.textFormat = textFormat;
        this.root = root;
    }

    @Override
    public void tick() {
        if (root.getKeyManager().isToggleHUD()) showing = !showing;

        if (showing) {
            ArrayList<String> lines = new ArrayList<>();

            lines.add("FPS " +
                    String.format("%f", root.getDisplay().getDisplayFps()) + " / " + root.getDisplay().getFps());
            lines.add("DISPLAY " + root.getDisplay().getWidth() + "x" + root.getDisplay().getHeight());
            lines.add("OBJECTS " + RootObject.objects.size());
            lines.add("");

            lines.add(root.getClient().isConnected() ?
                    "SERVER " + root.getClient().getHost() + ":" + root.getClient().getPort() :
                    "SERVER NOT_CONNECTED");
            lines.add("STATE " + root.getStateManager().getState());
            lines.add("POSITION " +
                    String.format("%f", root.getCharacter().getX()) + " " +
                    String.format("%f", root.getCharacter().getY()));
            lines.add("CAMERA " +
                    String.format("%f", root.getCamera().getX()) + " " +
                    String.format("%f", root.getCamera().getY()) + " " +
                    String.format("%f", root.getCamera().getZoom()));

            texts = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                texts.add(new Text(0, (int) (textFormat.getSize() * (i + 1)), lines.get(i), textFormat));
            }
        }
    }

    @Override
    public void render(Graphics graphics) {
        if (showing) {
            for (Text text : texts) text.render(graphics);
        }
    }
}
