package com.company;

public class Main {
    public static void main(String[] args) {
        if(args.length == 1) {
            String configFile = args[0];
            ConfigRider config = new ConfigRider(configFile);
            RLE rle = new RLE(config);
            rle.RunLengthEncoding();
        } else {
            Errors.ERROR_COMMAND_LINE_ARGS.PrintErrorInLog();
        }
    }
}
