package com.company;
import java.util.logging.Logger;
import java.util.logging.Level;

enum Errors {
    //File errors
    ERROR_CONFIG_FILE_OPENING("Configuration file opening error"),
    ERROR_INPUT_FILE_OPENING("Input file opening error"),
    ERROR_OUTPUT_FILE_OPENING("Output file opening error"),
    //Command line errors
    ERROR_COMMAND_LINE_ARGS("Invalid argument in command line"),
    //Configuration file errors
    ERROR_CONFIG_PARAMETERS_LENGTH("One of the configuration parameters is set incorrectly"),
    ERROR_CONFIG_GRAMMAR("An undefined parameter was encountered in the configuration file"),
    ERROR_CONFIG_LENGTH_BUFFER("The buffer length is less than the sequence length"),
    //Sequence length errors
    ERROR_LENGTH_TYPE("In configuration file sequence length isn't written in int type"),
    ERROR_LENGTH_LIMIT("The sequence length value in the configuration file doesn't fall within the segment [1,16]"),
    //Errors in the data input file
    ERROR_EMPTY_SEQUENCE("Sequence is empty (empty input file)");

    private final Logger log = Logger.getLogger(Errors.class.getName());
    private final String message;
    Errors(String message) {
        this.message = message;
    }
    void PrintErrorInLog() {
        log.log(Level.WARNING, message);
    }
}
