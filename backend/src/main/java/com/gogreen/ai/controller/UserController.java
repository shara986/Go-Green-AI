package com.gogreen.ai.controller;

import com.gogreen.ai.dto.ApiResponse;
import com.gogreen.ai.dto.request.UpdateProfileRequestDto;
import com.gogreen.ai.dto.response.UserResponseDto;
import com.gogreen.ai.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getCurrentUserProfile(Authentication authentication) {
        UserResponseDto responseDto = userService.getCurrentUserProfile(authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile fetched successfully", responseDto));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateCurrentUserProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequestDto requestDto) {
        UserResponseDto responseDto = userService.updateCurrentUserProfile(authentication.getName(), requestDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated successfully", responseDto));
    }
}
