package prj.shtelo.inminic.client.telecommunication;

import prj.shtelo.inminic.client.Root;
import prj.shtelo.inminic.client.cameraobject.Player;
import prj.shtelo.inminic.client.rootobject.RootObject;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
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

        System.out.println("RECV " + message);
        if (messages[0].equalsIgnoreCase("serverInfo")) {
            client.setMapName(messages[1]);

            int count = Integer.parseInt(messages[2]);
            for (int i = 0; i < count; i++) {
                messages = scanner.nextLine().split("\t");
                String name = messages[1];
                double x = Double.parseDouble(messages[2]);
                double y = Double.parseDouble(messages[3]);
                RootObject.add(new Player(x, y, name, root.getCamera(), root));
                if (name.equalsIgnoreCase(root.getName())) {
                    JOptionPane.showMessageDialog(null, "같은 이름의 플레이어가 서버에 있습니다.", "InMinic Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
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

            System.out.println("RECV " + message);

            if (messages[0].equalsIgnoreCase("connect")) {
                if (!messages[1].equalsIgnoreCase(root.getCharacter().getName())) {
                    double x = Double.parseDouble(messages[2]);
                    double y = Double.parseDouble(messages[3]);
                    RootObject.add(new Player(x, y, messages[1], root.getCamera(), root));
                }
            } else if (messages[0].equalsIgnoreCase("disconnect")) {
                String name = messages[2];

                root.findPlayerByName(name).destroy();
            } else if (messages[0].equalsIgnoreCase("move")) {
                String name = messages[1];
                if (!name.equalsIgnoreCase(root.getCharacter().getName())) {
                    double x = Double.parseDouble(messages[2]);
                    double y = Double.parseDouble(messages[3]);
                    boolean watchingRight = Boolean.parseBoolean(messages[4]);
                    int form = Integer.parseInt(messages[5]);

                    Player player = root.findPlayerByName(name);
                    player.setX(x);
                    player.setY(y);
                    player.setWatchingRight(watchingRight);
                    player.setForm(form);
                }
            }
        }
    }
}
