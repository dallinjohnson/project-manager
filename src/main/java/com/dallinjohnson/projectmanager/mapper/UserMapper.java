package com.dallinjohnson.projectmanager.mapper;

import com.dallinjohnson.projectmanager.domain.User;
import com.dallinjohnson.projectmanager.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);
}
