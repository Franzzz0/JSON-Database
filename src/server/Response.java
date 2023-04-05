package server;

public class Response {
    public final static String OK = "{\"response\":\"OK\"}";
    public final static String ERROR = "{\"response\":\"ERROR\"}";
    public final static String NO_SUCH_KEY = "{\"response\":\"ERROR\",\"reason\":\"No such key\"}";
    public final static String RESPONSE_VALUE_TEMPLATE = "{\"response\":\"OK\",\"value\":%s}";
}
