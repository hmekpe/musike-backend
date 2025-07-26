package com.musike.dto;

public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String avatar;
    private String provider;
    private String providerId;
    private boolean isEmailVerified;
    private String verificationToken;
    private String resetToken;

    public UserDto() {}

    public UserDto(Long id, String username, String email, String name, String avatar, String provider, String providerId) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.avatar = avatar;
        this.provider = provider;
        this.providerId = providerId;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getAvatar() { return avatar; }
    public String getProvider() { return provider; }
    public String getProviderId() { return providerId; }
    public boolean isEmailVerified() { return isEmailVerified; }
    public String getVerificationToken() { return verificationToken; }
    public String getResetToken() { return resetToken; }

    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setProvider(String provider) { this.provider = provider; }
    public void setProviderId(String providerId) { this.providerId = providerId; }
    public void setEmailVerified(boolean isEmailVerified) { this.isEmailVerified = isEmailVerified; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }
} 