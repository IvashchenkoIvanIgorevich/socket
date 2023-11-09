package controller;

public class StringHelper {

    private static final int FACTOR = 1000;

    public static String modifyText(String text) {
        StringBuilder result = new StringBuilder();

        if (isNumeric(text)) {
            int number = Integer.parseInt(text);
            return Integer.toString(number * FACTOR);
        }

        char[] characters = text.toCharArray();
        boolean toUpperCase = true;
        for (char character : characters) {
            if (Character.isSpaceChar(character)) {
                result.append('_');
                continue;
            }
            if (Character.isLetter(character)) {
                result.append(toggleCase(character, toUpperCase));
                toUpperCase = !toUpperCase;
            } else {
                result.append(character);
            }
        }

        return result.toString();
    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+");
    }

    private static char toggleCase(char letter, boolean toUpperCase) {
        if (toUpperCase) {
            return Character.toUpperCase(letter);
        }
        return Character.toLowerCase(letter);
    }
}
