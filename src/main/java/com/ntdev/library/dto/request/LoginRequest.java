package com.ntdev.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotNull(message = "University ID must not be null")
    private Long universityId;

    @NotBlank(message = "Password must not be blank")
    private String password;
}
