package at.mvgeboltskirchen.kroissma.mvgapp.client.gui.guiUtil;


import javafx.scene.control.TextField;

public class ValidationUtil {

    /**
     * An empty private constructor to prevent the creation of an ValidationUtil Instance.
     */
    private ValidationUtil() {
    }

    /**
     * Checks if the input String of the textfield is a valid integer.
     * Especially checks if the textfield is not null and doesn't consists of whitespaces only
     *
     * @param textfield the textfield where the text to be checked is extracted
     * @return true if the input is a valid int
     */
    public static boolean isValidInt(TextField textfield) {
        return textfield != null && isValidInt(textfield.getText());
    }

    /**
     * Checks if the input String is a valid integer.
     * Especially checks if the string is not null and doesn't consists of whitespaces only
     *
     * @param text the text to be checked
     * @return true if the input String is a valid int
     */
    public static boolean isValidInt(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the input String of the textfield is a valid.
     * Especially checks if the textfield is not null and doesn't consists of whitespaces only
     *
     * @param textfield the textfield where the text to be checked is extracted
     * @return true if the input is a not null and don't consists of whitespaces only
     */
    public static boolean isValidText(TextField textfield) {
        return textfield != null && isValidText(textfield.getText());
    }

    /**
     * Checks if the input String is valid.
     * Especially checks if the string is not null and doesn't consists of whitespaces only
     *
     * @param text the text to be checked
     * @return true if the input is a not null and doesn't consists of whitespaces only
     */
    public static boolean isValidText(String text) {
        return !(text == null || text.trim().isEmpty());
    }
}
