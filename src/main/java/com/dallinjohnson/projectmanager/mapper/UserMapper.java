package com.dallinjohnson.projectmanager.mapper;

import com.dallinjohnson.projectmanager.domain.User;
import com.dallinjohnson.projectmanager.dto.UserDTO;

public interface UserMapper {
    UserDTO mapToDTO(User user);
    User mapToEntity(UserDTO userDTO);
}
