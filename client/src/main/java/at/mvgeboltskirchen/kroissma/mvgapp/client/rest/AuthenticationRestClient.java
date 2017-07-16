package at.mvgeboltskirchen.kroissma.mvgapp.client.rest;

import at.mvgeboltskirchen.kroissma.mvgapp.client.exception.DataAccessException;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.authentication.AuthenticationRequest;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.authentication.AuthenticationToken;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.authentication.AuthenticationTokenInfo;

public interface AuthenticationRestClient {

    /**
     * Authenticate with username and password.
     *
     * @param authenticationRequest containing the username and password to authenticate
     * @return A valid authentication token
     * @throws DataAccessException in case something went wrong
     */
    AuthenticationToken authenticate(AuthenticationRequest authenticationRequest)
        throws DataAccessException;

    /**
     * Renew authentication with the current authentication token.
     *
     * @return A valid authentication token
     * @throws DataAccessException in case something went wrong
     */
    AuthenticationToken authenticate() throws DataAccessException;

    /**
     * @return Informations about the current authentication
     * @throws DataAccessException in case something went wrong
     */
    AuthenticationTokenInfo tokenInfoCurrent() throws DataAccessException;

}
