package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageLogger {

    private static final File LOG_FILE;
    private static final String LOG_FILE_NAME = "LogFile.txt";
    private static final List<StringBuilder> loggedData = new ArrayList<>();

    static {
        LOG_FILE = new File(MessageLogger.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    }

    public static void logMessage(String message) {
        loggedData.add(new StringBuilder(message));
    }

    public static void saveLoggedMessage() {
        String logFilePath = LOG_FILE.getParentFile().getAbsolutePath() + File.separator + LOG_FILE_NAME;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
            writer.write(getLoggedMessages());
            writer.newLine();
        } catch (IOException e) {
            // TODO: should be handle
            e.printStackTrace();
        }
    }

    private static String getLoggedMessages() {
        return loggedData.stream().map(message -> message + "\n").collect(Collectors.joining());
    }
}
