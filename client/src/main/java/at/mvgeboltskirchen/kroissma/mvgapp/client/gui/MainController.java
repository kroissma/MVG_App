package at.mvgeboltskirchen.kroissma.mvgapp.client.gui;

import at.mvgeboltskirchen.kroissma.mvgapp.client.gui.news.CreateNewsController;
import at.mvgeboltskirchen.kroissma.mvgapp.client.gui.news.NewsController;
import at.mvgeboltskirchen.kroissma.mvgapp.client.gui.user.UserController;
import at.mvgeboltskirchen.kroissma.mvgapp.client.service.AuthenticationInformationService;
import at.mvgeboltskirchen.kroissma.mvgapp.client.service.AuthenticationService;
import at.mvgeboltskirchen.kroissma.mvgapp.client.util.BundleManager;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.authentication.AuthenticationTokenInfo;
import at.mvgeboltskirchen.kroissma.springfx.SpringFxmlLoader;
import at.mvgeboltskirchen.kroissma.springfx.SpringFxmlLoader.LoadWrapper;
import java.util.Locale;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainController {

    private static final int TAB_ICON_FONT_SIZE = 20;
    private final SpringFxmlLoader springFxmlLoader;
    private final FontAwesome fontAwesome;
    private boolean isLoggedIn = false;
    private boolean isAdmin = false;
    @FXML
    private StackPane spMainContent;
    @FXML
    private ProgressBar pbLoadingProgress;
    @FXML
    private TabPane tpContent;
    @FXML
    private MenuBar mbMain;
    @FXML
    private MenuItem miLogout;
    @FXML
    private MenuItem miExit;
    @FXML
    private MenuItem miGerman;
    @FXML
    private MenuItem miEnglish;
    @FXML
    private MenuItem miAbout;
    @FXML
    private Menu muApplication;
    @FXML
    private Menu muLanguage;
    @FXML
    private Menu muHelp;


    private AuthenticationController authenticationController;
    private Node login;
    private NewsController newsController;
    private Tab newsTab;
    private UserController userController;
    private Tab userTab;
    private CreateNewsController createNewsController;
    private Tab createNewsTab;

    @Autowired
    private AuthenticationService authenticationService;

    public MainController(
        SpringFxmlLoader springFxmlLoader,
        FontAwesome fontAwesome,
        AuthenticationInformationService authenticationInformationService
    ) {
        this.springFxmlLoader = springFxmlLoader;
        this.fontAwesome = fontAwesome;
        authenticationInformationService.addAuthenticationChangeListener(
            authenticationTokenInfo -> setAuthenticated(authenticationTokenInfo));
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> mbMain.setUseSystemMenuBar(true));
        pbLoadingProgress.setProgress(0);
        LoadWrapper wrapper = springFxmlLoader.loadAndWrap("/fxml/authenticationComponent.fxml");
        login = (Node) wrapper.getLoadedObject();
        spMainContent.getChildren().add(login);
        authenticationController = (AuthenticationController) wrapper.getController();
        initLocalizationBindings();
        initNewsTabPane();
        initUserTabPane();
        initCreateNewsTabPane();
    }

    private void initLocalizationBindings() {
        muApplication.textProperty().bind(BundleManager.createStringBinding("menu.application"));
        muLanguage.textProperty().bind(BundleManager.createStringBinding("menu.language"));
        muHelp.textProperty().bind(BundleManager.createStringBinding("menu.help"));
        miLogout.textProperty().bind(BundleManager.createStringBinding("menu.application.logout"));
        miExit.textProperty().bind(BundleManager.createStringBinding("menu.application.exit"));
        miEnglish.textProperty().bind(BundleManager.createStringBinding("menu.language.english"));
        miGerman.textProperty().bind(BundleManager.createStringBinding("menu.language.german"));
        miAbout.textProperty().bind(BundleManager.createStringBinding("menu.help.about"));

        String language = BundleManager.getLocale().getLanguage();
        if (language.equals("de")) {
            miGerman.setDisable(true);
        } else if (language.equals("en")) {
            miEnglish.setDisable(true);
        }
    }

    @FXML
    private void germanMenuAction() {
        Locale.setDefault(new Locale("de", "DE"));
        BundleManager.changeLocale(new Locale("de"));
        miGerman.setDisable(true);
        miEnglish.setDisable(false);
        refreshTabs();
    }

    @FXML
    private void englishMenuAction() {
        Locale.setDefault(new Locale("en", "US"));
        BundleManager.changeLocale(new Locale("en"));
        miGerman.setDisable(false);
        miEnglish.setDisable(true);
        refreshTabs();
    }

    private void refreshTabs() {
        if (isLoggedIn) {
            int selectedIndex = tpContent.getSelectionModel().getSelectedIndex();

//            tpContent.getTabs().remove(0, tpContent.getTabs().size());
            tpContent.getTabs().remove(newsTab);
            initNewsTabPane();
            if (isAdmin) {
                tpContent.getTabs().remove(userTab);
                initUserTabPane();
                tpContent.getTabs().remove(createNewsTab);
                initCreateNewsTabPane();
            }
            tpContent.getSelectionModel().select(selectedIndex);
        }
    }

    @FXML
    private void exitApplication(ActionEvent actionEvent) {
        Stage stage = (Stage) spMainContent.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    private void logout(ActionEvent actionEvent) {
        isLoggedIn = false;
        authenticationService.deAuthenticate();
        authenticationController.clearInput();
        miLogout.setVisible(false);
    }

    @FXML
    private void aboutApplication(ActionEvent actionEvent) {
        Stage stage = (Stage) spMainContent.getScene().getWindow();
        Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setScene(new Scene((Parent) springFxmlLoader.load("/fxml/aboutDialog.fxml")));
        dialog.setTitle(BundleManager.getBundle().getString("dialog.about.title"));
        dialog.showAndWait();
    }

    private void initNewsTabPane() {
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/news/newsComponent.fxml");
        newsController = (NewsController) wrapper.getController();
        newsTab = new Tab(null, (Node) wrapper.getLoadedObject());
        Glyph newsGlyph = fontAwesome.create(FontAwesome.Glyph.NEWSPAPER_ALT);
        newsGlyph.setFontSize(TAB_ICON_FONT_SIZE);
        newsGlyph.setColor(Color.WHITE);
        newsTab.setGraphic(newsGlyph);
        tpContent.getTabs().add(newsTab);

        tpContent.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null && newValue.equals(newsTab)) {
                    newsController.loadNews();
                }
            }
        );
    }

    private void initUserTabPane() {
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/user/userComponent.fxml");
        userController = (UserController) wrapper.getController();
        userTab = new Tab(null, (Node) wrapper.getLoadedObject());
        Glyph userGlyph = fontAwesome.create(FontAwesome.Glyph.USERS);
        userGlyph.setFontSize(TAB_ICON_FONT_SIZE);
        userGlyph.setColor(Color.WHITE);
        userTab.setGraphic(userGlyph);
        tpContent.getTabs().add(userTab);
        tpContent.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null && newValue.equals(userTab)) {
                    userController.loadUsers();
                }
            }
        );
    }

    private void initCreateNewsTabPane() {
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/news/createNewsComponent.fxml");
        createNewsController = (CreateNewsController) wrapper.getController();
        createNewsTab = new Tab(null, (Node) wrapper.getLoadedObject());
        Glyph ticketGlyph = fontAwesome.create(FontAwesome.Glyph.PENCIL_SQUARE_ALT);
        ticketGlyph.setFontSize(TAB_ICON_FONT_SIZE);
        ticketGlyph.setColor(Color.WHITE);
        createNewsTab.setGraphic(ticketGlyph);
        tpContent.getTabs().add(createNewsTab);
    }

    private void setAuthenticated(AuthenticationTokenInfo authenticationTokenInfo) {
        // Initial authentication
        if (authenticationTokenInfo != null && !isLoggedIn) {
            isLoggedIn = true;
            isAdmin = authenticationTokenInfo.getRoles().contains("ROLE_ADMIN");
            userController.setUsername(authenticationTokenInfo.getUsername());

            if (!isAdmin) {
                if (tpContent.getTabs().contains(userTab)) {
                    tpContent.getTabs().remove(userTab);
                }
                if (tpContent.getTabs().contains(createNewsTab)) {
                    tpContent.getTabs().remove(createNewsTab);
                }
            } else {
                if (!tpContent.getTabs().contains(userTab)) {
                    tpContent.getTabs().add(userTab);
                }
                if (!tpContent.getTabs().contains(createNewsTab)) {
                    tpContent.getTabs().add(createNewsTab);
                }
            }

            if (spMainContent.getChildren().contains(login)) {
                spMainContent.getChildren().remove(login);
            }

            miLogout.setVisible(true);
            newsController.loadNews();
            tpContent.getSelectionModel().selectFirst();
            userController.resetFilters();
            createNewsController.clearInputfields();
        } else if (authenticationTokenInfo == null) {
            if (!spMainContent.getChildren().contains(login)) {
                spMainContent.getChildren().add(login);
            }
        }
    }

    public void setProgressbarProgress(double progress) {
        pbLoadingProgress.setProgress(progress);
    }

}
