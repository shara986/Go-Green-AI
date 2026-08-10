package com.gogreen.ai.service;

import com.gogreen.ai.dto.request.ChangePasswordRequestDto;
import com.gogreen.ai.dto.request.ForgotPasswordRequestDto;
import com.gogreen.ai.dto.request.LoginRequestDto;
import com.gogreen.ai.dto.request.RefreshTokenRequestDto;
import com.gogreen.ai.dto.request.RegisterRequestDto;
import com.gogreen.ai.dto.request.ResetPasswordRequestDto;
import com.gogreen.ai.dto.request.VerifyEmailRequestDto;
import com.gogreen.ai.dto.request.VerifyOtpRequestDto;
import com.gogreen.ai.dto.response.AuthResponseDto;

public interface AuthService {

    AuthResponseDto login(LoginRequestDto loginRequestDto);

    AuthResponseDto registerCustomer(RegisterRequestDto requestDto);

    AuthResponseDto registerNurseryOwner(RegisterRequestDto requestDto);

    AuthResponseDto registerGardeningExpert(RegisterRequestDto requestDto);

    AuthResponseDto registerDeliveryPartner(RegisterRequestDto requestDto);

    AuthResponseDto refreshToken(RefreshTokenRequestDto requestDto);

    void logout(String usernameOrEmail);

    void forgotPassword(ForgotPasswordRequestDto requestDto);

    void resetPassword(ResetPasswordRequestDto requestDto);

    void changePassword(String usernameOrEmail, ChangePasswordRequestDto requestDto);

    void verifyEmail(VerifyEmailRequestDto requestDto);

    void verifyOtp(VerifyOtpRequestDto requestDto);
}
