package com.ntdev.library.service;

import org.springframework.stereotype.Service;

import com.ntdev.library.dto.request.LoginRequest;
import com.ntdev.library.dto.request.RegisterRequest;
import com.ntdev.library.dto.response.UserResponse;
import com.ntdev.library.entity.User;
import com.ntdev.library.enums.StatusCode;
import com.ntdev.library.exception.CustomException;
import com.ntdev.library.repository.jpa.UserRepository;
import com.ntdev.library.util.JwtUtil;
import com.ntdev.library.util.PasswordUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RedisService redisService;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    public AuthService(
            JwtUtil jwtUtil,
            PasswordUtil passwordUtil,
            RedisService redisService,
            UserService userService,
            UserRepository userRepository) {
        this.userService = userService;
        this.passwordUtil = passwordUtil;
        this.redisService = redisService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public UserResponse register(RegisterRequest registerRequest, HttpServletResponse response) {

        String saltBase64 = passwordUtil.generateSaltBase64();
        String hashPassword = passwordUtil.hashPassword(registerRequest.getPassword(),
                passwordUtil.decodeSaltBase64(saltBase64));

        String password = saltBase64 + ":" + hashPassword;

        User user = userService.create(registerRequest, password);

        String token = jwtUtil.generateToken(user.getId(), user.getUniversityId(), user.getRole().name());
        redisService.save(user.getUniversityId(), token);
        jwtUtil.setCookie(token, response);

        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .universityId(user.getUniversityId())
                .email(user.getEmail())
                .profileImage(user.getProfileImage())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .profileImage(user.getProfileImage())
                .build();
    }

    public UserResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        User user = userRepository.findByUniversityId(loginRequest.getUniversityId())
                .orElseThrow(() -> new CustomException(StatusCode.USER_NOT_FOUND));

        if (!passwordUtil.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomException(StatusCode.INVALID_PASSWORD);
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUniversityId(), user.getRole().name());
        redisService.save(user.getUniversityId(), token);
        jwtUtil.setCookie(token, response);

        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .universityId(user.getUniversityId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtUtil.getTokenFromCookie(request);
        redisService.deleteByToken(token);
        jwtUtil.deleteCookie(response);
    }
}
