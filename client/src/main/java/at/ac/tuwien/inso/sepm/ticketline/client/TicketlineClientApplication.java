package at.ac.tuwien.inso.sepm.ticketline.client;

import at.ac.tuwien.inso.sepm.ticketline.client.configuration.properties.JavaFxConfigurationProperties;
import at.ac.tuwien.inso.sepm.ticketline.client.preloader.AppPreloader;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.springfx.SpringFxApplication;
import com.sun.javafx.application.LauncherImpl;
import java.util.Optional;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"at.ac.tuwien.inso.sepm.ticketline.client",
    "at.ac.tuwien.inso.springfx"})
public class TicketlineClientApplication extends SpringFxApplication {

    private Stage mainStage;

    @Autowired
    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    // Suppress warning cause sadly it seems that there is no nice way of doing this without field injection here
    private JavaFxConfigurationProperties javaFxConfigurationProperties;

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        LauncherImpl.launchApplication(TicketlineClientApplication.class, AppPreloader.class, args);
    }

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        stage.setTitle(javaFxConfigurationProperties.getTitle());
        stage.setScene(new Scene(
            loadParent("/fxml/mainWindow.fxml"),
            javaFxConfigurationProperties.getInitialWidth(),
            javaFxConfigurationProperties.getInitialHeight()
        ));
        stage.getIcons().add(new Image(
            TicketlineClientApplication.class.getResourceAsStream("/image/ticketlineIcon.png")));
        stage.centerOnScreen();
        stage.show();
        stage.setOnCloseRequest(event -> {

            ButtonType ok = new ButtonType(BundleManager.getBundle().getString("button.ok"),
                ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType(BundleManager.getBundle().getString("button.cancel"),
                ButtonData.CANCEL_CLOSE);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(stage);
            alert.setTitle(BundleManager.getBundle().getString("dialog.exit.title"));
            alert.setHeaderText(BundleManager.getBundle().getString("dialog.exit.header"));
            alert.setContentText(BundleManager.getBundle().getString("dialog.exit.content"));
            alert.getButtonTypes().setAll(ok, cancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (!result.isPresent() || !ok.equals(result.get())) {
                event.consume();
            }
        });
    }

    public Stage getMainStage() {
        return mainStage;
    }
}
