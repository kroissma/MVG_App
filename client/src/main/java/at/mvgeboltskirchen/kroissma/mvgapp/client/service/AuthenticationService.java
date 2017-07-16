package at.mvgeboltskirchen.kroissma.mvgapp.client.service;

import at.mvgeboltskirchen.kroissma.mvgapp.client.exception.DataAccessException;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.authentication.AuthenticationRequest;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.authentication.AuthenticationTokenInfo;

public interface AuthenticationService {

    /**
     * Authenticate with username and password.
     *
     * @param authenticationRequest containing the username and password to authenticate
     * @return A valid authentication token
     * @throws DataAccessException in case something went wrong
     */
    AuthenticationTokenInfo authenticate(AuthenticationRequest authenticationRequest)
        throws DataAccessException;

    /**
     * Deauthenticate current user
     */
    void deAuthenticate();

}
