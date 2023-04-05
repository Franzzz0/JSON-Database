package client;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names = "-t", description = "type")
    private String type = "";

    @Parameter(names = "-k", description = "key")
    private String key = "";

    @Parameter(names = "-v", description = "value")
    private String value = "";

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
