package networkjoy.integration;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Network {
    private Socket socket;
    private DataInputStream socketInput;
    private DataOutputStream socketOutput;
    private ByteArrayOutputStream bArrayOutputStream;


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
        this.bArrayOutputStream = new ByteArrayOutputStream();
        


    }

    public void sendJoyData(boolean[] buttonDatas, int[] axisDatas, byte[] povDatas) throws IOException {
        while (socketInput.readUnsignedByte() != 239) {
        } // Block until reciever is ready
        bArrayOutputStream.reset();
        DataOutputStream output = new DataOutputStream(bArrayOutputStream);
        //output.writeInt(3+buttonDatas.length+4*axisDatas.length+povDatas.length);
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

        socketOutput.write(bArrayOutputStream.toByteArray());
    }

    public void stopConnection() throws IOException {
        this.socket.close();
    }
}
