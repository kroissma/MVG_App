package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ImageProcessingException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.guiUtil.DialogUtil;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.guiUtil.ValidationUtil;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CreateNewsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateNewsController.class);
    private final NewsService newsService;
    private final FontAwesome fontAwesome;
    private byte[] image = null;
    @FXML
    private Label lblPath;
    @FXML
    private TextArea taContent;
    @FXML
    private TextField tfAbstract;
    @FXML
    private Button homeButton;
    @FXML
    private TabHeaderController tabHeaderController;

    public CreateNewsController(NewsService newsService, FontAwesome fontAwesome) {
        this.newsService = newsService;
        this.fontAwesome = fontAwesome;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.PENCIL_SQUARE_ALT);
        tabHeaderController.setTitle("news.create.title");

        homeButton.setGraphic(
            fontAwesome
                .create(Glyph.FILE_IMAGE_ALT)
                .size(15));

        tfAbstract.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 100) {
                tfAbstract.setText(newValue.substring(0, 100));
            }
        });
        taContent.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 10_000) {
                taContent.setText(newValue.substring(0, 10_000));
            }
        });
    }

    @FXML
    public void clearPath(ActionEvent actionEvent) {
        lblPath.setText(BundleManager.getBundle().getString("news.create.image.path"));
        image = null;
    }

    @FXML
    public void createNews(ActionEvent actionEvent) {
        if (isInputValid()) {
            DetailedNewsDTO detailedNewsDTO = DetailedNewsDTO.builder()
                .title(tfAbstract.getText())
                .text(taContent.getText())
                .image(image)
                .build();
            try {
                newsService.publishNews(detailedNewsDTO);
                clearInputfields();
                DialogUtil.showDialog(
                    Alert.AlertType.INFORMATION,
                    (Stage) tfAbstract.getScene().getWindow(),
                    BundleManager.getBundle().getString("news.create.success.title"),
                    BundleManager.getBundle().getString("news.create.success.title"),
                    BundleManager.getBundle().getString("news.create.success.content")
                );
            } catch (DataAccessException e) {
                DialogUtil.exceptionDialog(e, tfAbstract.getScene().getWindow(), e.getMessage())
                    .showAndWait();
            } catch (ImageProcessingException e) {
                LOGGER.warn("Unable to store image on server");
                DialogUtil.exceptionDialog(e, tfAbstract.getScene().getWindow(),
                    BundleManager.getBundle().getString("news.image.process.error"));
            }
        }
    }

    @FXML
    public void showFileChooser(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(BundleManager.getBundle().getString("news.create.fc.title"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
            BundleManager.getBundle().getString("news.create.fc.image"),
            "*.jpg"));
        File file = fileChooser.showOpenDialog(tfAbstract.getScene().getWindow());
        if (file != null) {
            String filename = file.getName();
            String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
            if (extension.toLowerCase().equals("jpg")) {
                lblPath.setText(file.getAbsolutePath());
                image = resizeImage(file);
            } else {
                DialogUtil
                    .errorDialog(BundleManager.getBundle().getString("news.image.noimagefile"),
                        lblPath.getScene().getWindow()).showAndWait();
            }
        }
    }

    private byte[] resizeImage(File imageFile) {
        try {
            BufferedImage imgIn = ImageIO.read(imageFile);
            int newWidth = 400;
            double resizeFactor = (double) newWidth / (double) imgIn.getWidth();
            int newHeigth = (int) (imgIn.getHeight() * resizeFactor);
            BufferedImage imgOut = new BufferedImage(newWidth, newHeigth, imgIn.getType());
            Graphics2D g = imgOut.createGraphics();
            g.drawImage(imgIn, 0, 0, newWidth, newHeigth, null);
            g.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imgOut, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            LOGGER.warn("Unable to process image file {}", e);
            DialogUtil.errorDialog(BundleManager.getBundle().getString("news.image.process.error"),
                lblPath.getScene().getWindow()).showAndWait();
        }
        return null;
    }

    public void clearInputfields() {
        tfAbstract.clear();
        taContent.clear();
        lblPath.setText(BundleManager.getBundle().getString("news.create.image.path"));
        image = null;
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (!ValidationUtil.isValidText(tfAbstract)) {
            errorMessage +=
                BundleManager.getBundle().getString("dialog.error.msg.invalidAbstract") + "\n";
            LOGGER.info("New News -> invalid abstract: %", tfAbstract.getText());
        }
        if (!ValidationUtil.isValidText(taContent.getText())) {
            errorMessage +=
                BundleManager.getBundle().getString("dialog.error.msg.invalidContent") + "\n";
            LOGGER.info("New News -> invalid content");
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            DialogUtil.showDialog(Alert.AlertType.ERROR, (Stage) tfAbstract.getScene().getWindow(),
                BundleManager.getBundle().getString("dialog.error.invalidInput"),
                BundleManager.getBundle().getString("dialog.error.enterValidInput")
                , errorMessage);
            return false;
        }
    }
}
