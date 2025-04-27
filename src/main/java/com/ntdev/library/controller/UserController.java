package com.ntdev.library.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntdev.library.dto.request.UserUpdateRequest;
import com.ntdev.library.dto.response.ApiResponse;
import com.ntdev.library.dto.response.UserResponse;
import com.ntdev.library.enums.StatusCode;
import com.ntdev.library.exception.CustomException;
import com.ntdev.library.service.AuthService;
import com.ntdev.library.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        try {
            ApiResponse<List<UserResponse>> users = ApiResponse.<List<UserResponse>>builder()
                    .success(true)
                    .status(StatusCode.SUCCESS.getCode())
                    .message("Users retrieved successfully")
                    .data(userService.getAll())
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (CustomException e) {
            throw new CustomException(e.getStatusCode());
        }
    }

    @GetMapping("/{universityId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long universityId) {
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

    @PatchMapping("/update/{universityId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long universityId,
            @Valid @RequestBody UserUpdateRequest updates,
            HttpServletRequest request) {
        try {
            UserResponse resuilt = userService.update(universityId, updates);
            ApiResponse<UserResponse> updatedUser = ApiResponse.<UserResponse>builder()
                    .success(true)
                    .status(StatusCode.SUCCESS.getCode())
                    .message("User updated successfully")
                    .data(resuilt)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } catch (CustomException e) {
            throw new CustomException(e.getStatusCode());
        }
    }

    @DeleteMapping("/delete/{universityId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long universityId,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            userService.delete(universityId);
            authService.logout(request, response);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                    .success(true)
                    .status(StatusCode.SUCCESS.getCode())
                    .message("User deleted successfully")
                    .build());
        } catch (CustomException e) {
            throw new CustomException(e.getStatusCode());
        }
    }
}
