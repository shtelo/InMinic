package prj.shtelo.inminic.client.rootobject;

import prj.shtelo.inminic.client.Root;
import prj.shtelo.inminic.client.cameraobject.Camera;
import prj.shtelo.inminic.client.root.KeyManager;
import prj.shtelo.inminic.client.root.TextFormat;

import java.awt.*;
import java.util.ArrayList;

public class HUD extends RootObject {
    private TextFormat textFormat;
    private Camera camera;
    private KeyManager keyManager;
    private Root root;

    private ArrayList<String> lines = new ArrayList<>();
    private ArrayList<Text> texts = new ArrayList<>();

    private boolean showing = false;

    public HUD(TextFormat textFormat, Camera camera, KeyManager keyManager, Root root) {
        this.textFormat = textFormat;
        this.camera = camera;
        this.keyManager = keyManager;
        this.root = root;
    }

    @Override
    public void tick() {
        if (keyManager.isToggleHUD()) showing = !showing;

        if (showing) {
            lines = new ArrayList<>();

            lines.add("POSITION " + String.format("%f", root.getCharacter().getX()) + " " + String.format("%f", root.getCharacter().getY()));
            lines.add("CAMERA " + String.format("%f", camera.getX()) + " " + String.format("%f", camera.getY()) + " " + String.format("%f", camera.getZoom()));
            lines.add("OBJECTS " + RootObject.objects.size());
            lines.add("FPS " + String.format("%f", root.getDisplay().getDisplayFps()) + " / " + root.getDisplay().getFps());
            if (root.getClient().isConnected())
                lines.add("SERVER " + root.getClient().getHost() + ":" + root.getClient().getPort());
            else
                lines.add("SERVER NOT_CONNECTED");
            lines.add("STATE " + root.getStateManager().getState());

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
