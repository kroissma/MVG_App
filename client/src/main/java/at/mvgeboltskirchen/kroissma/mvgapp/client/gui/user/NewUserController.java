package at.mvgeboltskirchen.kroissma.mvgapp.client.gui.user;

import at.mvgeboltskirchen.kroissma.mvgapp.client.exception.DataAccessException;
import at.mvgeboltskirchen.kroissma.mvgapp.client.gui.guiUtil.DialogUtil;
import at.mvgeboltskirchen.kroissma.mvgapp.client.gui.guiUtil.ValidationUtil;
import at.mvgeboltskirchen.kroissma.mvgapp.client.service.UserService;
import at.mvgeboltskirchen.kroissma.mvgapp.client.util.BundleManager;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.user.UserDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewUserController.class);

    @Autowired
    private UserService userService;

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private PasswordField pfPasswordConf;
    @FXML
    private CheckBox cbAdmin;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    private boolean okClicked;
    private UserDTO user;

    @FXML
    private void initialize() {
        btnSave.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnActionSave();
            }
        });
        btnCancel.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnActionCancel();
            }
        });
    }


    @FXML
    private void btnActionSave() {
        if (isInputValid()) {
            setUserData();
            try {
                userService.addOrUpdateUser(user);
                okClicked = true;
                newUserStage().close();
            } catch (DataAccessException e) {
                if (e.getMessage().contains("status code 409")) {
                    LOGGER.warn("User already exists");
                    String errorMsg = BundleManager.getExceptionBundle()
                        .getString("exception.userExists");
                    DialogUtil.exceptionDialog(e, tfUsername.getScene().getWindow(), errorMsg)
                        .showAndWait();
                } else {
                    LOGGER.error("Could not save user: %", user, e);
                }
            }
        }
    }

    @FXML
    private void btnActionCancel() {
        okClicked = false;
        newUserStage().close();
    }

    /* method necessary because it can't the Stage can't be initialized while loading the fxml file
       because the Scene is set afterwards */
    private Stage newUserStage() {
        return ((Stage) tfUsername.getScene().getWindow());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public boolean isInputValid() {
        String errorMessage = "";

        if (!ValidationUtil.isValidText(tfUsername)) {
            errorMessage +=
                BundleManager.getBundle().getString("dialog.error.msg.invalidUsername") + "\n";
            LOGGER.info("New User -> invalid username: %", tfUsername.getText());
        }
        if (!ValidationUtil.isValidText(pfPassword)) {
            errorMessage +=
                BundleManager.getBundle().getString("dialog.error.msg.invalidPassword") + "\n";
            LOGGER.info("New User -> invalid password");
        }
        if (!pfPassword.getText().equals(pfPasswordConf.getText())) {
            errorMessage +=
                BundleManager.getBundle().getString("dialog.error.msg.notMatchingPassword") + "\n";
            LOGGER.info("New User -> passwords don't match");
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            DialogUtil.showDialog(Alert.AlertType.ERROR, newUserStage(),
                BundleManager.getBundle().getString("dialog.error.invalidInput"),
                BundleManager.getBundle().getString("dialog.error.enterValidInput")
                , errorMessage);
            return false;
        }
    }

    /**
     * sets the user data that should be loaded into the form
     * if you want to create a new user, pass an element with empty strings for each field
     *
     * @param user the object with the information of the user
     */
    public void setUser(UserDTO user) {
        this.user = user;
    }

    private void setUserData() {
        user.setUsername(tfUsername.getText());
        if (!pfPassword.getText().isEmpty()) {
            user.setPassword(pfPassword.getText());
        }
        user.setAdmin(cbAdmin.isSelected());
    }
}
