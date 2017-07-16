package at.mvgeboltskirchen.kroissma.mvgapp.server.entity.mapper.user;

import at.mvgeboltskirchen.kroissma.mvgapp.rest.user.UserDTO;
import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.User;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    List<UserDTO> usersToUserDTOs(List<User> all);

    UserDTO userToUserDTO(User one);

    User userDTOToUser(UserDTO userDTO);
}
