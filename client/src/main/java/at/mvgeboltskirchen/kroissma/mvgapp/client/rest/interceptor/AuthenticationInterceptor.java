package at.mvgeboltskirchen.kroissma.mvgapp.client.rest.interceptor;

import at.mvgeboltskirchen.kroissma.mvgapp.client.service.AuthenticationInformationService;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationInterceptor implements ClientHttpRequestInterceptor {

    private final AuthenticationInformationService authenticationInformationService;

    public AuthenticationInterceptor(
        AuthenticationInformationService authenticationInformationService) {
        this.authenticationInformationService = authenticationInformationService;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
        ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        authenticationInformationService.getCurrentAuthenticationToken().ifPresent(
            authenticationToken -> headers.add("Authorization", "Bearer " + authenticationToken));
        return execution.execute(request, body);
    }
}
