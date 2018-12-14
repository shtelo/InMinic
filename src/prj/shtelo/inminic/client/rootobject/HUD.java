package prj.shtelo.inminic.client.rootobject;

import prj.shtelo.inminic.client.Root;
import prj.shtelo.inminic.client.cameraobject.Camera;
import prj.shtelo.inminic.client.root.KeyManager;
import prj.shtelo.inminic.client.root.TextFormat;

import java.awt.*;

public class HUD extends RootObject {
    private TextFormat textFormat;
    private Camera camera;
    private KeyManager keyManager;
    private Root root;

    private final int count = 5;
    private String[] lines = new String[count];
    private Text[] texts = new Text[count];

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
            lines[0] = "POSITION " + String.format("%f", root.getCharacter().getX()) + " " + String.format("%f", root.getCharacter().getY());
            lines[1] = "CAMERA " + String.format("%f", camera.getX()) + " " + String.format("%f", camera.getY()) + " " + String.format("%f", camera.getZoom());
            lines[2] = "OBJECTS " + RootObject.objects.size();
            lines[3] = "FPS " + String.format("%f", root.getDisplay().getDisplayFps()) + " / " + root.getDisplay().getFps();
            if (root.getClient().getConnected())
                lines[4] = "SERVER " + root.getClient().getHost() + ":" + root.getClient().getPort();
            else
                lines[4] = "SERVER NOT_CONNECTED";

            for (int i = 0; i < count; i++) {
                texts[i] = new Text(0, (int) (textFormat.getSize() * (i + 1)), lines[i], textFormat);
            }
        }
    }

    @Override
    public void render(Graphics graphics) {
        if (showing) {
            for (int i = 0; i < count; i++)
                texts[i].render(graphics);
        }
    }
}
