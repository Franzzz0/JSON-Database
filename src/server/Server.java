package server;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ServerSocket server;
    private final DataBase dataBase;
    private final ExecutorService executor;

    public Server(File dbFile) {
        try {
            String address = "127.0.0.1";
            int port = 23456;
            server = new ServerSocket(port, 50, InetAddress.getByName(address));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.dataBase = new DataBase(dbFile);
        this.executor = Executors.newFixedThreadPool(4);
        start();
    }

    public void start() {
        System.out.println("Server started!");
        while (!server.isClosed()) {
            Socket socket;
            try {
                socket = server.accept();
            } catch (IOException e) {
                break;
            }
            executor.submit(new RequestHandler(this, this.dataBase, socket));
        }
    }

    public void shutdown() {
        try {
            executor.shutdownNow();
            dataBase.save();
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
