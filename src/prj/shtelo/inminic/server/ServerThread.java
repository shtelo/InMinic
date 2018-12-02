package prj.shtelo.inminic.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

class ServerThread extends Thread {
    private Socket socket;
    private Main server;

    private Scanner scanner;
    private PrintStream printStream;
    private Player player;

    ServerThread(Socket socket, Main server) {
        this.socket = socket;
        this.server = server;
    }

    private void connect() {
        System.out.println(socket.getLocalSocketAddress() + "이(가) 서버에 접속했습니다.");

        printStream.println("serverInfo\t" + server.mapName + "\t" + server.getClients().size());

        for (ServerThread serverThread : server.getClients()) {
            printStream.println("player\t" + serverThread.getPlayer().toString());
        }

        String[] playerInfos = scanner.nextLine().split("\t");

        player = new Player(0, 0, playerInfos[1]);

        server.announce("connect\t" + player.toString());

        server.getClients().add(this);
    }

    private void disconnect() {
        System.out.println(socket.getLocalSocketAddress() + "이(가) 서버에서 퇴장했습니다.");
        server.announce("disconnect\t" + player.toString());
        server.getClients().remove(this);
    }

    @Override
    public void run() {
        connect();

        String message;
        String[] messages;
        while (server.running) {
            try {
                message = scanner.nextLine();
            } catch (NoSuchElementException e) {
                break;
            }

            messages = message.split("\t");

            if (messages[0].equalsIgnoreCase("move")) {
                player.setX(Double.parseDouble(messages[1]));
                player.setY(Double.parseDouble(messages[2]));
                player.setWatchingRight(Boolean.parseBoolean(messages[3]));
                server.announce(message + "\t" + player.getName());
            }

            System.out.println(socket.getLocalSocketAddress() + ": " + message);
        }

        disconnect();
    }

    @Override
    public synchronized void start() {
        try {
            scanner = new Scanner(socket.getInputStream());
            printStream = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.start();
    }

    private Player getPlayer() {
        return player;
    }

    PrintStream getPrintStream() {
        return printStream;
    }
}
