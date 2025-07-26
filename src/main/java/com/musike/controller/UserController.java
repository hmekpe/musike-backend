package com.musike.controller;

import com.musike.model.User;
import com.musike.service.UserService;
import com.musike.dto.UserDto;
import com.musike.service.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return userService.getAllUsers().stream()
            .map(UserMapper::toDto)
            .collect(Collectors.toList());
    }

    @PostMapping("/users")
    public UserDto registerUser(@RequestBody UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        User saved = userService.addUser(user);
        return UserMapper.toDto(saved);
    }

    @GetMapping("/users/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUserById(id)
            .map(UserMapper::toDto)
            .orElse(null);
    }

    @PutMapping("/users/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User updated = userService.updateUser(id, userDto);
        return UserMapper.toDto(updated);
    }
} 