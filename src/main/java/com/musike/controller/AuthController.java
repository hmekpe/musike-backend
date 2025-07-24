package com.musike.controller;

import com.musike.model.User;
import com.musike.util.JwtUtil;
import com.musike.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    // Remove test user initialization for production
    // public AuthController() {
    //     users.add(new User(1L, "testuser", "password"));
    // }

    @PostMapping("/login")
    public Object login(@RequestBody User loginUser) {
        if (userService.validateCredentials(loginUser.getUsername(), loginUser.getPassword())) {
            String token = jwtUtil.generateToken(loginUser.getUsername());
            return java.util.Collections.singletonMap("token", token);
        }
        return java.util.Collections.singletonMap("error", "Invalid credentials");
    }
} 