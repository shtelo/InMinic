package prj.shtelo.inminic.client.rootobject;

import java.awt.*;
import java.util.ArrayList;

public abstract class RootObject {
    public static ArrayList<RootObject> objects = new ArrayList<>();
    private static ArrayList<RootObject> removes = new ArrayList<>();

    public static void add(RootObject rootObject) {
        objects.add(rootObject);
    }

    public void destroy() {
        RootObject.removes.add(this);
    }

    public static void cleanRemoves() {
        RootObject.objects.removeAll(removes);
        removes = new ArrayList<>();
    }

    public void tick() {}

    public void render(Graphics graphics) {}

    public static RootObject[] getObjectsClone() {
        RootObject[] result = new RootObject[objects.size()];
        for (int i = 0; i < objects.size(); i++) {
            result[i] = objects.get(i);
        }
        return result;
    }
}
