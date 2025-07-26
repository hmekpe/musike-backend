package com.musike.service;

import com.musike.model.User;
import com.musike.dto.UserDto;

public class UserMapper {
    public static UserDto toDto(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getName(),
            user.getAvatar(),
            user.getProvider(),
            user.getProviderId()
        );
        // Set new fields
        dto.setEmailVerified(user.isEmailVerified());
        dto.setVerificationToken(user.getVerificationToken());
        dto.setResetToken(user.getResetToken());
        return dto;
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;
        User user = new User(
            dto.getId(),
            dto.getUsername(),
            null, // password should be set separately
            dto.getProvider(),
            dto.getProviderId(),
            dto.getEmail(),
            dto.getName(),
            dto.getAvatar()
        );
        // Set new fields
        user.setEmailVerified(dto.isEmailVerified());
        user.setVerificationToken(dto.getVerificationToken());
        user.setResetToken(dto.getResetToken());
        return user;
    }
} 