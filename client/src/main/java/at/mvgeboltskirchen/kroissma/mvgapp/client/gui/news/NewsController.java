package at.mvgeboltskirchen.kroissma.mvgapp.client.gui.news;

import at.mvgeboltskirchen.kroissma.mvgapp.client.exception.DataAccessException;
import at.mvgeboltskirchen.kroissma.mvgapp.client.gui.MainController;
import at.mvgeboltskirchen.kroissma.mvgapp.client.gui.TabHeaderController;
import at.mvgeboltskirchen.kroissma.mvgapp.client.gui.guiUtil.DialogUtil;
import at.mvgeboltskirchen.kroissma.mvgapp.client.service.NewsService;
import at.mvgeboltskirchen.kroissma.mvgapp.client.util.BundleManager;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.news.SimpleNewsDTO;
import at.mvgeboltskirchen.kroissma.springfx.SpringFxmlLoader;
import java.util.Iterator;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NewsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);
    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final NewsService newsService;
    @FXML
    private ChoiceBox<String> cbNews;
    @FXML
    private VBox vbNewsElements;
    @FXML
    private TabHeaderController tabHeaderController;

    /*
    This variable simplifies the determination of which news should be loaded.

    If it is true, the unread news of the current user should be loaded.
    If it is false, the read news of the current user should be loaded.
     */
    private boolean loadUnreadNews;

    public NewsController(MainController mainController, SpringFxmlLoader springFxmlLoader,
        NewsService newsService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.newsService = newsService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.NEWSPAPER_ALT);
        tabHeaderController.setTitle("tabHeader.title");

        cbNews.getItems().add(BundleManager.getBundle().getString("news.select.not.read"));
        cbNews.getItems().add(BundleManager.getBundle().getString("news.select.read"));
        cbNews.setValue(BundleManager.getBundle().getString("news.select.not.read"));
        loadUnreadNews = true;
        cbNews.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                loadUnreadNews = !loadUnreadNews;
                loadNews();
            }
        });
    }

    public void loadNews() {
        ObservableList<Node> vbNewsBoxChildren = vbNewsElements.getChildren();
        vbNewsBoxChildren.clear();
        Task<List<SimpleNewsDTO>> task = new Task<List<SimpleNewsDTO>>() {
            @Override
            protected List<SimpleNewsDTO> call() throws DataAccessException {
                return loadUnreadNews ? newsService.findAllNotRead() : newsService.findAllRead();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                for (Iterator<SimpleNewsDTO> iterator = getValue().iterator();
                    iterator.hasNext(); ) {
                    SimpleNewsDTO news = iterator.next();
                    SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
                        .loadAndWrap("/fxml/news/newsElement.fxml");
                    ((NewsElementController) wrapper.getController())
                        .initializeData(news, loadUnreadNews);
                    vbNewsBoxChildren.add((Node) wrapper.getLoadedObject());
                    if (iterator.hasNext()) {
                        Separator separator = new Separator();
                        vbNewsBoxChildren.add(separator);
                    }
                }
            }

            @Override
            protected void failed() {
                super.failed();
                DialogUtil.exceptionDialog(getException(),
                    vbNewsElements.getScene().getWindow(),
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
