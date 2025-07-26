package com.musike.service;

import com.musike.model.User;
import com.musike.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findAll().stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findAll().stream()
            .filter(u -> email != null && email.equals(u.getEmail()))
            .findFirst();
    }

    public boolean validateCredentials(String username, String password) {
        return userRepository.findAll().stream()
            .anyMatch(u -> u.getUsername().equals(username) && u.getPassword().equals(password));
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, com.musike.dto.UserDto userDto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (userDto.getUsername() != null) user.setUsername(userDto.getUsername());
            if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
            if (userDto.getName() != null) user.setName(userDto.getName());
            if (userDto.getAvatar() != null) user.setAvatar(userDto.getAvatar());
            if (userDto.getProvider() != null) user.setProvider(userDto.getProvider());
            if (userDto.getProviderId() != null) user.setProviderId(userDto.getProviderId());
            return userRepository.save(user);
        }
        return null;
    }
} 