package at.mvgeboltskirchen.kroissma.mvgapp.client.rest;

import at.mvgeboltskirchen.kroissma.mvgapp.client.exception.DataAccessException;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.info.Info;

public interface InfoRestClient {

    /**
     * Load info object
     *
     * @return info object representing information from the ticketline server
     * @throws DataAccessException in case something went wrong
     */
    Info find() throws DataAccessException;

}
