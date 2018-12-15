package prj.shtelo.inminic.client.telecommunication;

import prj.shtelo.inminic.client.Root;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Client {
    private boolean connected = false;

    private String host;
    private int port;
    private Root root;

    private PrintStream printStream;

    private String mapName;

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
            JOptionPane.showMessageDialog(null, "서버에 연결할 수 없습니다!\n오프라인 모드로 계속합니다.", "InMinic Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        connected = true;

        printStream = new PrintStream(socket.getOutputStream());

        ClientThread clientThread = new ClientThread(socket, this, root);
        clientThread.start();
    }

    public void send(String message) {
        System.out.println("SEND " + message);
        printStream.println(message);
    }

    public boolean isConnected() {
        return connected;
    }

    public String getMapName() {
        return mapName;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getIP() {
        return host + ':' + port;
    }

    void setMapName(String mapName) {
        this.mapName = mapName;
    }
}
