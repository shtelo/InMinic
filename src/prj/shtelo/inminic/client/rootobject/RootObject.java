package prj.shtelo.inminic.client.rootobject;

import java.awt.*;
import java.util.ArrayList;

public abstract class RootObject {
    public static ArrayList<RootObject> objects = new ArrayList<>();

    public static void add(RootObject rootObject) {
        objects.add(rootObject);
    }

    public void destroy() {
        objects.remove(this);
    }

    public void tick() {}

    public void render(Graphics graphics) {}
}
