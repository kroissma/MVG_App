package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.UserRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleUserService implements UserService {

    @Autowired
    private UserRestClient userRestClient;

    @Override
    public void addOrUpdateUser(UserDTO user) throws DataAccessException {
        userRestClient.addOrUpdateUser(user);
    }

    @Override
    public List<UserDTO> getAllUsers() throws DataAccessException {
        return userRestClient.getAllUsers();
    }

    @Override
    public UserDTO getUserById(long id) throws DataAccessException {
        return userRestClient.getUserById(id);
    }

    @Override
    public void deleteUser(long id) throws DataAccessException {
        userRestClient.deleteUser(id);
    }
}
