package com.dallinjohnson.projectmanager.controller;

import com.dallinjohnson.projectmanager.domain.User;
import com.dallinjohnson.projectmanager.dto.UserDTO;
import com.dallinjohnson.projectmanager.mapper.UserMapper;
import com.dallinjohnson.projectmanager.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(("/api/users"))
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable @Positive Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        UserDTO userDTO = userMapper.mapToDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        User user = userMapper.mapToEntity(userDTO);
        User savedUser = userService.save(user);
        UserDTO savedUserDTO = userMapper.mapToDTO(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDTO);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable @Positive Long userId, @Valid @RequestBody UserDTO userDTO) {
        User user = userMapper.mapToEntity(userDTO);
        user.setId(userId);
        User updatedUser = userService.update(userId, user);
        UserDTO updatedUserDTO = userMapper.mapToDTO(updatedUser);
        return ResponseEntity.ok(updatedUserDTO);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Positive Long userId) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
