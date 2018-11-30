package prj.shtelo.inminic.client.rootobject;

import prj.shtelo.inminic.client.Root;
import prj.shtelo.inminic.client.TextFormat;
import prj.shtelo.inminic.client.cameraobject.Camera;

import java.awt.*;

public class HUD extends RootObject {
    private TextFormat textFormat;
    private Camera camera;
    private Root root;

    public HUD(TextFormat textFormat, Camera camera, Root root) {
        this.textFormat = textFormat;
        this.camera = camera;
        this.root = root;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(textFormat.getColor());

        graphics.drawString("POSITION " + root.getCharacter().getX() + " " + root.getCharacter().getY(), 0, (int) textFormat.getSize());
        graphics.drawString("CAMERA " + camera.getX() + " " + camera.getY() + " " + camera.getZoom(), 0, (int) textFormat.getSize() * 2);
        graphics.drawString("OBJECTS " + RootObject.objects.size(), 0, (int) textFormat.getSize() * 3);
    }
}
