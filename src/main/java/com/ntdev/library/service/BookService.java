package com.ntdev.library.service;

import com.ntdev.library.dto.request.BookCreateRequest;
import com.ntdev.library.dto.request.BookUpdateRequest;
import com.ntdev.library.dto.response.BookResponse;
import com.ntdev.library.entity.Book;
import com.ntdev.library.enums.StatusCode;
import com.ntdev.library.exception.CustomException;
import com.ntdev.library.repository.jpa.BookRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookResponse> getAll() {
        List<BookResponse> books = bookRepository.findAll().stream()
                .map(this::convertToBookResponse)
                .collect(Collectors.toList());

        return books;
    }

    public BookResponse get(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new CustomException(StatusCode.BOOK_NOT_FOUND));
        return convertToBookResponse(book);
    }

    @Transactional
    public BookResponse create(BookCreateRequest request) {

        Optional<Book> existingBookOpt = bookRepository.findByIsbn(request.getIsbn());

        if (existingBookOpt.isPresent()) {
            Book existingBook = existingBookOpt.get();
            existingBook.setTotalCopies(existingBook.getTotalCopies() + request.getTotalCopies());
            existingBook.setAvailableCopies(existingBook.getAvailableCopies() + request.getTotalCopies());
            bookRepository.save(existingBook);
            return convertToBookResponse(existingBook);
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .genre(request.getGenre())
                .coverUrl(request.getCoverUrl())
                .description(request.getDescription())
                .summary(request.getSummary())
                .isbn(request.getIsbn())
                .totalCopies(request.getTotalCopies())
                .availableCopies(request.getTotalCopies())
                .build();

        Book savedBook = bookRepository.saveAndFlush(book);
        return convertToBookResponse(savedBook);
    }

    @Transactional
    public BookResponse update(Long id, BookUpdateRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new CustomException(StatusCode.BOOK_NOT_FOUND));

        if (request.getTitle() != null)
            book.setTitle(request.getTitle());
        if (request.getAuthor() != null)
            book.setAuthor(request.getAuthor());
        if (request.getGenre() != null)
            book.setGenre(request.getGenre());
        if (request.getCoverUrl() != null)
            book.setCoverUrl(request.getCoverUrl());
        if (request.getDescription() != null)
            book.setDescription(request.getDescription());
        if (request.getSummary() != null)
            book.setSummary(request.getSummary());
        if (request.getIsbn() != null)
            book.setIsbn(request.getIsbn());

        if (request.getTotalCopies() != null) {
            int borrowedCopies = book.getTotalCopies() - book.getAvailableCopies();
            book.setTotalCopies(request.getTotalCopies());
            book.setAvailableCopies(Math.max(0, request.getTotalCopies() - borrowedCopies));
        }

        bookRepository.save(book);
        return convertToBookResponse(book);
    }

    @Transactional
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new CustomException(StatusCode.BOOK_NOT_FOUND));
        bookRepository.delete(book);
    }

    private BookResponse convertToBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .coverUrl(book.getCoverUrl())
                .description(book.getDescription())
                .summary(book.getSummary())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .build();
    }
}
