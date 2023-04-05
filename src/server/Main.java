package server;

import java.io.File;

public class Main {
    private final static String DATABASE_PATH = ".\\src\\server\\data\\db.json";
//    private final static String DATABASE_PATH = ".\\JSON Database (Java)\\task\\src\\server\\data\\db.json";

    public static void main(String[] args) {
        File dbFile = new File(DATABASE_PATH);
        new Server(dbFile);
    }
}
