package at.mvgeboltskirchen.kroissma.mvgapp.client.rest.implementation;

import at.mvgeboltskirchen.kroissma.mvgapp.client.exception.DataAccessException;
import at.mvgeboltskirchen.kroissma.mvgapp.client.rest.LogoRestClient;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.logo.LogoDTO;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

@Component
public class SimpleLogoRestClient implements LogoRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLogoRestClient.class);
    private static final String LOGO_URL = "/logo";

    private RestClient restClient;

    public SimpleLogoRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<LogoDTO> findAll() throws DataAccessException {
        try {
            URI uri = restClient.getServiceURI(LOGO_URL);
            LOGGER.debug("Retrieving logos from {}", uri);
            ResponseEntity<List<LogoDTO>> logos =
                restClient.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<LogoDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", logos.getStatusCode(),
                logos.getBody());
            return logos.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed to retrieve logos with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

}
