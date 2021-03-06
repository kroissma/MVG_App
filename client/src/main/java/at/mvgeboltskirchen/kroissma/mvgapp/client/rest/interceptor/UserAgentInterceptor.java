package at.mvgeboltskirchen.kroissma.mvgapp.client.rest.interceptor;

import at.mvgeboltskirchen.kroissma.mvgapp.client.configuration.properties.RestClientConfigurationProperties;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class UserAgentInterceptor implements ClientHttpRequestInterceptor {

    private final String userAgent;

    public UserAgentInterceptor(
        RestClientConfigurationProperties restClientConfigurationProperties) {
        userAgent = restClientConfigurationProperties.getUserAgent();
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
        ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        if (userAgent != null) {
            headers.add(HttpHeaders.USER_AGENT, userAgent);
        }
        return execution.execute(request, body);
    }

}
