package networkjoy.integration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import networkjoy.util.RecievedJoyData;


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



    public RecievedJoyData recieveData() throws IOException {
        output.writeInt(73239); // Write a specific int to signify ready to recieve.
        int buttonInputs = input.readUnsignedByte();
        int axisInputs = input.readUnsignedByte();
        int povInputs = input.readUnsignedByte();
        RecievedJoyData recievedData = new RecievedJoyData(buttonInputs, axisInputs, povInputs);

        for (int i = 0; i < buttonInputs; i++) {
            recievedData.getButtonDatas()[i] = input.readBoolean();
        }
        for (int i = 0; i < axisInputs; i++) {
            recievedData.getAxisDatas()[i] = input.readInt();
        }
        input.read(recievedData.getPovDatas());

        return recievedData;
    }
        public void stopConnection() throws IOException {
        this.socket.close();
    }

}
