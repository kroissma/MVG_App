package at.mvgeboltskirchen.kroissma.mvgapp.client.service.implementation;

import at.mvgeboltskirchen.kroissma.mvgapp.client.exception.DataAccessException;
import at.mvgeboltskirchen.kroissma.mvgapp.client.rest.AuthenticationRestClient;
import at.mvgeboltskirchen.kroissma.mvgapp.client.service.AuthenticationInformationService;
import at.mvgeboltskirchen.kroissma.mvgapp.client.service.AuthenticationService;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.authentication.AuthenticationRequest;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.authentication.AuthenticationToken;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.authentication.AuthenticationTokenInfo;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ScheduledFuture;
import javafx.application.Platform;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

@Service
public class SimpleAuthenticationService implements AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAuthenticationService.class);

    private final AuthenticationRestClient authenticationRestClient;
    private final AuthenticationInformationService authenticationInformationService;
    private final ThreadPoolTaskScheduler taskScheduler;

    private AuthenticationToken authenticationToken;
    private ScheduledFuture<?> schedule;

    public SimpleAuthenticationService(AuthenticationRestClient authenticationRestClient,
        AuthenticationInformationService authenticationInformationService) {
        this.authenticationInformationService = authenticationInformationService;
        this.authenticationRestClient = authenticationRestClient;
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.initialize();
    }

    @Override
    public AuthenticationTokenInfo authenticate(AuthenticationRequest authenticationRequest)
        throws DataAccessException {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("trying to authenticate {}", authenticationRequest);
        } else {
            LOGGER.debug("trying to authenticate");
        }
        return authenticateAndScheduleNextAuthentication(authenticationRequest);
    }

    private AuthenticationTokenInfo authenticateAndScheduleNextAuthentication()
        throws DataAccessException {
        return authenticateAndScheduleNextAuthentication(null);
    }

    private AuthenticationTokenInfo authenticateAndScheduleNextAuthentication(
        AuthenticationRequest authenticationRequest) throws DataAccessException {
        if (null == authenticationRequest) {
            authenticationToken = authenticationRestClient.authenticate();
        } else {
            authenticationToken = authenticationRestClient.authenticate(authenticationRequest);
        }
        authenticationInformationService
            .setCurrentAuthenticationToken(authenticationToken.getCurrentToken());
        AuthenticationTokenInfo authenticationTokenInfo = authenticationRestClient
            .tokenInfoCurrent();
        scheduleReauthenticationTask(authenticationTokenInfo.getExpireAt()
            .minus(authenticationTokenInfo.getOverlapDuration().dividedBy(2)));
        authenticationInformationService.setCurrentAuthenticationTokenInfo(authenticationTokenInfo);
        return authenticationTokenInfo;
    }

    private void scheduleReauthenticationTask(LocalDateTime runAt) {
        schedule = taskScheduler.schedule(
            () -> {
                LOGGER.debug("setting current token to future token");
                if (authenticationToken != null) {
                    authenticationInformationService
                        .setCurrentAuthenticationToken(authenticationToken.getFutureToken());
                    LOGGER.debug("trying to re-authenticate {}", authenticationToken);
                }
                try {
                    authenticateAndScheduleNextAuthentication();
                } catch (DataAccessException e) {
                    deAuthenticate();
                }
            },
            Date.from(runAt.atZone(ZoneId.systemDefault()).toInstant()));
    }

    @Override
    public void deAuthenticate() {
        LOGGER.debug("de authenticating");
        Platform.runLater(() -> {
            authenticationInformationService.clearAuthentication();
        });
        authenticationToken = null;
        //taskScheduler.shutdown();
        if (schedule != null) {
            schedule.cancel(true);
        }
    }

    @PreDestroy
    public void preDestroy() {
        deAuthenticate();
        taskScheduler.shutdown();
    }

}
