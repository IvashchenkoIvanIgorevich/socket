package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandReader {

    private static final String COMMANDS_FILE_NAME = "Commands.txt";
    private static final char COMMANDS_DELIMITER = ':';
    private final File COMMANDS_FILE;
    private final Map<String, String> commands = new HashMap<>();

    public CommandReader() {
        COMMANDS_FILE = new File(CommandReader.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        readFromFile();
    }

    public String getCommandText(String command) {
        return commands.get(command);
    }

    public boolean isCommand(String message) {
        return commands.containsKey(message);
    }

    private void readFromFile() {
        String commandsFilePath = COMMANDS_FILE.getParentFile().getAbsolutePath() + File.separator + COMMANDS_FILE_NAME;
        try (BufferedReader br = new BufferedReader(new FileReader(commandsFilePath))) {
            String text;

            while ((text = br.readLine()) != null) {
                List<String> command = splitTextByDelimiter(text);
                commands.put(command.get(0), command.get(1));
            }
        } catch (FileNotFoundException e) {
            MessageLogger.logMessage("File not found." + e.getMessage());
        } catch (IOException e) {
            MessageLogger.logMessage(e.getMessage());
        } finally {
            MessageLogger.saveLoggedMessage();
        }
    }

    private List<String> splitTextByDelimiter(String text) {
        String[] parts = text.split(String.valueOf(COMMANDS_DELIMITER));
        List<String> result = new ArrayList<>();
        if (parts.length > 1) {
            result.add(parts[0]);
            result.add(parts[1]);
        }
        return result;
    }
}
