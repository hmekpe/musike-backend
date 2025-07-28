package com.musike.service;

import com.musike.model.User;
import com.musike.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        // Validate user data before saving
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        
        // Only validate password for local users (not OAuth users)
        if (user.getProvider() == null || user.getProvider().equals("local")) {
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty for local users");
            }
        }
        
        logger.info("Adding user with email: {}", user.getEmail());
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }
        return userRepository.findAll().stream()
            .filter(u -> u.getUsername() != null && u.getUsername().equals(username))
            .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return userRepository.findAll().stream()
            .filter(u -> u.getEmail() != null && email.equals(u.getEmail()))
            .findFirst();
    }

    public boolean validateCredentials(String email, String password) {
        logger.info("Validating credentials for email: {}", email);
        
        if (email == null || password == null) {
            logger.warn("Email or password is null");
            return false;
        }
        
        List<User> allUsers = userRepository.findAll();
        logger.info("Total users in database: {}", allUsers.size());
        
        for (User user : allUsers) {
            logger.debug("Checking user: id={}, email={}, password={}", 
                user.getId(), user.getEmail(), user.getPassword() != null ? "***" : "null");
        }
        
        boolean isValid = allUsers.stream()
            .anyMatch(u -> u.getEmail() != null && u.getPassword() != null && 
                         u.getEmail().equals(email) && u.getPassword().equals(password));
        
        logger.info("Credentials validation result: {}", isValid);
        return isValid;
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