package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.IllegalNewsException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.IllegalUserException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    /**
     * Find a single news entry by id.
     *
     * @param id the is of the news entry
     * @return the news entry
     */
    User findOne(Long id);

    /**
     * Mark the given news as read by the given user.
     *
     * @param news read news
     * @param user user who read the news
     */
    void markNewsAsRead(News news, User user) throws IllegalUserException, IllegalNewsException;

    /**
     * gets the user that is currently logged in
     *
     * @return an Optional containing a user
     */
    Optional<User> getLoggedInUser();

    /**
     * Adds a new user.
     *
     * @param user user to add
     */
    void addUser(User user) throws IllegalUserException;

    /**
     * Gets all users.
     *
     * @return all users
     */
    List<User> getAllUsers();


    /**
     * Get a single user by id.
     *
     * @param id user
     * @return single user with given id
     */
    User getUserById(Long id);

    /**
     * Get a single user by username.
     *
     * @param username username
     * @return single user with given username
     */
    User getUserByUsername(String username);

    /**
     * Deletes a single user.
     *
     * @param id user
     */
    void deleteUser(long id);

}
