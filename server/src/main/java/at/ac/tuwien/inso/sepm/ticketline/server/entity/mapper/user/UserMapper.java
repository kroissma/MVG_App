package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    List<UserDTO> usersToUserDTOs(List<User> all);

    UserDTO userToUserDTO(User one);

    User userDTOToUser(UserDTO userDTO);
}
