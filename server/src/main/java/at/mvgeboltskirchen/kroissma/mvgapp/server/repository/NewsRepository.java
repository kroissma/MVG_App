package at.mvgeboltskirchen.kroissma.mvgapp.server.repository;

import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.News;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Find a single news entry by id.
     *
     * @param id the id of the news entry
     * @return Optional containing the news entry
     */
    Optional<News> findOneById(Long id);

    /**
     * Find all news entries ordered by published at date (descending).
     *
     * @return ordered list of al news entries
     */
    List<News> findAllByOrderByPublishedAtDesc();

    /**
     * Find not read news entries ordered by published at date (descending).
     *
     * @return ordered list of all news entries
     */
    @Query(value = "Select * from News n "
        + "where n.id not in (select r.news_id from User u "
        + "join user_read_news r on (Select id from user where username = ?1) = r.user_id) "
        + "ORDER BY published_at DESC"
        , nativeQuery = true)
    List<News> findNotReadByOrderByPublishedAtDesc(String username);

    /**
     * Finds all read news entries  ordered by published at date (descending).
     *
     * @param username user
     * @return ordered list of all news entries
     */
    @Query(value = "select n.* \n" +
        "    from (user u join user_read_news rn on rn.user_id=u.id) \n" +
        "    join news n on n.id=rn.news_id \n" +
        "    where u.username = :username ;", nativeQuery = true)
    List<News> findReadByOrderByPublishedAtDesc(@Param("username") String username);
}
