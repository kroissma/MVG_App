package at.mvgeboltskirchen.kroissma.mvgapp.client.service.implementation;

import at.mvgeboltskirchen.kroissma.mvgapp.client.exception.DataAccessException;
import at.mvgeboltskirchen.kroissma.mvgapp.client.rest.UserRestClient;
import at.mvgeboltskirchen.kroissma.mvgapp.client.service.UserService;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.user.UserDTO;
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
