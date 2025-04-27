package com.ntdev.library.exception;

import com.ntdev.library.enums.StatusCode;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final StatusCode statusCode;

    public CustomException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
    }
}
