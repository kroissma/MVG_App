package at.mvgeboltskirchen.kroissma.mvgapp.client.service.implementation;

import at.mvgeboltskirchen.kroissma.mvgapp.client.service.AuthenticationInformationService;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.authentication.AuthenticationTokenInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import org.springframework.stereotype.Service;

@Service
public class SimpleAuthenticationInformationService implements AuthenticationInformationService {

    private String currentAuthenticationToken;

    private AuthenticationTokenInfo currentAuthenticationTokenInfo;
    private List<AuthenticationChangeListener> changeListener = new ArrayList<>();

    @Override
    public Optional<String> getCurrentAuthenticationToken() {
        return Optional.ofNullable(currentAuthenticationToken);
    }

    @Override
    public void setCurrentAuthenticationToken(String currentAuthenticationToken) {
        this.currentAuthenticationToken = currentAuthenticationToken;
    }

    @Override
    public Optional<AuthenticationTokenInfo> getCurrentAuthenticationTokenInfo() {
        return Optional.ofNullable(currentAuthenticationTokenInfo);
    }

    @Override
    public void setCurrentAuthenticationTokenInfo(
        AuthenticationTokenInfo currentAuthenticationTokenInfo) {
        this.currentAuthenticationTokenInfo = currentAuthenticationTokenInfo;
        changeListener.forEach(authenticationChangeListener ->
            Platform.runLater(
                () -> authenticationChangeListener.changed(this.currentAuthenticationTokenInfo)
            ));
    }

    @Override
    public void clearAuthentication() {
        currentAuthenticationToken = null;
        currentAuthenticationTokenInfo = null;
        changeListener.forEach(authenticationChangeListener ->
            authenticationChangeListener.changed(currentAuthenticationTokenInfo));
    }

    @Override
    public void addAuthenticationChangeListener(AuthenticationChangeListener changeListener) {
        this.changeListener.add(changeListener);
    }


}
