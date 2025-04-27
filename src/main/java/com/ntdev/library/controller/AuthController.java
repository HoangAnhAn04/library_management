package com.ntdev.library.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntdev.library.dto.request.LoginRequest;
import com.ntdev.library.dto.request.RegisterRequest;
import com.ntdev.library.dto.response.ApiResponse;
import com.ntdev.library.dto.response.UserResponse;
import com.ntdev.library.exception.CustomException;
import com.ntdev.library.service.AuthService;
import com.ntdev.library.service.UserService;
import com.ntdev.library.enums.StatusCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @RequestBody @Valid RegisterRequest request,
            HttpServletResponse response) {
        try {
            UserResponse registerResponse = authService.register(request, response);
            ApiResponse<UserResponse> result = ApiResponse.<UserResponse>builder()
                    .success(true)
                    .status(StatusCode.SUCCESS.getCode())
                    .message("User registered successfully")
                    .data(registerResponse)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (CustomException e) {
            throw new CustomException(e.getStatusCode());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse response) {
        try {
            UserResponse loginResponse = authService.login(request, response);
            ApiResponse<UserResponse> result = ApiResponse.<UserResponse>builder()
                    .success(true)
                    .status(StatusCode.SUCCESS.getCode())
                    .message("User logged in successfully")
                    .data(loginResponse)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (CustomException e) {
            throw new CustomException(e.getStatusCode());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            authService.logout(request, response);
            ApiResponse<Void> result = ApiResponse.<Void>builder()
                    .success(true)
                    .status(StatusCode.SUCCESS.getCode())
                    .message("User logged out successfully")
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (CustomException e) {
            throw new CustomException(e.getStatusCode());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(HttpServletRequest request) {
        Long universityId = Long.valueOf(request.getAttribute("universityId").toString());
        try {
            UserResponse resuilt = userService.get(universityId);
            ApiResponse<UserResponse> result = ApiResponse.<UserResponse>builder()
                    .success(true)
                    .status(StatusCode.SUCCESS.getCode())
                    .message("User retrieved successfully")
                    .data(resuilt)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (CustomException e) {
            throw new CustomException(e.getStatusCode());
        }
    }
}
