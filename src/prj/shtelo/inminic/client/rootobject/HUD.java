package prj.shtelo.inminic.client.rootobject;

import prj.shtelo.inminic.client.Root;
import prj.shtelo.inminic.client.root.TextFormat;
import prj.shtelo.inminic.client.cameraobject.Camera;

import java.awt.*;

public class HUD extends RootObject {
    private TextFormat textFormat;
    private Camera camera;
    private Root root;

    private final int count = 5;
    private String[] lines = new String[count];

    public HUD(TextFormat textFormat, Camera camera, Root root) {
        this.textFormat = textFormat;
        this.camera = camera;
        this.root = root;
    }

    @Override
    public void tick() {
        lines[0] = "POSITION " + String.format("%f", root.getCharacter().getX()) + " " + String.format("%f", root.getCharacter().getY());
        lines[1] = "CAMERA " + String.format("%f", camera.getX()) + " " + String.format("%f", camera.getY()) + " " + String.format("%f", camera.getZoom());
        lines[2] = "OBJECTS " + RootObject.objects.size();
        lines[3] = "FPS " + String.format("%f", root.getDisplay().getDisplayFps()) + " / " + root.getDisplay().getFps();
        if (root.getClient().getConnected())
            lines[4] = "SERVER " + root.getClient().getHost() + ":" + root.getClient().getPort();
        else
            lines[4] = "SERVER NOT_CONNECTED";
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(textFormat.getColor());
        graphics.setFont(textFormat.getFont());

        for (int i = 0; i < lines.length; i++)
            graphics.drawString(lines[i], 0, (int) (textFormat.getSize() * (i+1)));
    }
}
