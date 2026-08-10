package com.gogreen.ai.service.impl;

import com.gogreen.ai.dto.request.ChangePasswordRequestDto;
import com.gogreen.ai.dto.request.ForgotPasswordRequestDto;
import com.gogreen.ai.dto.request.LoginRequestDto;
import com.gogreen.ai.dto.request.RefreshTokenRequestDto;
import com.gogreen.ai.dto.request.RegisterRequestDto;
import com.gogreen.ai.dto.request.ResetPasswordRequestDto;
import com.gogreen.ai.dto.request.VerifyEmailRequestDto;
import com.gogreen.ai.dto.request.VerifyOtpRequestDto;
import com.gogreen.ai.dto.response.AuthResponseDto;
import com.gogreen.ai.entity.Role;
import com.gogreen.ai.entity.User;
import com.gogreen.ai.entity.enums.UserRole;
import com.gogreen.ai.exception.APIException;
import com.gogreen.ai.mapper.UserMapper;
import com.gogreen.ai.repository.RoleRepository;
import com.gogreen.ai.repository.UserRepository;
import com.gogreen.ai.security.JwtTokenProvider;
import com.gogreen.ai.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public AuthResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsernameOrEmail(),
                loginRequestDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsernameOrEmail(loginRequestDto.getUsernameOrEmail(), loginRequestDto.getUsernameOrEmail())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found"));

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        return new AuthResponseDto(accessToken, refreshToken, "Bearer", jwtTokenProvider.getAccessTokenExpirationInSeconds(), userMapper.toResponseDto(user));
    }

    @Override
    @Transactional
    public AuthResponseDto registerCustomer(RegisterRequestDto requestDto) {
        return registerUser(requestDto, UserRole.ROLE_CUSTOMER);
    }

    @Override
    @Transactional
    public AuthResponseDto registerNurseryOwner(RegisterRequestDto requestDto) {
        return registerUser(requestDto, UserRole.ROLE_NURSERY_OWNER);
    }

    @Override
    @Transactional
    public AuthResponseDto registerGardeningExpert(RegisterRequestDto requestDto) {
        return registerUser(requestDto, UserRole.ROLE_GARDENING_EXPERT);
    }

    @Override
    @Transactional
    public AuthResponseDto registerDeliveryPartner(RegisterRequestDto requestDto) {
        return registerUser(requestDto, UserRole.ROLE_DELIVERY_PARTNER);
    }

    @Override
    @Transactional
    public AuthResponseDto refreshToken(RefreshTokenRequestDto requestDto) {
        if (!jwtTokenProvider.validateToken(requestDto.getRefreshToken())) {
            throw new APIException(HttpStatus.UNAUTHORIZED, "Refresh token is invalid or expired");
        }

        String usernameOrEmail = jwtTokenProvider.getUsername(requestDto.getRefreshToken());
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, java.util.List.of());
        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        return new AuthResponseDto(newAccessToken, newRefreshToken, "Bearer", jwtTokenProvider.getAccessTokenExpirationInSeconds(), userMapper.toResponseDto(user));
    }

    @Override
    @Transactional
    public void logout(String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found"));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found with the provided email"));

        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequestDto requestDto) {
        if (!requestDto.getNewPassword().equals(requestDto.getConfirmPassword())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Passwords do not match");
        }

        User user = userRepository.findByEmail(requestDto.getToken())
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Invalid reset token"));
        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(String usernameOrEmail, ChangePasswordRequestDto requestDto) {
        if (!requestDto.getNewPassword().equals(requestDto.getConfirmPassword())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Passwords do not match");
        }

        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void verifyEmail(VerifyEmailRequestDto requestDto) {
        // Structure-only email verification flow.
    }

    @Override
    public void verifyOtp(VerifyOtpRequestDto requestDto) {
        // Structure-only OTP verification flow.
    }

    private AuthResponseDto registerUser(RegisterRequestDto requestDto, UserRole targetRole) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Username is already in use");
        }

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Email is already in use");
        }

        Role role = roleRepository.findByName(targetRole)
                .orElseGet(() -> roleRepository.save(new Role(targetRole)));

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setName(requestDto.getName());
        user.setUsername(requestDto.getUsername());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setPhoneNumber(requestDto.getPhoneNumber());
        user.setRoles(roles);
        user.setEnabled(true);
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, java.util.List.of());
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        return new AuthResponseDto(accessToken, refreshToken, "Bearer", jwtTokenProvider.getAccessTokenExpirationInSeconds(), userMapper.toResponseDto(user));
    }
}
