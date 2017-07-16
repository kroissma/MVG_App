package at.mvgeboltskirchen.kroissma.mvgapp.client.rest;

import at.mvgeboltskirchen.kroissma.mvgapp.client.exception.DataAccessException;
import at.mvgeboltskirchen.kroissma.mvgapp.client.exception.ImageProcessingException;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.news.DetailedNewsDTO;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.news.SimpleNewsDTO;
import java.util.List;

public interface NewsRestClient {

    /**
     * Find all news entries.
     *
     * @return list of news entries
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleNewsDTO> findAll() throws DataAccessException;

    /**
     * Mark given news as read by the current user.
     *
     * @param simpleNewsDTO news to mark
     */
    void markNewsAsRead(SimpleNewsDTO simpleNewsDTO) throws DataAccessException;

    /**
     * Find all news entries in database which are currently not read by the current user.
     *
     * @return a list of news entries which are not read by the user
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleNewsDTO> findAllNotRead() throws DataAccessException;

    /**
     * Publishes a new news to the server.
     *
     * @param news to be published
     * @return the published news
     */
    DetailedNewsDTO publishNews(DetailedNewsDTO news)
        throws DataAccessException, ImageProcessingException;

    /**
     * Find all news entries in database which are already read by the current user.
     *
     * @return a list of news entries which are read by the user
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleNewsDTO> findAllRead() throws DataAccessException;

    /**
     * Finds a DetailedNewsDTO from a SimpleNewsDTO.
     *
     * @return DetailedNewsDTO
     */
    DetailedNewsDTO findDetailedNews(SimpleNewsDTO news) throws DataAccessException;
}
