package com.musike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.musike.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    
    @Autowired
    private UserService userService;
    
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    
    @SuppressWarnings("null")
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oauth2User.getAttribute("id") != null
            ? oauth2User.getAttribute("id").toString()
            : (oauth2User.getAttribute("sub") != null
                ? oauth2User.getAttribute("sub").toString()
                : oauth2User.getName());
        
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        
        if (name == null) {
            name = oauth2User.getAttribute("login"); // GitHub username
        }
        
        // Create or update user in database
        try {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setUsername(email != null ? email : name);
            user.setProvider(provider);
            user.setProviderId(providerId);
            user.setEmailVerified(true);
            
            // Check if user already exists
            Optional<User> existingUser = userService.findByEmail(email);
            if (existingUser.isPresent()) {
                // Update existing user
                User existing = existingUser.get();
                existing.setName(name);
                existing.setProvider(provider);
                existing.setProviderId(providerId);
                userService.addUser(existing);
                System.out.println("Updated existing user: " + email);
            } else {
                // Create new user
                userService.addUser(user);
                System.out.println("Created new user: " + email);
            }
        } catch (Exception e) {
            System.err.println("Error creating/updating user: " + e.getMessage());
        }
        
        System.out.println("OAuth2 Login - Provider: " + provider + ", Email: " + email + ", Name: " + name);
        
        Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
        attributes.put("provider", provider);
        attributes.put("providerId", providerId);
        
        return new DefaultOAuth2User(
            oauth2User.getAuthorities(),
            attributes,
            "login" // GitHub uses "login" as the name attribute key
        );
    }
} 