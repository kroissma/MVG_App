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
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResetPasswordController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResetPasswordController.class);

    @Autowired
    private UserService userService;


    @FXML
    private PasswordField pfPassword;
    @FXML
    private PasswordField pfPasswordConf;
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
            } catch (DataAccessException e) {
                LOGGER.error("Could not save user: %", user, e);
            }
            okClicked = true;
            resetPasswordStage().close();
        }
    }

    @FXML
    private void btnActionCancel() {
        okClicked = false;
        resetPasswordStage().close();
    }

    /* method necessary because it can't the Stage can't be initialized while loading the fxml file
       because the Scene is set afterwards */
    private Stage resetPasswordStage() {
        return ((Stage) pfPassword.getScene().getWindow());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public boolean isInputValid() {
        String errorMessage = "";

        if (!ValidationUtil.isValidText(pfPassword)) {
            errorMessage +=
                BundleManager.getBundle().getString("dialog.error.msg.invalidPassword") + "\n";
            LOGGER.info("Reset Password -> invalid password");
        }
        if (!pfPassword.getText().equals(pfPasswordConf.getText())) {
            errorMessage +=
                BundleManager.getBundle().getString("dialog.error.msg.notMatchingPassword") + "\n";
            LOGGER.info("Reset Password -> passwords don't match");
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            DialogUtil.showDialog(Alert.AlertType.ERROR, resetPasswordStage(),
                BundleManager.getBundle().getString("dialog.error.invalidInput"),
                BundleManager.getBundle().getString("dialog.error.enterValidInput")
                , errorMessage);
            return false;
        }
    }

    /**
     * sets the user data that should be loaded into the form
     *
     * @param user the object with the information of the user
     */
    public void setUser(UserDTO user) {
        this.user = user;
    }

    private void setUserData() {
        user.setPassword(pfPassword.getText());
    }
}
