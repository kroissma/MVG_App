package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class NewsDetailController {

    @FXML
    private Label lbHeader;
    @FXML
    private Label lbDate;
    @FXML
    private TextArea taContent;
    @FXML
    private BorderPane borderPane;
    @FXML
    private ImageView ivImage;

    public void init(DetailedNewsDTO news) {
        lbHeader.setText(news.getTitle());
        lbDate.setText(news.getPublishedAt().format(
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT).withLocale(
                BundleManager.getLocale())));
        taContent.setText(news.getText());
        if (news.getImageBytes() != null) {
            ivImage.setImage(new Image(new ByteArrayInputStream(news.getImageBytes())));
        } else {
            borderPane.setLeft(null);
        }
    }

    @FXML
    private void handleOk() {
        ((Stage) lbHeader.getScene().getWindow()).close();
    }
}
