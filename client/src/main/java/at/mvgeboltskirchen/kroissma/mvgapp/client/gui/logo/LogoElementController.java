package at.mvgeboltskirchen.kroissma.mvgapp.client.gui.logo;

import at.mvgeboltskirchen.kroissma.mvgapp.rest.logo.LogoDTO;
import java.io.ByteArrayInputStream;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LogoElementController {

    private LogoDTO logo;

    @FXML
    private Label lblName;
    @FXML
    private ImageView ivLogo;

    public void initializeData(LogoDTO logo) {
        this.logo = logo;

        lblName.setText(logo.getTitle());
        if (logo.getImageBytes() != null) {
            ivLogo.setImage(new Image(new ByteArrayInputStream(logo.getImageBytes())));
        }
    }
}
