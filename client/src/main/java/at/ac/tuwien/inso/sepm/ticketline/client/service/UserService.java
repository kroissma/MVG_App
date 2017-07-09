package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import java.util.List;

public interface UserService {

    /**
     * Adds a new user or updates a user if id already exits.
     *
     * @param user new or updated user
     */
    void addOrUpdateUser(UserDTO user) throws DataAccessException;

    /**
     * Gets all users.
     *
     * @return all users
     */
    List<UserDTO> getAllUsers() throws DataAccessException;

    /**
     * Gets a single user with the given id.
     *
     * @param id user
     * @return the user
     */
    UserDTO getUserById(long id) throws DataAccessException;

    /**
     * Deletes the user with the given id.
     *
     * @param id user
     */
    void deleteUser(long id) throws DataAccessException;
}
