package at.ac.tuwien.inso.sepm.ticketline.client.gui.guiUtil;

public class DurationUtil {

    public static String formatDuration(int duration) {
        int days = 0;
        int hours = duration / 60;
        int minutes = duration % 60;

        if (hours >= 24) {
            days = hours / 24;
            hours = hours % 24;
        }

        if (days >= 1) {
            return String.format("%02d", days) + "d " + String.format("%02d", hours) + "h " + String
                .format("%02d", minutes) + "m";
        } else {
            return String.format("%02d", hours) + "h " + String.format("%02d", minutes) + "m";
        }
    }
}
