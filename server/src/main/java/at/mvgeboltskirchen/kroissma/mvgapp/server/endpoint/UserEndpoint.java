package at.mvgeboltskirchen.kroissma.mvgapp.server.endpoint;

import at.mvgeboltskirchen.kroissma.mvgapp.rest.user.UserDTO;
import at.mvgeboltskirchen.kroissma.mvgapp.server.configuration.SecurityConfiguration;
import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.User;
import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.mapper.user.UserMapper;
import at.mvgeboltskirchen.kroissma.mvgapp.server.exception.IllegalUserException;
import at.mvgeboltskirchen.kroissma.mvgapp.server.security.AuthenticationConstants;
import at.mvgeboltskirchen.kroissma.mvgapp.server.service.HeaderTokenAuthenticationService;
import at.mvgeboltskirchen.kroissma.mvgapp.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
@Api(value = "user")
public class UserEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEndpoint.class);

    private final UserService userService;
    private final UserMapper userMapper;
    private final HeaderTokenAuthenticationService authenticationService;

    public UserEndpoint(UserService userService,
        UserMapper userMapper, HeaderTokenAuthenticationService headerTokenAuthenticationService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationService = headerTokenAuthenticationService;
    }

    private String getUsername(String authorizationHeader) {
        return authenticationService.authenticationTokenInfo(
            authorizationHeader.substring(AuthenticationConstants.TOKEN_PREFIX.length()).trim())
            .getUsername();
    }

    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Adds a user")
    public void addUser(@RequestBody UserDTO userDTO,
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader,
        HttpServletResponse response) {
        User userToAdd = userMapper.userDTOToUser(userDTO);
        User existingUser = null;
        try {
            existingUser = userService.getUserByUsername(userToAdd.getUsername());
        } catch (Exception e) {
        }

        // Only encrypt the password if it changed or if it is a new user
        if (existingUser == null || !userToAdd.getPassword().equals(existingUser.getPassword())) {
            userToAdd.setPassword(SecurityConfiguration.configureDefaultPasswordEncoder()
                .encode(userToAdd.getPassword()));
        }
        // Special case to not allow admins to get locked this way
        if (existingUser != null && !existingUser.getLocked() && userToAdd.getLocked()
            && getUsername(authorizationHeader).equals(userToAdd.getUsername())) {
            userToAdd.setLocked(false);
        }
        try {
            userService.addUser(userToAdd);
            LOGGER.info("Add or update user with id {}.", userToAdd.getId());
        } catch (IllegalUserException e) {
            LOGGER.warn("Failed to add or update user with id {}.", userToAdd.getId());
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Get all users")
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = userMapper.usersToUserDTOs(userService.getAllUsers());
        LOGGER.info("Sending {} users from database.", users.size());
        return users;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Get a single user by id")
    public UserDTO getUserById(@PathVariable long id) {
        UserDTO userDTO = userMapper.userToUserDTO(userService.getUserById(id));
        LOGGER.info("Sending user with id {}.", userDTO.getId());
        return userDTO;
    }

    /*
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete a user")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }*/
}
