package com.musike.service;

import com.musike.model.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final List<User> users = new ArrayList<>();

    public List<User> getAllUsers() {
        return users;
    }

    public User addUser(User user) {
        user.setId((long) (users.size() + 1));
        users.add(user);
        return user;
    }

    public Optional<User> findByUsername(String username) {
        return users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
    }

    public boolean validateCredentials(String username, String password) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username) && u.getPassword().equals(password));
    }
} 