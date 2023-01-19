package org.example.services;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerService {

    private static final Logger logger = Logger.getLogger(LoggerService.class.getName());

    private static final String pattern = "order-book-snapshots-log";

    static {
        try {
            FileHandler fileHandler = new FileHandler(pattern);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeLogToFile(String text) {
        logger.info(text);
    }
}
