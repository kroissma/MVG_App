package at.mvgeboltskirchen.kroissma.mvgapp.server.repository;

import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a single user by id.
     *
     * @param id the id of the user
     * @return Optional containing the user
     */
    Optional<User> findOneById(Long id);

    /**
     * Loads the user with the passed username from the database.
     *
     * @param username username of the user
     * @return user for the passed username
     */
    @Query(value = "SELECT * FROM User u WHERE u.username = :username", nativeQuery = true)
    Optional<User> findByUsername(@Param("username") String username);
}
