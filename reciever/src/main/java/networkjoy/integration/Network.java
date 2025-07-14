package networkjoy.integration;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import networkjoy.util.RecievedJoyData;


public class Network {
    private Socket socket;
    private DataInputStream socketInput;
    private DataOutputStream socketOutput;

    public Network(boolean server, String hostname, int port) throws IOException {
        if (server) {
            ServerSocket serverSocket = new ServerSocket(port);
            this.socket = serverSocket.accept();
            serverSocket.close();

        } else {
            this.socket = new Socket(InetAddress.getByName(hostname), port);
        }
        this.socketInput = new DataInputStream(this.socket.getInputStream());
        this.socketOutput = new DataOutputStream(this.socket.getOutputStream());

    }



    public RecievedJoyData recieveData() throws IOException {
        socketOutput.writeByte(239); // Write a specific byte to signify ready to recieve.
        //int dataLength = socketInput.readInt();
        
        int buttonInputs = socketInput.readUnsignedByte();
        int axisInputs = socketInput.readUnsignedByte();
        int povInputs = socketInput.readUnsignedByte();
        RecievedJoyData recievedData = new RecievedJoyData(buttonInputs, axisInputs, povInputs);

        for (int i = 0; i < buttonInputs; i++) {
            recievedData.getButtonDatas()[i] = socketInput.readBoolean();
        }
        for (int i = 0; i < axisInputs; i++) {
            recievedData.getAxisDatas()[i] = socketInput.readInt();
        }
        socketInput.read(recievedData.getPovDatas());

        return recievedData;
    }
        public void stopConnection() throws IOException {
        this.socket.close();
    }

}
