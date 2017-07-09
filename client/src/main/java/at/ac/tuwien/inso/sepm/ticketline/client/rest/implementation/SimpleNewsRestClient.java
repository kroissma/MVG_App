package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ImageProcessingException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.NewsRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;


@Component
public class SimpleNewsRestClient implements NewsRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNewsRestClient.class);
    private static final String NEWS_URL = "/news";

    private final RestClient restClient;

    public SimpleNewsRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<SimpleNewsDTO> findAll() throws DataAccessException {
        try {
            URI uri = restClient.getServiceURI(NEWS_URL);
            LOGGER.debug("Retrieving all news from {}", uri);
            ResponseEntity<List<SimpleNewsDTO>> news =
                restClient.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SimpleNewsDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", news.getStatusCode(),
                news.getBody());
            return news.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed retrieve news with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<SimpleNewsDTO> findAllNotRead() throws DataAccessException {
        try {
            URI uri = restClient.getServiceURI(NEWS_URL + "/notread");
            LOGGER.debug("Retrieving unread news from {}", uri);
            ResponseEntity<List<SimpleNewsDTO>> news =
                restClient.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SimpleNewsDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", news.getStatusCode(),
                news.getBody());
            return news.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed retrieve not read news with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void markNewsAsRead(SimpleNewsDTO simpleNewsDTO)
        throws DataAccessException {
        try {
            URI uri = restClient.getServiceURI(NEWS_URL + "/read");
            String uriString = uri.toString() + "/{id}";
            LOGGER.debug("Sending mark_as_read request for news id: {} by current user to {}",
                simpleNewsDTO.getId(), uri.toString() + "/" + simpleNewsDTO.getId());
            Map<String, Long> params = new HashMap<>();
            params.put("id", simpleNewsDTO.getId());
            ResponseEntity<Object> response = restClient.exchange(
                uriString,
                HttpMethod.PUT,
                null,
                Object.class,
                params
            );
            LOGGER.debug("Result status was {}", response.getStatusCode());
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed retrieve news with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public DetailedNewsDTO publishNews(DetailedNewsDTO news)
        throws DataAccessException, ImageProcessingException {
        try {
            URI uri = restClient.getServiceURI(NEWS_URL);
            LOGGER.debug("Sending POST request with {} to {}", news, uri);
            HttpEntity<DetailedNewsDTO> detailedNewsDTOHttpEntity = new HttpEntity<>(news);
            ResponseEntity<DetailedNewsDTO> response =
                restClient.exchange(
                    uri,
                    HttpMethod.POST,
                    detailedNewsDTOHttpEntity,
                    new ParameterizedTypeReference<DetailedNewsDTO>() {
                    });
            LOGGER.debug("Result status was {} return body {}", response.getStatusCode(),
                response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                throw new ImageProcessingException();
            } else {
                throw new DataAccessException(
                    "Failed to publish news with status code " + e.getStatusCode().toString()
                        + "\n"
                        + "Message: " + e.getMessage()
                );
            }
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<SimpleNewsDTO> findAllRead() throws DataAccessException {
        try {
            URI uri = restClient.getServiceURI(NEWS_URL + "/read");
            LOGGER.debug("Retrieving read news from {}", uri);
            ResponseEntity<List<SimpleNewsDTO>> news =
                restClient.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SimpleNewsDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", news.getStatusCode(),
                news.getBody());
            return news.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed retrieve read news with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public DetailedNewsDTO findDetailedNews(SimpleNewsDTO news) throws DataAccessException {
        try {
            URI uri = restClient.getServiceURI(NEWS_URL);
            LOGGER.debug("Sending GET request with id {} to {}", news.getId(), uri);
            Map<String, Long> params = new HashMap<>();
            params.put("id", news.getId());
            String uriString = uri.toString() + "/{id}";
            ResponseEntity<DetailedNewsDTO> response =
                restClient.exchange(
                    uriString,
                    HttpMethod.GET,
                    null,
                    DetailedNewsDTO.class,
                    params
                );
            LOGGER.debug("Result status was {} return body {}", response.getStatusCode(),
                response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed to retrieve news with status code " + e.getStatusCode().toString()
                    + "\n"
                    + "Message: " + e.getMessage()
            );
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
