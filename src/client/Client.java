package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private final String message;

    public Client(String message) {
        this.message = message;
        start();
    }

    private void start() {
        int PORT = 23456;
        String ADDRESS = "127.0.0.1";
        try (Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");
            output.writeUTF(message);
            System.out.println("Sent: " + message);
            String response = input.readUTF();
            System.out.println("Received: " + response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
