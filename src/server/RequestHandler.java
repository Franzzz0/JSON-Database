package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RequestHandler extends Thread {
    private final Server server;
    private final DataBase dataBase;
    private final Socket socket;
    private final Gson gson;

    public RequestHandler(Server server, DataBase dataBase, Socket socket) {
        this.server = server;
        this.dataBase = dataBase;
        this.socket = socket;
        gson = new GsonBuilder().create();
    }

    @Override
    public void run() {
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String messageReceived = input.readUTF();
            System.out.println("Received: " + messageReceived);
            Command command = gson.fromJson(messageReceived, Command.class);
            String response = DataBaseHandler.processCommand(this.dataBase, command);
            output.writeUTF(response);
            if (command.getType().equals(CommandType.EXIT)) {
                server.shutdown();
                socket.close();
            }
            System.out.println("Sent: " + response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
