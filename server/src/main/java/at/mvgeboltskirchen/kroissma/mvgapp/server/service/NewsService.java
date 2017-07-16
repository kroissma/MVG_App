package at.mvgeboltskirchen.kroissma.mvgapp.server.service;

import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.News;
import java.util.List;

public interface NewsService {

    /**
     * Find all news entries ordered by published at date (descending).
     *
     * @return ordered list of all news entries
     */
    List<News> findAll();

    /**
     * Find all news entries ordered by published at date (descending) which where not read by the
     * user.
     *
     * @param username the user for which you want to get the news
     * @return ordered list of news entries not read by the specified user
     */
    List<News> findAllNotRead(String username);

    /**
     * Find a single news entry by id.
     *
     * @param id the id of the news entry
     * @return the news entry
     */
    News findOne(Long id);

    /**
     * Publish a single news entry
     *
     * @param news to publish
     * @return published news entry
     */
    News publishNews(News news);

    /**
     * Find all news entries ordered by published at date (descending) which are already read by the
     * user.
     *
     * @param username the user for which you want to get the news
     * @return ordered list of news entries read by the specified user
     */
    List<News> findAllRead(String username);
}
