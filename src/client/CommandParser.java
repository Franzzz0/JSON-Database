package client;

import com.beust.jcommander.JCommander;

public class CommandParser {
    private final String[] argv;
    private final Args args;

    public CommandParser(String[] argv) {
        this.args = new Args();
        this.argv = argv;
    }

    public String parseParameters() {
        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse(argv);
        return getMessage();
    }

    public String getFileName() {
        for (int i = 0; i < argv.length; i++) {
            if ("-in".equals(argv[i]) && argv.length > i + 1) {
                return argv[i + 1];
            }
        }
        return null;
    }

    public boolean isFileProvided() {
        return getFileName() != null;
    }

    private String getMessage() {
        String type = String.format("\"type\":%s", args.getType());
        String key = "".equals(args.getKey()) ? "" : String.format(",\"key\":\"%s\"", args.getKey());
        String value = "".equals(args.getValue()) ? "" : String.format(",\"value\":\"%s\"", args.getValue());

        return String.format("{%s%s%s}", type, key, value);
    }
}
