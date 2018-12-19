package prj.shtelo.inminic.client.telecommunication;

import prj.shtelo.inminic.client.Root;
import prj.shtelo.inminic.client.rootobject.RootObject;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Client {
    private boolean connected = false;

    private String host;
    private int port;
    private Root root;

    private PrintStream printStream;
    private ClientThread clientThread;

    public Client(Root root) {
        this.root = root;
    }

    public Client(String host, int port, Root root) throws IOException {
        this.host = host;
        this.port = port;
        this.root = root;

        init();
    }

    private void init() throws IOException {
        Socket socket;
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            return;
        }
        connected = true;

        printStream = new PrintStream(socket.getOutputStream());

        send("playerName\t" + root.getCharacter().getName());

        clientThread = new ClientThread(socket, this, root);
        clientThread.start();
    }

    public void send(String message) {
        printStream.println(message);
    }

    public boolean connect(String host, int port) {
        disconnect();

        this.host = host;
        this.port = port;

        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }

        return false;
    }

    public boolean connect(String host) {
        return connect(host, 1010);
    }

    public void disconnect() {
        if (!connected)
            return;
        connected = false;
        printStream.close();
        try {
            clientThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        changeMapName("test001");
        root.getCharacter().setX(0);
        root.getCharacter().setY(0);
        RootObject.sweep();
    }

    public void chatting(String message) {
        send("chatting\t" + message);
    }

    public boolean isConnected() {
        return connected;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    void changeMapName(String mapName) {
        root.getMap().changeMapName(mapName);
    }
}
