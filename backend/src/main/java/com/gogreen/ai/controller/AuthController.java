package com.gogreen.ai.controller;

import com.gogreen.ai.dto.ApiResponse;
import com.gogreen.ai.dto.request.ChangePasswordRequestDto;
import com.gogreen.ai.dto.request.ForgotPasswordRequestDto;
import com.gogreen.ai.dto.request.LoginRequestDto;
import com.gogreen.ai.dto.request.RefreshTokenRequestDto;
import com.gogreen.ai.dto.request.RegisterRequestDto;
import com.gogreen.ai.dto.request.ResetPasswordRequestDto;
import com.gogreen.ai.dto.request.VerifyEmailRequestDto;
import com.gogreen.ai.dto.request.VerifyOtpRequestDto;
import com.gogreen.ai.dto.response.AuthResponseDto;
import com.gogreen.ai.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Registration, login, refresh token, password, and email verification flows")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register a customer", description = "Creates a new customer account and returns JWT tokens")
    @PostMapping("/register/customer")
    public ResponseEntity<ApiResponse<AuthResponseDto>> registerCustomer(@Valid @RequestBody RegisterRequestDto requestDto) {
        return new ResponseEntity<>(new ApiResponse<>(true, "Customer registered successfully", authService.registerCustomer(requestDto)), HttpStatus.CREATED);
    }

    @PostMapping("/register/nursery")
    public ResponseEntity<ApiResponse<AuthResponseDto>> registerNurseryOwner(@Valid @RequestBody RegisterRequestDto requestDto) {
        return new ResponseEntity<>(new ApiResponse<>(true, "Nursery owner registration submitted for admin approval", authService.registerNurseryOwner(requestDto)), HttpStatus.CREATED);
    }

    @PostMapping("/register/expert")
    public ResponseEntity<ApiResponse<AuthResponseDto>> registerGardeningExpert(@Valid @RequestBody RegisterRequestDto requestDto) {
        return new ResponseEntity<>(new ApiResponse<>(true, "Gardening expert registration submitted for admin approval", authService.registerGardeningExpert(requestDto)), HttpStatus.CREATED);
    }

    @PostMapping("/register/delivery")
    public ResponseEntity<ApiResponse<AuthResponseDto>> registerDeliveryPartner(@Valid @RequestBody RegisterRequestDto requestDto) {
        return new ResponseEntity<>(new ApiResponse<>(true, "Delivery partner registration submitted for admin approval", authService.registerDeliveryPartner(requestDto)), HttpStatus.CREATED);
    }

    @Operation(summary = "Sign in", description = "Authenticates a user and returns access and refresh tokens")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", authService.login(loginRequestDto)));
    }

    @Operation(summary = "Refresh tokens", description = "Issues a new access token using a valid refresh token")
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponseDto>> refreshToken(@Valid @RequestBody RefreshTokenRequestDto requestDto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed successfully", authService.refreshToken(requestDto)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(Authentication authentication) {
        authService.logout(authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Logout successful", null));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto requestDto) {
        authService.forgotPassword(requestDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Password reset instructions have been prepared", null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequestDto requestDto) {
        authService.resetPassword(requestDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Password reset successfully", null));
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(Authentication authentication,
                                                               @Valid @RequestBody ChangePasswordRequestDto requestDto) {
        authService.changePassword(authentication.getName(), requestDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Password changed successfully", null));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@Valid @RequestBody VerifyEmailRequestDto requestDto) {
        authService.verifyEmail(requestDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Email verification request received", null));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<String>> verifyOtp(@Valid @RequestBody VerifyOtpRequestDto requestDto) {
        authService.verifyOtp(requestDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "OTP verification request received", null));
    }
}
