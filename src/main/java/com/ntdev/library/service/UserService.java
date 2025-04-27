package com.ntdev.library.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ntdev.library.dto.request.UserCreateRequest;
import com.ntdev.library.dto.request.UserUpdateRequest;
import com.ntdev.library.dto.response.UserResponse;
import com.ntdev.library.entity.User;
import com.ntdev.library.enums.RoleName;
import com.ntdev.library.enums.StatusCode;
import com.ntdev.library.exception.CustomException;
import com.ntdev.library.repository.jpa.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAll() {
        List<UserResponse> users = userRepository.findAll().stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());

        return users;
    }

    public UserResponse get(Long universityId) {
        User user = userRepository.findByUniversityId(universityId)
                .orElseThrow(() -> new CustomException(StatusCode.USER_NOT_FOUND));

        return convertToUserResponse(user);
    }

    @Transactional
    public User create(UserCreateRequest request, String password) {
        if (userRepository.findByUniversityIdOrEmail(request.getUniversityId(), request.getEmail()).isPresent()) {
            throw new CustomException(StatusCode.UNIVERSITYID_OR_EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .universityId(request.getUniversityId())
                .email(request.getEmail())
                .password(password)
                .role(RoleName.MEMBER) // giả sử default role
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.saveAndFlush(user); // 👈 saveAndFlush
        return savedUser;
    }

    @Transactional
    public UserResponse update(Long universityId, UserUpdateRequest request) {

        User user = userRepository.findByUniversityId(universityId)
                .orElseThrow(() -> new CustomException(StatusCode.USER_NOT_FOUND));

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        userRepository.saveAndFlush(user);

        return convertToUserResponse(user);
    }

    @Transactional
    public void delete(Long universityId) {
        User user = userRepository.findByUniversityId(universityId)
                .orElseThrow(() -> new CustomException(StatusCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }

    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .universityId(user.getUniversityId())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
