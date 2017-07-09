package at.ac.tuwien.inso.sepm.ticketline.client.gui.guiUtil;


import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * This class provides helper methods for JavaFX.
 */
public class DialogUtil {

    /**
     * An empty private constructor to prevent the creation of an DialogUtil Instance.
     */
    private DialogUtil() {
    }


    /**
     * Creates a new Alert Dialog and shows it on the screen until the user clicks accept, cancel or
     * close (depending of the type of the Alert).
     *
     * @param type the type of the alert (@see Alert.AlertType)
     * @param parent the owner of the dialog
     * @param title the title of the dialog
     * @param headerText the header text of the dialog
     * @param contentText the content of the dialog
     */
    public static Alert showDialog(Alert.AlertType type, Stage parent, String title,
        String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.initOwner(parent);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
        return alert;
    }

    /**
     * Creates a new Alert Dialog and shows it on the screen until the user clicks accept, cancel or
     * close (depending of the type of the Alert).
     *
     * @param type the type of the alert (@see Alert.AlertType)
     * @param parent the owner of the dialog
     * @param title the title of the dialog
     * @param headerText the header text of the dialog
     * @param contentText the content of the dialog
     * @param buttonTypes a list of buttons
     */
    public static Alert showDialog(Alert.AlertType type, Stage parent, String title,
        String headerText, String contentText, ButtonType... buttonTypes) {
        Alert alert = new Alert(type);
        alert.initOwner(parent);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.getButtonTypes().setAll(buttonTypes);
        alert.showAndWait();
        return alert;
    }

    /**
     * Creates a new Warning Dialog (@see Alert.AlertType.WARNING) and shows it on the screen until
     * the user clicks accept or close. The title is "dialog.text.nothingSelected" The header text is
     * "dialog.text.noItemSelected" The content is "dialog.text.pleaseSelectItem"
     *
     * @param parent the owner of the dialog
     */
    public static void noItemSelectedDialog(Stage parent) {
        DialogUtil.showDialog(Alert.AlertType.WARNING, parent,
            BundleManager.getBundle().getString("dialog.text.nothingSelected"),
            BundleManager.getBundle().getString("dialog.text.noItemSelected"),
            BundleManager.getBundle().getString("dialog.text.pleaseSelectItem"));
    }

    /**
     * Creates a new Alert Dialog (@see Alert.AlertType.WARNING) and shows it on the screen until the
     * user clicks clicks accept, cancel or close.
     *
     * @param stage the owner of the dialog
     * @return true if the user clicked the OK button
     */
    public static boolean reallyDeleteDialog(Stage stage) {
        return confirmDialog(stage,
            BundleManager.getBundle().getString("dialog.title.reallyDelete"),
            BundleManager.getBundle().getString("dialog.headerText.reallyDelete"),
            BundleManager.getBundle().getString("dialog.text.reallyDelete"));
    }

    /**
     * Creates a dialog for an exception. Based on <a href="http://code.makery.ch/blog/javafx-dialogs-official/">http://code.makery.ch/blog/javafx-dialogs-official/</a>
     *
     * @param throwable the throwable
     * @return the dialog which shows the exception
     */
    public static Alert exceptionDialog(Throwable throwable, Window parentWindow, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        if (parentWindow != null) {
            alert.initOwner(parentWindow);
            alert.initModality(Modality.WINDOW_MODAL);
        }
        alert.setTitle(BundleManager.getExceptionBundle().getString("error"));
        alert.setHeaderText(BundleManager.getExceptionBundle().getString("exception.unexpected"));
        alert.setContentText(message);

        ButtonType ok = new ButtonType(BundleManager.getBundle().getString("button.ok"),
            ButtonData.OK_DONE);

        Hyperlink detailsButton = (Hyperlink) alert.getDialogPane().lookup(".details-button");

        //detailsButton.setText( "asd" );

        alert.getButtonTypes().setAll(ok);
        Label label = new Label(
            BundleManager.getExceptionBundle().getString("exception.stacktrace"));
        TextArea textArea = new TextArea(stacktraceToString(throwable));
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);
        return alert;
    }

    private static String stacktraceToString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * Creates a standard error dialog.
     *
     * @param contentText of the error dialog
     * @param parentWindow or null if not modal
     * @return the dialog which shows the error
     */
    public static Alert errorDialog(String contentText, Window parentWindow) {
        Alert alert = new Alert(AlertType.ERROR);
        if (parentWindow != null) {
            alert.initOwner(parentWindow);
            alert.initModality(Modality.WINDOW_MODAL);
        }
        alert.setTitle(BundleManager.getExceptionBundle().getString("error"));
        alert.setHeaderText(BundleManager.getExceptionBundle().getString("exception.unexpected"));
        alert.setContentText(contentText);

        ButtonType ok = new ButtonType(BundleManager.getBundle().getString("button.ok"),
            ButtonData.OK_DONE);

        alert.getButtonTypes().setAll(ok);
        return alert;
    }

    public static boolean confirmDialog(Stage parent, String title, String headerText,
        String contentText) {

        ButtonType ok = new ButtonType(BundleManager.getBundle().getString("button.ok"),
            ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType(BundleManager.getBundle().getString("button.cancel"),
            ButtonData.CANCEL_CLOSE);

        Alert alert = DialogUtil
            .showDialog(AlertType.CONFIRMATION, parent, title, headerText, contentText, ok, cancel);

        return alert.getResult().equals(ok);
    }

    public static boolean cancelDialog(Stage parent) {
        ResourceBundle bundle = BundleManager.getBundle();

        ButtonType ok = new ButtonType(BundleManager.getBundle().getString("button.ok"),
            ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType(BundleManager.getBundle().getString("button.cancel"),
            ButtonData.CANCEL_CLOSE);

        Alert alert = DialogUtil.showDialog(
            AlertType.CONFIRMATION,
            parent,
            bundle.getString("dialog.confirm.cancel.title"),
            bundle.getString("dialog.confirm.cancel.headerText"), null, ok, cancel);
        return alert.getResult().equals(ok);
    }
}
