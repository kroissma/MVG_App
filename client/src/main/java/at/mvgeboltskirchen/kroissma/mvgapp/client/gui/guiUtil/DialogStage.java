package at.mvgeboltskirchen.kroissma.mvgapp.client.gui.guiUtil;

import at.mvgeboltskirchen.kroissma.mvgapp.client.MvgAppClientApplication;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogStage extends Stage {

    public DialogStage(Stage parent, String title) {
        setTitle(title);
        initModality(Modality.WINDOW_MODAL);
        initOwner(parent);
        setResizable(false);
        getIcons().add(new Image(
            MvgAppClientApplication.class.getResourceAsStream("/image/ticketlineIcon.png")));
    }
}
