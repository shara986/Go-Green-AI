package com.gogreen.ai.service.impl;

import com.gogreen.ai.dto.request.UpdateProfileRequestDto;
import com.gogreen.ai.dto.response.UserResponseDto;
import com.gogreen.ai.entity.User;
import com.gogreen.ai.exception.APIException;
import com.gogreen.ai.mapper.UserMapper;
import com.gogreen.ai.repository.UserRepository;
import com.gogreen.ai.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getCurrentUserProfile(String usernameOrEmail) {
        User user = findUser(usernameOrEmail);
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateCurrentUserProfile(String usernameOrEmail, UpdateProfileRequestDto requestDto) {
        User user = findUser(usernameOrEmail);

        if (requestDto.getName() != null) {
            user.setName(requestDto.getName());
        }
        if (requestDto.getEmail() != null) {
            if (userRepository.existsByEmail(requestDto.getEmail()) && !requestDto.getEmail().equals(user.getEmail())) {
                throw new APIException(HttpStatus.BAD_REQUEST, "Email is already in use");
            }
            user.setEmail(requestDto.getEmail());
        }
        if (requestDto.getPhoneNumber() != null) {
            user.setPhoneNumber(requestDto.getPhoneNumber());
        }

        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    @Override
    @Transactional
    public void changePassword(String usernameOrEmail, String currentPassword, String newPassword) {
        User user = findUser(usernameOrEmail);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private User findUser(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
