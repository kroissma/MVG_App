package at.mvgeboltskirchen.kroissma.mvgapp.client.rest.interceptor;

import java.io.IOException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class LocaleInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
        ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add(HttpHeaders.ACCEPT_LANGUAGE,
            StringUtils.toLanguageTag(LocaleContextHolder.getLocale()));
        return execution.execute(request, body);
    }

}
