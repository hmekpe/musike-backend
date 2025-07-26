package com.musike.service;

import com.musike.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String provider = (String) oauth2User.getAttribute("provider");
        
        if (name == null) {
            name = oauth2User.getAttribute("login"); // GitHub username
        }
        
        // Ensure we have a valid identifier for JWT
        String identifier = email != null ? email : (name != null ? name : "unknown");
        
        // Generate JWT token
        String token = jwtUtil.generateToken(identifier);
        
        // Redirect to frontend with token
        String redirectUrl = "http://localhost:19006/oauth-success?token=" + token + "&provider=" + (provider != null ? provider : "unknown");
        response.sendRedirect(redirectUrl);
        
        System.out.println("OAuth2 Success - Provider: " + provider + ", Email: " + email + ", Name: " + name);
    }
} 