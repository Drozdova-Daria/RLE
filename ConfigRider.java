package com.company;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

enum ConfigGrammar {
    INPUT_FILE,
    OUTPUT_FILE,
    LENGTH_SEQUENCE,
    BUFFER_SIZE,
    ACTION;
    public static final String delimiter = " = ";
}

public class ConfigRider {
    private String input, output, action;
    private int lengthSequence, bufferSize;

    ConfigRider(String fileName) {
        try {
            FileReader reader = new FileReader(fileName);
            Scanner config = new Scanner(reader);
            while (config.hasNextLine()) {
                String[] parameter = config.nextLine().split(ConfigGrammar.delimiter);
                ReadParameters(parameter);
            }
            if (bufferSize < lengthSequence) {
                Errors.ERROR_CONFIG_LENGTH_BUFFER.PrintErrorInLog();
            }
        } catch (IOException exception) {
            Errors.ERROR_CONFIG_FILE_OPENING.PrintErrorInLog();
        }
    }

    private void ReadParameters(String[] parameter) {
        if (parameter.length != 2) {
            Errors.ERROR_CONFIG_PARAMETERS_LENGTH.PrintErrorInLog();
        } else {
            switch (ConfigGrammar.valueOf(parameter[0])) {
                case INPUT_FILE:
                    input = parameter[1];
                    break;
                case OUTPUT_FILE:
                    output = parameter[1];
                    break;
                case LENGTH_SEQUENCE:
                    lengthSequence = parseInt(parameter[1]);
                    break;
                case BUFFER_SIZE:
                    bufferSize = parseInt(parameter[1]);
                    break;
                case ACTION:
                    action = parameter[1];
                    break;
                default:
                    Errors.ERROR_CONFIG_GRAMMAR.PrintErrorInLog();
                    break;
            }
        }
    }

    String GetInputFile() { return input; }
    String GetOutputFile() { return output; }
    int GetLengthSequence() { return lengthSequence; }
    int GetBufferSize() { return bufferSize; }
    String GetAction() { return action; }
}
