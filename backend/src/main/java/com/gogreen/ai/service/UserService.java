package com.gogreen.ai.service;

import com.gogreen.ai.dto.request.UpdateProfileRequestDto;
import com.gogreen.ai.dto.response.UserResponseDto;

public interface UserService {

    UserResponseDto getCurrentUserProfile(String usernameOrEmail);

    UserResponseDto updateCurrentUserProfile(String usernameOrEmail, UpdateProfileRequestDto requestDto);

    void changePassword(String usernameOrEmail, String currentPassword, String newPassword);
}
