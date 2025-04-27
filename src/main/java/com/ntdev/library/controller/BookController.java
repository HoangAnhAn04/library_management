package com.ntdev.library.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ntdev.library.dto.request.BookCreateRequest;
import com.ntdev.library.dto.request.BookUpdateRequest;
import com.ntdev.library.dto.response.ApiResponse;
import com.ntdev.library.dto.response.BookResponse;
import com.ntdev.library.enums.StatusCode;
import com.ntdev.library.exception.CustomException;
import com.ntdev.library.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<BookResponse>>> getAllBooks() {
        try {
            ApiResponse<List<BookResponse>> books = ApiResponse.<List<BookResponse>>builder()
                .success(true)
                .status(StatusCode.SUCCESS.getCode())
                .message("Books retrieved successfully")
                .data(bookService.getAll())
                .build();

            return ResponseEntity.status(HttpStatus.OK).body(books);
        } catch (CustomException e) {
            throw new CustomException(e.getStatusCode());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getBook(@PathVariable Long id) {
        try {
            BookResponse result = bookService.get(id);
            ApiResponse<BookResponse> response = ApiResponse.<BookResponse>builder()
                .success(true)
                .status(StatusCode.SUCCESS.getCode())
                .message("Book retrieved successfully")
                .data(result)
                .build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (CustomException e) {
            throw new CustomException(e.getStatusCode());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BookResponse>> createBook(
        @Valid @RequestBody BookCreateRequest request
    ) {
        try {
            BookResponse result = bookService.create(request);
            ApiResponse<BookResponse> response = ApiResponse.<BookResponse>builder()
                .success(true)
                .status(StatusCode.SUCCESS.getCode())
                .message("Book created successfully")
                .data(result)
                .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CustomException e) {
            throw new CustomException(e.getStatusCode());
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(
        @PathVariable Long id,
        @Valid @RequestBody BookUpdateRequest updates
    ) {
        try {
            BookResponse result = bookService.update(id, updates);
            ApiResponse<BookResponse> response = ApiResponse.<BookResponse>builder()
                .success(true)
                .status(StatusCode.SUCCESS.getCode())
                .message("Book updated successfully")
                .data(result)
                .build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (CustomException e) {
            throw new CustomException(e.getStatusCode());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {
        try {
            bookService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .success(true)
                .status(StatusCode.SUCCESS.getCode())
                .message("Book deleted successfully")
                .build());
        } catch (CustomException e) {
            throw new CustomException(e.getStatusCode());
        }
    }
}
