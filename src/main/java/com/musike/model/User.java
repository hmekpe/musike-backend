package com.musike.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String provider; // e.g., 'google', 'facebook', 'apple', or 'local'
    private String providerId; // ID from the OAuth2 provider
    private String email;
    private String name;
    private String avatar;
    private boolean isEmailVerified;
    private String verificationToken;
    private String resetToken;

    // Default constructor required by JPA
    public User() {
    }

    public User(Long id, String username, String password, String provider, String providerId, String email, String name, String avatar) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.name = name;
        this.avatar = avatar;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getProvider() { return provider; }
    public String getProviderId() { return providerId; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getAvatar() { return avatar; }
    public boolean isEmailVerified() { return isEmailVerified; }
    public String getVerificationToken() { return verificationToken; }
    public String getResetToken() { return resetToken; }

    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setProvider(String provider) { this.provider = provider; }
    public void setProviderId(String providerId) { this.providerId = providerId; }
    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setEmailVerified(boolean isEmailVerified) { this.isEmailVerified = isEmailVerified; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }
} 