package at.mvgeboltskirchen.kroissma.mvgapp.client.rest.implementation;

import at.mvgeboltskirchen.kroissma.mvgapp.client.exception.DataAccessException;
import at.mvgeboltskirchen.kroissma.mvgapp.client.rest.UserRestClient;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.user.UserDTO;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

@Component
public class SimpleUserRestClient implements UserRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleUserRestClient.class);
    private static final String CLIENT_URL = "/user";

    @Autowired
    private RestClient restUser;

    @Override
    public void addOrUpdateUser(UserDTO user) throws DataAccessException {
        try {
            URI uri = restUser.getServiceURI(CLIENT_URL);
            LOGGER.debug("Sending PUT request with {} to {}", user, uri);
            HttpEntity<UserDTO> userDTOHttpEntity = new HttpEntity<>(user);
            ResponseEntity<Object> response =
                restUser.exchange(
                    uri,
                    HttpMethod.PUT,
                    userDTOHttpEntity,
                    Object.class);
            LOGGER.debug("Result status was {}", response.getStatusCode());
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed to add or update user with status code " + e.getStatusCode());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<UserDTO> getAllUsers() throws DataAccessException {
        try {
            URI uri = restUser.getServiceURI(CLIENT_URL);
            LOGGER.debug("Retrieving all users from {}", uri);
            ResponseEntity<List<UserDTO>> users =
                restUser.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<UserDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", users.getStatusCode(),
                users.getBody());
            return users.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed retrieve users with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public UserDTO getUserById(long id) throws DataAccessException {
        try {
            URI uri = restUser.getServiceURI(CLIENT_URL);
            LOGGER.debug("Retrieving single user with id {} from {}", id, uri);
            Map<String, Long> params = new HashMap<>();
            params.put("id", id);
            String uriString = uri.toString() + "/{id}";
            ResponseEntity<UserDTO> user =
                restUser.exchange(
                    uriString,
                    HttpMethod.GET,
                    null,
                    UserDTO.class,
                    params);
            LOGGER.debug("Result status was {} with content {}", user.getStatusCode(),
                user.getBody());
            return user.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed retrieve user with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(long id) throws DataAccessException {
        try {
            URI uri = restUser.getServiceURI(CLIENT_URL);
            LOGGER.debug("Deleting single user with id {} from {}", id, uri);
            Map<String, Long> params = new HashMap<>();
            params.put("id", id);
            ResponseEntity<Object> response =
                restUser.exchange(
                    uri.toString() + "/{id}",
                    HttpMethod.DELETE,
                    null,
                    Object.class,
                    params);
            LOGGER.debug("Result status was {}", response.getStatusCode());
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(
                "Failed deleting user with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
