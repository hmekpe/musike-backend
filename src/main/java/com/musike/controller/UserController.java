package com.musike.controller;

import com.musike.model.User;
import com.musike.service.UserService;
import com.musike.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/profile")
    public Object getProfile(@RequestHeader("Authorization") String authHeader) {
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
                    "provider", user.getProvider(),
                    "avatar", user.getAvatar()
                );
            }

            return Map.of("error", "User not found");
        } catch (Exception e) {
            return Map.of("error", "An error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/profile")
    public Object updateProfile(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, Object> updates) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Map.of("error", "Invalid authorization header");
            }

            String token = authHeader.substring(7);
            String email = jwtUtil.extractUsername(token);

            Optional<User> userOpt = userService.findByEmail(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                // Update fields if provided
                if (updates.containsKey("name")) {
                    user.setName((String) updates.get("name"));
                }
                if (updates.containsKey("avatar")) {
                    user.setAvatar((String) updates.get("avatar"));
                }
                
                User updatedUser = userService.addUser(user);
                return Map.of(
                    "id", updatedUser.getId(),
                    "email", updatedUser.getEmail(),
                    "name", updatedUser.getName(),
                    "provider", updatedUser.getProvider(),
                    "avatar", updatedUser.getAvatar()
                );
            }

            return Map.of("error", "User not found");
        } catch (Exception e) {
            return Map.of("error", "An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/playlists")
    public List<Map<String, Object>> getPlaylists(@RequestHeader("Authorization") String authHeader) {
        // Placeholder - return empty list for now
        return new ArrayList<>();
    }

    @PostMapping("/playlists")
    public Map<String, Object> createPlaylist(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, String> playlistData) {
        // Placeholder - return success message
        return Map.of(
            "id", 1L,
            "name", playlistData.get("name"),
            "description", playlistData.get("description"),
            "message", "Playlist created successfully"
        );
    }
} 