package at.mvgeboltskirchen.kroissma.mvgapp.client.gui.logo;

import at.mvgeboltskirchen.kroissma.mvgapp.client.exception.DataAccessException;
import at.mvgeboltskirchen.kroissma.mvgapp.client.gui.MainController;
import at.mvgeboltskirchen.kroissma.mvgapp.client.gui.TabHeaderController;
import at.mvgeboltskirchen.kroissma.mvgapp.client.gui.guiUtil.DialogUtil;
import at.mvgeboltskirchen.kroissma.mvgapp.client.service.LogoService;
import at.mvgeboltskirchen.kroissma.mvgapp.client.util.BundleManager;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.logo.LogoDTO;
import at.mvgeboltskirchen.kroissma.springfx.SpringFxmlLoader;
import java.util.Iterator;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

@Component
public class LogoController {

    private MainController mainController;
    private SpringFxmlLoader springFxmlLoader;
    private LogoService logoService;

    @FXML
    private VBox vbLogos;
    @FXML
    private TabHeaderController tabHeaderController;

    public LogoController(MainController mainController, SpringFxmlLoader springFxmlLoader,
        LogoService logoService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.logoService = logoService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.NEWSPAPER_ALT);
        tabHeaderController.setTitle("tabHeader.title.logos");
    }

    public void loadLogos() {
        ObservableList<Node> vbLogosChildren = vbLogos.getChildren();
        vbLogosChildren.clear();
        Task<List<LogoDTO>> task = new Task<List<LogoDTO>>() {
            @Override
            protected List<LogoDTO> call() throws DataAccessException {
                return logoService.findAll();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                for (Iterator<LogoDTO> iterator = getValue().iterator();
                    iterator.hasNext(); ) {
                    LogoDTO logo = iterator.next();
                    SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
                        .loadAndWrap("/fxml/logo/logoElement.fxml");
                    ((LogoElementController) wrapper.getController())
                        .initializeData(logo);
                    vbLogosChildren.add((Node) wrapper.getLoadedObject());
                    if (iterator.hasNext()) {
                        Separator separator = new Separator();
                        vbLogosChildren.add(separator);
                    }
                }
            }

            @Override
            protected void failed() {
                super.failed();
                DialogUtil.exceptionDialog(getException(),
                    vbLogos.getScene().getWindow(),
                    BundleManager.getExceptionBundle().getString("exception.failed.loadData"))
                    .showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }
}
