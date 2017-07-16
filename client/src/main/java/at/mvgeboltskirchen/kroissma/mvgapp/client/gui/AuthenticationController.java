package at.mvgeboltskirchen.kroissma.mvgapp.client.gui;

import at.mvgeboltskirchen.kroissma.mvgapp.client.exception.DataAccessException;
import at.mvgeboltskirchen.kroissma.mvgapp.client.gui.guiUtil.DialogUtil;
import at.mvgeboltskirchen.kroissma.mvgapp.client.service.AuthenticationService;
import at.mvgeboltskirchen.kroissma.mvgapp.client.util.BundleManager;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.authentication.AuthenticationRequest;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.authentication.AuthenticationTokenInfo;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final MainController mainController;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblPassword;
    @FXML
    private Button btnAuthenticate;

    public AuthenticationController(AuthenticationService authenticationService,
        MainController mainController) {
        this.authenticationService = authenticationService;
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        txtUsername.promptTextProperty()
            .bind(BundleManager.createStringBinding("authenticate.username"));
        txtPassword.promptTextProperty()
            .bind(BundleManager.createStringBinding("authenticate.password"));
        lblUsername.textProperty().bind(BundleManager.createStringBinding("authenticate.username"));
        lblPassword.textProperty().bind(BundleManager.createStringBinding("authenticate.password"));
        btnAuthenticate.textProperty()
            .bind(BundleManager.createStringBinding("authenticate.authenticate"));
    }

    public void clearInput() {
        txtUsername.clear();
        txtPassword.clear();
    }

    @FXML
    private void handleAuthenticate(ActionEvent actionEvent) {
        Task<AuthenticationTokenInfo> task = new Task<AuthenticationTokenInfo>() {
            @Override
            protected AuthenticationTokenInfo call() throws DataAccessException {
                return authenticationService.authenticate(
                    AuthenticationRequest.builder()
                        .username(txtUsername.getText())
                        .password(txtPassword.getText())
                        .build());
            }

            @Override
            protected void failed() {
                super.failed();

                Throwable exception = getException();

                HttpClientErrorException hte = null;
                if (exception.getCause() instanceof HttpClientErrorException) {
                    hte = (HttpClientErrorException) exception.getCause();
                }

                String errorMsg;
                if (exception.getMessage().contains("Unable to connect")) {
                    errorMsg = BundleManager.getExceptionBundle()
                        .getString("exception.connect.server");
                } else if (hte != null && hte.getResponseBodyAsString()
                    .contains("Account is locked")) {
                    errorMsg = BundleManager.getExceptionBundle()
                        .getString("authenticate.error.msg.locked");
                } else {
                    errorMsg = BundleManager.getBundle()
                        .getString("authenticate.error.msg.content");
                }

                DialogUtil.exceptionDialog(exception,
                    txtUsername.getScene().getWindow(), errorMsg).showAndWait();

            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

}