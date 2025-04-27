package com.ntdev.library.dto.response;

import java.time.LocalDateTime;

import com.ntdev.library.enums.RoleName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private Long universityId;
    private String email;
    private String profileImage;
    private RoleName role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
