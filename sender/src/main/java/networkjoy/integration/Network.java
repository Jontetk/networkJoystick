package networkjoy.integration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Network {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public Network(boolean server, String hostname, int port) throws IOException {
        if (server) {
            ServerSocket serverSocket = new ServerSocket(port);
            this.socket = serverSocket.accept();
            serverSocket.close();

        } else {
            this.socket = new Socket(InetAddress.getByName(hostname), port);
        }
        this.input = new DataInputStream(this.socket.getInputStream());
        this.output = new DataOutputStream(this.socket.getOutputStream());

    }

    public void sendJoyData(boolean[] buttonDatas, int[] axisDatas, byte[] povDatas) throws IOException {
        while (input.readInt() != 73239) {
        } // Block until reciever is ready
        output.writeByte(buttonDatas.length);
        output.writeByte(axisDatas.length);
        output.writeByte(povDatas.length);

        for (boolean button : buttonDatas) {
            output.writeBoolean(button);
        }
        for (int axis : axisDatas) {
            output.writeInt(axis);
        }
        output.write(povDatas);

    }

    public void stopConnection() throws Exception {
        this.socket.close();
    }
}
