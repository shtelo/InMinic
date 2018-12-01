package prj.shtelo.inminic.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {
    private final static int PORT = 1010;
    boolean running = false;

    String mapName = "test001";
    private ArrayList<ServerThread> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        new Main().startServer();
    }

    private void startServer() throws IOException {
        if (running)
            return;
        running = true;

        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("로컬 서버가 포트 " + PORT + "에서 열렸습닏뎨ㅔ에ㅔ~~");

        while (running) {
        Socket socket = serverSocket.accept();

        new ServerThread(socket, this).start();
    }
}

    void announce(String message) {
        for (ServerThread client : clients) {
            client.getPrintStream().println(message);
        }
    }

    ArrayList<ServerThread> getClients() {
        return clients;
    }
}
