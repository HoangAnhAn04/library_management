package com.ntdev.library.controller;

import com.ntdev.library.dto.response.ApiResponse;
import com.ntdev.library.dto.response.BorrowResponse;
import com.ntdev.library.enums.StatusCode;
import com.ntdev.library.exception.CustomException;
import com.ntdev.library.service.BorrowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BorrowResponse>>> getAllBorrows() {
        try {
            ApiResponse<List<BorrowResponse>> borrows = ApiResponse.<List<BorrowResponse>>builder()
                    .success(true)
                    .status(StatusCode.SUCCESS.getCode())
                    .message("Borrows retrieved successfully")
                    .data(borrowService.getAll())
                    .build();
            return ResponseEntity.status(StatusCode.SUCCESS.getHttpStatus()).body(borrows);
        } catch (CustomException e) {
            throw new CustomException(e.getStatusCode());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<BorrowResponse>>> getBorrowsByUser(@PathVariable Long userId) {
        try {
            ApiResponse<List<BorrowResponse>> borrows = ApiResponse.<List<BorrowResponse>>builder()
                    .success(true)
                    .status(StatusCode.SUCCESS.getCode())
                    .message("User borrows retrieved successfully")
                    .data(borrowService.getByUserId(userId))
                    .build();
            return ResponseEntity.status(StatusCode.SUCCESS.getHttpStatus()).body(borrows);
        } catch (CustomException e) {
            throw new CustomException(e.getStatusCode());
        }
    }

    @PostMapping("/borrow")
    public ResponseEntity<ApiResponse<BorrowResponse>> borrowBook(
            @RequestParam Long userId,
            @RequestParam Long bookId
    ) {
        try {
            BorrowResponse result = borrowService.borrowBook(userId, bookId);
            ApiResponse<BorrowResponse> response = ApiResponse.<BorrowResponse>builder()
                    .success(true)
                    .status(StatusCode.CREATED.getCode())
                    .message("Book borrowed successfully")
                    .data(result)
                    .build();
            return ResponseEntity.status(StatusCode.CREATED.getHttpStatus()).body(response);
        } catch (CustomException e) {
            throw new CustomException(e.getStatusCode());
        }
    }

    @PatchMapping("/return/{borrowId}")
    public ResponseEntity<ApiResponse<BorrowResponse>> returnBook(@PathVariable Long borrowId) {
        try {
            BorrowResponse result = borrowService.returnBook(borrowId);
            ApiResponse<BorrowResponse> response = ApiResponse.<BorrowResponse>builder()
                    .success(true)
                    .status(StatusCode.SUCCESS.getCode())
                    .message("Book returned successfully")
                    .data(result)
                    .build();
            return ResponseEntity.status(StatusCode.SUCCESS.getHttpStatus()).body(response);
        } catch (CustomException e) {
            throw new CustomException(e.getStatusCode());
        }
    }
}