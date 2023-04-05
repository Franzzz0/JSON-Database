package client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    private static final String PATH = ".\\src\\client\\data\\";
//    private static final String PATH = ".\\JSON Database (Java)\\task\\src\\client\\data\\";

    public static void main(String[] args) {
        CommandParser parser = new CommandParser(args);
        String message;
        if (parser.isFileProvided()) {
            message = readFile(parser.getFileName());
        } else {
            message = parser.parseParameters();
        }
        new Client(message);
    }

    private static String readFile(String fileName) {
        String filePath = PATH + fileName;
        String fileContent;
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileContent;
    }
}
