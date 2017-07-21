package at.mvgeboltskirchen.kroissma.mvgapp.client.service;

import at.mvgeboltskirchen.kroissma.mvgapp.client.exception.DataAccessException;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.logo.LogoDTO;
import java.util.List;

public interface LogoService {

    /**
     * Finds all logos in the database
     *
     * @return a List of all logos in the Database
     * @throws DataAccessException if the data could not be loaded from the server
     */
    List<LogoDTO> findAll() throws DataAccessException;
}
