package server;

import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataBase {
    private final File databaseFile;
    private final Gson gson;
    private final Lock readLock;
    private final Lock writeLock;
    private JsonObject root;

    public DataBase(File file) {
        this.databaseFile = file;
        this.gson = new GsonBuilder().create();
        ReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
        readFile();
    }

    public String get(JsonElement key) {
        readFile();
        if (key.isJsonArray()) {
            return deepGet(root, key.getAsJsonArray(), false).toString();
        } else {
            return root.get(key.getAsString()).toString();
        }
    }

    public String remove(JsonElement key) {
        return writeToFile(CommandType.DELETE, key, null);
    }

    public void set(JsonElement key, JsonElement value) {
        writeToFile(CommandType.SET, key, value);
    }

    public void save() {
        writeToFile(null, null, null);
    }

    private JsonElement deepGet(JsonElement root, JsonArray array, boolean excludeLast) {
        int size = excludeLast ? array.size() - 1 : array.size();
        for (int i = 0; i < size; i ++) {
            if (root.isJsonObject()) {
                String key = array.get(i).getAsString();
                if (!root.getAsJsonObject().has(key)) {
                    JsonObject object = new JsonObject();
                    root.getAsJsonObject().add(key, object);
                }
                root = root.getAsJsonObject().get(key);
            }
        }
        return root;
    }

    private String writeToFile(CommandType type, JsonElement keyElement, JsonElement value) {
        writeLock.lock();
        String result = null;
        if (type != null) {
            JsonObject parent;
            String key;
            if (keyElement.isJsonArray()) {
                JsonArray array = keyElement.getAsJsonArray();
                key = array.get(array.size() - 1).getAsString();
                parent = deepGet(root, array, true).getAsJsonObject();
            } else {
                key = keyElement.getAsString();
                parent = root;
            }
            if (type.equals(CommandType.SET)) {
                parent.add(key, value);
            } else if (type.equals(CommandType.DELETE)) {
                result = parent.remove(key).toString();
            }
        }
        String jsonDatabase = gson.toJson(root);
        try (FileWriter writer = new FileWriter(databaseFile)) {
            writer.write(jsonDatabase);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writeLock.unlock();
        return result;
    }

    private void readFile() {
        if (this.databaseFile.exists()) {
            readLock.lock();
            String jsonDatabase;
            try {
                jsonDatabase = new String(Files.readAllBytes(Paths.get(databaseFile.getPath())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                readLock.unlock();
            }
            this.root = gson.fromJson(jsonDatabase, JsonObject.class);
        } else {
            this.root = new JsonObject();
        }
    }
}
