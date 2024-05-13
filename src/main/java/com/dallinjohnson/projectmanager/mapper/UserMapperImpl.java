package com.dallinjohnson.projectmanager.mapper;

import com.dallinjohnson.projectmanager.domain.User;
import com.dallinjohnson.projectmanager.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserDTO mapToDTO(User user) {
        if (user == null) { return null; }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());

        return userDTO;
    }

    @Override
    public User mapToEntity(UserDTO userDTO) {
        if (userDTO == null) { return null; }

        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());

        return user;
    }
}
