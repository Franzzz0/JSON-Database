package server;

import com.google.gson.JsonElement;

public class DataBaseHandler{

    public static String processCommand(DataBase dataBase, Command command) {
        JsonElement key = command.getKey();
        return switch (command.getType()) {
            case GET -> get(dataBase, key);
            case SET -> set(dataBase, key, command.getValue());
            case DELETE -> delete(dataBase, key);
            case EXIT -> Response.OK;
        };
    }

    private static String get(DataBase dataBase, JsonElement key) {
        String value;
        try {
            value = dataBase.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR;
        }
        if (value == null) {
            return Response.NO_SUCH_KEY;
        } else {
            return String.format(Response.RESPONSE_VALUE_TEMPLATE, value);
        }
    }

    private static String set(DataBase dataBase, JsonElement key, JsonElement value) {
        try {
            dataBase.set(key, value);
        } catch (Exception e) {
            return Response.ERROR;
        }
        return Response.OK;
    }

    private static String delete(DataBase dataBase, JsonElement key) {
        boolean removed;

        try {
            removed = dataBase.remove(key) != null;
        } catch (Exception e) {
            return Response.ERROR;
        }

        if (removed) {
            return Response.OK;
        } else {
            return Response.NO_SUCH_KEY;
        }
    }
}
