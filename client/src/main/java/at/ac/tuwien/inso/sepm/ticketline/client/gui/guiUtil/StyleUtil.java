package at.ac.tuwien.inso.sepm.ticketline.client.gui.guiUtil;


import java.util.Random;
import javafx.scene.paint.Color;

public class StyleUtil {

    /**
     * Generates a random Color. Each value r,g,b can be 0 to 255.
     *
     * @return the generated Color
     */
    public static Color generateRandomColor() {
        Random random = new Random();
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();
        java.awt.Color c = new java.awt.Color(r, g, b);
        return Color.rgb(c.getRed(), c.getGreen(), c.getBlue());
    }

    /**
     * Converts a color of rgb to a hex value.
     *
     * @param c the color that should be converted
     * @return the hex String with prefix "#" and then 6 digits for the color
     */
    public static String colorToHexString(Color c) {
        int green = (int) (c.getGreen() * 255);
        String greenString = Integer.toHexString(green);
        greenString = appendZeroPrefix(greenString);

        int red = (int) (c.getRed() * 255);
        String redString = Integer.toHexString(red);
        redString = appendZeroPrefix(redString);

        int blue = (int) (c.getBlue() * 255);
        String blueString = Integer.toHexString(blue);
        blueString = appendZeroPrefix(blueString);

        return "#" + redString + greenString + blueString;
    }

    /**
     * a helper method to append a zero prefix to the hex String if it contains of
     * 1 element only
     *
     * @param hexValue the hexValue to check and maybe append
     * @return the same hex value, always consisting of 2 zeros, maybe with a 0 prefix
     */
    private static String appendZeroPrefix(String hexValue) {
        StringBuilder sb = new StringBuilder();
        if (hexValue.length() == 1) {
            sb.append("0");
        }
        sb.append(hexValue);
        return sb.toString();
    }

    /**
     * Formats the passed value into currency with 2 decimals and leading "€" Symbol.
     *
     * @param value the value that should be formatted
     * @return the passed value formatted as Currency String
     */
    public static String formatCurrency(double value) {
        return String.format("€ %.2f", value / 100);
    }
}
