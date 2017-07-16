package at.mvgeboltskirchen.kroissma.mvgapp.client.gui;

import at.mvgeboltskirchen.kroissma.mvgapp.client.util.BundleManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TabHeaderController {

    private static final int HEADER_ICON_SIZE = 25;
    private final FontAwesome fontAwesome;
    @FXML
    private Label lblHeaderIcon;
    @FXML
    private Label lblHeaderTitle;

    public TabHeaderController(FontAwesome fontAwesome) {
        this.fontAwesome = fontAwesome;
    }

    public void setIcon(FontAwesome.Glyph glyph) {
        lblHeaderIcon.setGraphic(
            fontAwesome
                .create(glyph)
                .size(HEADER_ICON_SIZE));
    }

    public void setTitle(String key) {
        lblHeaderTitle.textProperty().bind(BundleManager.createStringBinding(key));
    }
}
