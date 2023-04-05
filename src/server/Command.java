package server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class Command {
    private final String type;
    private final JsonElement key;
    private final JsonElement value;

    public Command(String type, JsonArray key, JsonElement value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public CommandType getType() {
        return switch (type.toUpperCase()) {
            case "EXIT" -> CommandType.EXIT;
            case "SET" -> CommandType.SET;
            case "GET" -> CommandType.GET;
            case "DELETE" -> CommandType.DELETE;
            default -> throw new RuntimeException("Unsupported type parameter: " + type);
        };
    }

    public JsonElement getKey() {
        return key;
    }

    public JsonElement getValue() {
        return value;
    }
}
