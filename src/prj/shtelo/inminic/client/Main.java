package prj.shtelo.inminic.client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Root root = new Root("InMinic", 1280, 720, 144, "sch_0q0");
        root.start();
    }
}
