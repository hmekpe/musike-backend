package com.musike.controller;

import com.musike.model.User;
import com.musike.util.JwtUtil;
import com.musike.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    // Debug endpoint to list all users (remove in production)
    @GetMapping("/debug/users")
    public Object getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream().map(user -> {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("email", user.getEmail() != null ? user.getEmail() : "NULL");
            userMap.put("name", user.getName() != null ? user.getName() : "NULL");
            userMap.put("username", user.getUsername() != null ? user.getUsername() : "NULL");
            userMap.put("provider", user.getProvider() != null ? user.getProvider() : "NULL");
            return userMap;
        }).toList();
    }

    @PostMapping("/register")
    public Object register(@RequestBody User registerUser) {
        try {
            logger.info("Registration attempt for email: {}", registerUser.getEmail());
            
            // Validate required fields
            if (registerUser.getEmail() == null || registerUser.getEmail().trim().isEmpty()) {
                logger.warn("Registration failed: Email is required");
                return Map.of("error", "Email is required");
            }
            if (registerUser.getPassword() == null || registerUser.getPassword().trim().isEmpty()) {
                logger.warn("Registration failed: Password is required");
                return Map.of("error", "Password is required");
            }
            if (registerUser.getName() == null || registerUser.getName().trim().isEmpty()) {
                logger.warn("Registration failed: Name is required");
                return Map.of("error", "Name is required");
            }

            // Check if user already exists
            Optional<User> existingUser = userService.findByEmail(registerUser.getEmail());
            if (existingUser.isPresent()) {
                logger.warn("Registration failed: User with email {} already exists", registerUser.getEmail());
                return Map.of("error", "User with this email already exists");
            }

            // Set default values
            registerUser.setProvider("local");
            registerUser.setEmailVerified(true);

            User savedUser = userService.addUser(registerUser);
            String token = jwtUtil.generateToken(savedUser.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                "id", savedUser.getId(),
                "email", savedUser.getEmail(),
                "name", savedUser.getName(),
                "provider", savedUser.getProvider()
            ));

            logger.info("Registration successful for email: {}", savedUser.getEmail());
            return response;
        } catch (Exception e) {
            logger.error("Registration error for email {}: {}", registerUser.getEmail(), e.getMessage(), e);
            return Map.of("error", "An error occurred during registration: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public Object login(@RequestBody User loginUser) {
        try {
            logger.info("Login attempt for email: {}", loginUser.getEmail());
            
            // Validate required fields
            if (loginUser.getEmail() == null || loginUser.getEmail().trim().isEmpty()) {
                logger.warn("Login failed: Email is required");
                return Map.of("error", "Email is required");
            }
            if (loginUser.getPassword() == null || loginUser.getPassword().trim().isEmpty()) {
                logger.warn("Login failed: Password is required");
                return Map.of("error", "Password is required");
            }

            boolean isValid = userService.validateCredentials(loginUser.getEmail(), loginUser.getPassword());
            logger.info("Credentials validation result: {}", isValid);
            
            if (isValid) {
                Optional<User> userOpt = userService.findByEmail(loginUser.getEmail());
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    String token = jwtUtil.generateToken(user.getEmail());

                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("user", Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "name", user.getName(),
                        "provider", user.getProvider()
                    ));

                    logger.info("Login successful for email: {}", user.getEmail());
                    return response;
                } else {
                    logger.warn("Login failed: User not found after validation for email: {}", loginUser.getEmail());
                    return Map.of("error", "User not found");
                }
            } else {
                logger.warn("Login failed: Invalid credentials for email: {}", loginUser.getEmail());
                return Map.of("error", "Invalid credentials");
            }
        } catch (Exception e) {
            logger.error("Login error for email {}: {}", loginUser.getEmail(), e.getMessage(), e);
            return Map.of("error", "An error occurred during login: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public Object logout() {
        // In a stateless JWT setup, logout is typically handled client-side
        // by removing the token from storage
        return Map.of("message", "Logged out successfully");
    }

    @GetMapping("/me")
    public Object getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Map.of("error", "Invalid authorization header");
            }

            String token = authHeader.substring(7);
            String email = jwtUtil.extractUsername(token);

            Optional<User> userOpt = userService.findByEmail(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                return Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "name", user.getName(),
                    "provider", user.getProvider()
                );
            }

            return Map.of("error", "User not found");
        } catch (Exception e) {
            logger.error("Error getting current user: {}", e.getMessage(), e);
            return Map.of("error", "An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/oauth/verify")
    public Object verifyOAuthToken(@RequestBody Map<String, Object> request) {
        try {
            String provider = (String) request.get("provider");
            String email = (String) request.get("email");
            String name = (String) request.get("name");
            String providerId = (String) request.get("providerId");
            String avatar = (String) request.get("avatar");

            if (email == null || email.trim().isEmpty()) {
                return Map.of("error", "Email is required");
            }

            logger.info("OAuth verification for email: {}, provider: {}", email, provider);

            // Check if user already exists
            Optional<User> existingUser = userService.findByEmail(email);
            User user;

            if (existingUser.isPresent()) {
                // Update existing user
                user = existingUser.get();
                user.setName(name != null ? name : user.getName());
                user.setProvider(provider != null ? provider : user.getProvider());
                user.setProviderId(providerId != null ? providerId : user.getProviderId());
                user.setAvatar(avatar != null ? avatar : user.getAvatar());
                user.setEmailVerified(true);
                userService.addUser(user);
                logger.info("Updated existing OAuth user: {}", email);
            } else {
                // Create new OAuth user
                user = new User();
                user.setEmail(email);
                user.setName(name != null ? name : email);
                user.setUsername(email);
                user.setProvider(provider != null ? provider : "unknown");
                user.setProviderId(providerId);
                user.setAvatar(avatar);
                user.setEmailVerified(true);
                userService.addUser(user);
                logger.info("Created new OAuth user: {}", email);
            }

            // Generate JWT token for the user
            String token = jwtUtil.generateToken(user.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "name", user.getName(),
                "provider", user.getProvider(),
                "avatar", user.getAvatar()
            ));

            logger.info("OAuth verification successful for email: {}", email);
            return response;

        } catch (Exception e) {
            logger.error("OAuth verification error: {}", e.getMessage(), e);
            return Map.of("error", "An error occurred during OAuth verification: " + e.getMessage());
        }
    }
} 