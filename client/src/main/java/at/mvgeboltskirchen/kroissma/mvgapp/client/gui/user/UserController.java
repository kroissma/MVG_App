package at.mvgeboltskirchen.kroissma.mvgapp.client.gui.user;


import at.mvgeboltskirchen.kroissma.mvgapp.client.MvgAppClientApplication;
import at.mvgeboltskirchen.kroissma.mvgapp.client.exception.DataAccessException;
import at.mvgeboltskirchen.kroissma.mvgapp.client.gui.MainController;
import at.mvgeboltskirchen.kroissma.mvgapp.client.gui.TabHeaderController;
import at.mvgeboltskirchen.kroissma.mvgapp.client.gui.guiUtil.DialogStage;
import at.mvgeboltskirchen.kroissma.mvgapp.client.gui.guiUtil.DialogUtil;
import at.mvgeboltskirchen.kroissma.mvgapp.client.service.UserService;
import at.mvgeboltskirchen.kroissma.mvgapp.client.util.BundleManager;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.user.UserDTO;
import at.mvgeboltskirchen.kroissma.springfx.SpringFxmlLoader;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final MvgAppClientApplication mainApp;
    private final SpringFxmlLoader springFxmlLoader;
    private final MainController mainController;
    private final UserService userService;
    // region FXML variables
    @FXML
    private TabHeaderController tabHeaderController;
    @FXML
    private TableView<UserDTO> tvUsers;
    @FXML
    private TableColumn<UserDTO, String> tcUsername;
    @FXML
    private TableColumn<UserDTO, Boolean> tcLocked;
    @FXML
    private TableColumn<UserDTO, Boolean> tcAdmin;
    @FXML
    private Button btnNewUser;
    @FXML
    private TextField tfFilterUsername;
    @FXML
    private ComboBox<String> cbLocked;
    @FXML
    private ComboBox<String> cbAdmin;
    @FXML
    private MenuItem miLock;
    @FXML
    private MenuItem miResetPassword;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblLocked;
    @FXML
    private Label lblAdmin;
    // endregion
    private NewUserController newUserController;
    private ResetPasswordController resetPasswordController;
    private String username;

    public UserController(SpringFxmlLoader springFxmlLoader, MainController mainController,
        MvgAppClientApplication mainApp, UserService userService) {
        this.springFxmlLoader = springFxmlLoader;
        this.mainApp = mainApp;
        this.userService = userService;
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        initLocalizationBindings();
        tabHeaderController.setIcon(FontAwesome.Glyph.USERS);
        tabHeaderController.setTitle("user.component.header");
        // region setCellValueFactory for all table columns
        tcUsername.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsername()));
        tcLocked.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isLocked()));
        tcLocked.setCellFactory(c -> new CheckBoxTableCell<>(
            index -> new SimpleBooleanProperty(tvUsers.getItems().get(index).isLocked())));
        tcAdmin.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isAdmin()));
        tcAdmin.setCellFactory(c -> new CheckBoxTableCell<>(
            index -> new SimpleBooleanProperty(tvUsers.getItems().get(index).isAdmin())));
        // endregion
        initComboBoxes();
        cbAdmin.setValue(BundleManager.getBundle().getString("filter.empty"));
        tvUsers.getContextMenu().setOnShowing(event -> {
            UserDTO userDTO = tvUsers.getSelectionModel().getSelectedItem();
            miLock.textProperty()
                .bind(userDTO.isLocked() ? BundleManager.createStringBinding("user.unlock") :
                    BundleManager.createStringBinding("user.lock"));
            miLock.setDisable(userDTO.getUsername().equals(username));
        });
        btnNewUser.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnActionNewUser();
            }
        });
    }

    private void initComboBoxes() {
        String locked = BundleManager.getBundle().getString("user.locked");
        String unlocked = BundleManager.getBundle().getString("user.unlocked");
        String emtpy = BundleManager.getBundle().getString("filter.empty");
        cbLocked.getItems().addAll(locked, unlocked, emtpy);
        cbLocked.getSelectionModel().selectLast();

        cbAdmin.getItems().addAll(BundleManager.getBundle().getString("user.admin"),
            BundleManager.getBundle().getString("user.user"),
            BundleManager.getBundle().getString("filter.empty"));
    }

    private void initLocalizationBindings() {
        lblUsername.textProperty().bind(BundleManager.createStringBinding("authenticate.username"));
        btnNewUser.textProperty().bind(BundleManager.createStringBinding("user.component.newUser"));
        lblLocked.textProperty().bind(BundleManager.createStringBinding("user.locked"));
        lblAdmin.textProperty().bind(BundleManager.createStringBinding("user.admin"));
        miResetPassword.textProperty().bind(BundleManager.createStringBinding("user.reset"));
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void loadUsers() {
        Task<List<UserDTO>> task = new Task<List<UserDTO>>() {
            @Override
            protected List<UserDTO> call() throws Exception {
                return userService.getAllUsers();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                ObservableList<UserDTO> users = FXCollections.observableArrayList(getValue());
                FilteredList<UserDTO> filteredUsers = new FilteredList<>(users, p -> true);
                addFilters(filteredUsers);
                SortedList<UserDTO> sortedUsers = new SortedList<>(filteredUsers);
                sortedUsers.comparatorProperty().bind(tvUsers.comparatorProperty());
                tvUsers.setItems(sortedUsers);
            }

            @Override
            protected void failed() {
                super.failed();
                DialogUtil.exceptionDialog(getException(),
                    tvUsers.getScene().getWindow(),
                    BundleManager.getExceptionBundle().getString("exception.failed.loadData"))
                    .showAndWait();
            }

            private void addFilters(FilteredList<UserDTO> filteredUsers) {
                filteredUsers.predicateProperty().bind(Bindings.createObjectBinding(() ->
                        user -> user.getUsername().toLowerCase()
                            .contains(tfFilterUsername.getText().toLowerCase())
                            && checkLockedFilter(user)
                            && checkAdminFilter(user),
                    tfFilterUsername.textProperty(),
                    cbLocked.valueProperty(),
                    cbAdmin.valueProperty()
                ));
            }

            private boolean checkLockedFilter(UserDTO user) {
                boolean selected = tcLocked.getCellData(user);
                return (selected && cbLocked != null && cbLocked.getValue()
                    .equals(BundleManager.getBundle().getString("user.locked")))
                    || (!selected && cbLocked != null && cbLocked.getValue()
                    .equals(BundleManager.getBundle().getString("user.unlocked")))
                    || cbLocked != null && cbLocked.getValue()
                    .equals(BundleManager.getBundle().getString("filter.empty"));
            }

            private boolean checkAdminFilter(UserDTO user) {
                boolean selected = tcAdmin.getCellData(user);
                return (selected && cbAdmin.getValue()
                    .equals(BundleManager.getBundle().getString("user.admin")))
                    || (!selected && cbAdmin.getValue()
                    .equals(BundleManager.getBundle().getString("user.user")))
                    || cbAdmin.getValue()
                    .equals(BundleManager.getBundle().getString("filter.empty"));
            }

        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    @FXML
    private void btnActionNewUser() {
        if (showNewUserDialogStage(new UserDTO())) {
            loadUsers();
        }
    }

    private boolean showNewUserDialogStage(UserDTO selectedUser) {
        Stage newUserDialogStage = new DialogStage(mainApp.getMainStage(),
            BundleManager.getBundle().getString("newUserDialog.title"));
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/user/newUserDialog.fxml");
        newUserController = (NewUserController) wrapper.getController();
        newUserController.setUser(selectedUser);
        Scene scene = new Scene((BorderPane) wrapper.getLoadedObject());
        newUserDialogStage.setScene(scene);
        newUserDialogStage.showAndWait();
        return newUserController.isOkClicked();
    }

    @FXML
    private void toggleLock() {
        UserDTO selectedUser = getSelectedUser();
        selectedUser.setLocked(!selectedUser.isLocked());
        try {
            userService.addOrUpdateUser(selectedUser);
        } catch (DataAccessException e) {
            LOGGER.error("Could not save user: %", selectedUser, e);
        }
        loadUsers();
    }

    @FXML
    private void resetPW() {
        UserDTO selectedUser = getSelectedUser();
        if (showPasswordResetDialogStage(selectedUser)) {
            loadUsers();
        }
    }

    private boolean showPasswordResetDialogStage(UserDTO selectedUser) {
        Stage resetPasswordDialogStage = new DialogStage(mainApp.getMainStage(),
            BundleManager.getBundle().getString("resetPasswordDialog.title"));
        SpringFxmlLoader.LoadWrapper wrapper = springFxmlLoader
            .loadAndWrap("/fxml/user/resetPasswordDialog.fxml");
        resetPasswordController = (ResetPasswordController) wrapper.getController();
        resetPasswordController.setUser(selectedUser);
        Scene scene = new Scene((BorderPane) wrapper.getLoadedObject());
        resetPasswordDialogStage.setScene(scene);
        resetPasswordDialogStage.showAndWait();
        return resetPasswordController.isOkClicked();
    }

    public void resetFilters() {
        tfFilterUsername.clear();
        cbLocked.getSelectionModel().selectLast();
        cbAdmin.getSelectionModel().selectLast();
        tvUsers.getSortOrder().clear();
    }

    private UserDTO getSelectedUser() {
        return tvUsers.getSelectionModel().getSelectedItem();
    }
}
