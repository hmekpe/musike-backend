package com.musike.config;

import com.musike.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
        @org.springframework.lang.NonNull HttpServletRequest request,
        @org.springframework.lang.NonNull HttpServletResponse response,
        @org.springframework.lang.NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // Log the exception but don't throw it to avoid breaking the filter chain
                System.out.println("JWT token validation failed: " + e.getMessage());
            }
        }

        if (username != null && org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication() == null) {
            // Here you would typically load user details from database
            // For now, we'll create a simple authentication
            org.springframework.security.core.userdetails.UserDetails userDetails = 
                org.springframework.security.core.userdetails.User.withUsername(username)
                    .password("") // No password check needed for JWT
                    .authorities("USER")
                    .build();

            if (jwtUtil.validateToken(jwt, username)) {
                org.springframework.security.authentication.UsernamePasswordAuthenticationToken authToken = 
                    new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
} 