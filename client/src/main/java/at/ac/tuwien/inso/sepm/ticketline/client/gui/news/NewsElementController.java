package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.guiUtil.DialogStage;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader.LoadWrapper;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewsElementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsElementController.class);

    private final NewsService newsService;
    private final NewsController newsController;
    private final SpringFxmlLoader springFxmlLoader;

    private SimpleNewsDTO simpleNewsDTO;
    private boolean isUnreadNews;

    @FXML
    private Label lblDate;
    @FXML
    private Label lblTitle;
    @FXML
    private Label lblText;

    public NewsElementController(
        NewsService newsService,
        NewsController newsController,
        SpringFxmlLoader springFxmlLoader) {
        this.newsService = newsService;
        this.newsController = newsController;
        this.springFxmlLoader = springFxmlLoader;
    }

    public void initializeData(SimpleNewsDTO simpleNewsDTO, boolean isUnreadNews) {
        this.simpleNewsDTO = simpleNewsDTO;
        this.isUnreadNews = isUnreadNews;
        DateTimeFormatter dateTimeFormatterNews =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT).withLocale(
                BundleManager.getLocale());
        lblDate.setText(dateTimeFormatterNews.format(simpleNewsDTO.getPublishedAt()));
        lblTitle.setText(simpleNewsDTO.getTitle());
        lblText.setText(simpleNewsDTO.getSummary());
    }

    @FXML
    private void showNewsDetails() {
        try {
            DetailedNewsDTO detailedNews = newsService.findDetailedNews(simpleNewsDTO);
            Stage newsDetailStage = initNewsDetailStage(detailedNews);
            newsDetailStage.showAndWait();
            if (isUnreadNews) {
                newsService.markNewsAsRead(simpleNewsDTO);
                newsController.loadNews();
            }
        } catch (DataAccessException e) {
            LOGGER.warn("Unable to mark news id: {} title: {} published: {} as read",
                simpleNewsDTO.getId(), simpleNewsDTO.getTitle(), simpleNewsDTO.getPublishedAt());
        }
    }

    private Stage initNewsDetailStage(DetailedNewsDTO detailedNews) {
        Stage newsDetailStage = new DialogStage((Stage) lblDate.getScene().getWindow(), "News");
        LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/news/newsDetails.fxml");
        NewsDetailController newsDetailController = (NewsDetailController) wrapper.getController();
        newsDetailController.init(detailedNews);
        newsDetailStage.setScene(new Scene((BorderPane) wrapper.getLoadedObject()));
        return newsDetailStage;
    }
}
