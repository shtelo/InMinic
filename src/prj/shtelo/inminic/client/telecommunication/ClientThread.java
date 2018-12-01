package prj.shtelo.inminic.client.telecommunication;

import prj.shtelo.inminic.client.Root;
import prj.shtelo.inminic.client.cameraobject.Player;
import prj.shtelo.inminic.client.rootobject.RootObject;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

class ClientThread extends Thread {
    private Socket socket;
    private Client client;
    private Root root;

    private Scanner scanner;

    ClientThread(Socket socket, Client client, Root root) throws IOException {
        this.socket = socket;
        this.client = client;
        this.root = root;

        init();
    }

    private void init() throws IOException {
        scanner = new Scanner(socket.getInputStream());

        String message;
        String[] messages;

        message = scanner.nextLine();
        messages = message.split("\t");

        System.out.println(Arrays.toString(messages));
        if (messages[0].equalsIgnoreCase("serverInfo")) {
            client.setMapName(messages[1]);

            int count = Integer.parseInt(messages[2]);
            for (int i = 0; i < count; i++) {
                messages = scanner.nextLine().split("\t");
                double x = Double.parseDouble(messages[1]);
                double y = Double.parseDouble(messages[2]);
                String name = messages[3];
                RootObject.add(new Player(x, y, name, root.getCamera(), root));
            }
        }
    }

    @Override
    public void run() {
        String message;
        String[] messages;
        while (client.isConnected()) {
            message = scanner.nextLine();
            messages = message.split("\t");

            System.out.println(Arrays.toString(messages));
        }
    }
}
