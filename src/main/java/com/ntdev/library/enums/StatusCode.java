package com.ntdev.library.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StatusCode {
    // Default status code
    SUCCESS(200, "Success", HttpStatus.OK),
    CREATED(201, "Created", HttpStatus.CREATED),
    BAD_REQUEST(400, "Bad Request", HttpStatus.BAD_REQUEST),
    NOT_FOUND(404, "Not Found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(403, "Forbidden", HttpStatus.FORBIDDEN),
    INTERNAL_SERVER_ERROR(500, "Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_ERROR(422, "Validation Error", HttpStatus.UNPROCESSABLE_ENTITY),

    // Custom status codes
    TOKEN_INVALID_OR_EXPIRED(1001, "Token is invalid or expired", HttpStatus.UNAUTHORIZED),
    TOKEN_NOT_FOUND(1002, "Token not found", HttpStatus.UNAUTHORIZED),
    UNIVERSITYID_OR_EMAIL_ALREADY_EXISTS(1003, "University id or email already exists", HttpStatus.BAD_REQUEST),
    PASSWORD_DOES_NOT_MATCH(1004, "Password does not match", HttpStatus.BAD_REQUEST),
    LOGGED_IN(1005, "User is already logged in", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(1006, "User not found", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD(1007, "Invalid password", HttpStatus.UNAUTHORIZED),
    BOOK_NOT_FOUND(1008, "Book not found", HttpStatus.NOT_FOUND),
    BOOK_ALREADY_EXISTS(1009, "Book already exists", HttpStatus.BAD_REQUEST),
    UNIVERSITYID_NOT_FOUND(1010, "University not found", HttpStatus.NOT_FOUND),
    BOOK_OUT_OF_STOCK(1011, "Book out of stock", HttpStatus.BAD_REQUEST),
    BOOK_ALREADY_BORROWED(1012, "Book already borrowed", HttpStatus.BAD_REQUEST),
    BOOK_NOT_BORROWED(1013, "Book not borrowed", HttpStatus.BAD_REQUEST),
    BORROW_NOT_FOUND(1014, "Borrow not found", HttpStatus.NOT_FOUND),
    BOOK_NOT_AVAILABLE(1015, "Book not available", HttpStatus.BAD_REQUEST),
    BORROW_ALREADY_RETURNED(1016, "Borrow already returned", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    StatusCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
