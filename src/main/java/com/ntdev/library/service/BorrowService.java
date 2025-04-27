package com.ntdev.library.service;

import com.ntdev.library.dto.response.BorrowResponse;
import com.ntdev.library.entity.Book;
import com.ntdev.library.entity.Borrow;
import com.ntdev.library.entity.User;
import com.ntdev.library.enums.BorrowingStatus;
import com.ntdev.library.enums.StatusCode;
import com.ntdev.library.exception.CustomException;
import com.ntdev.library.repository.jpa.BookRepository;
import com.ntdev.library.repository.jpa.BorrowRepository;
import com.ntdev.library.repository.jpa.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public BorrowService(BorrowRepository borrowRepository, UserRepository userRepository,
            BookRepository bookRepository) {
        this.borrowRepository = borrowRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public List<BorrowResponse> getAll() {
        return borrowRepository.findAll()
                .stream()
                .map(this::convertToBorrowResponse)
                .collect(Collectors.toList());
    }

    public BorrowResponse get(Long id) {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new CustomException(StatusCode.BORROW_NOT_FOUND));
        return convertToBorrowResponse(borrow);
    }

    @Transactional
    public BorrowResponse borrowBook(Long userId, Long bookId, int borrowDays) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(StatusCode.USER_NOT_FOUND));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException(StatusCode.BOOK_NOT_FOUND));

        if (book.getAvailableCopies() <= 0) {
            throw new CustomException(StatusCode.BOOK_NOT_AVAILABLE);
        }

        // Giảm số lượng available
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Borrow borrow = Borrow.builder()
                .user(user)
                .book(book)
                .borrowDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(borrowDays))
                .status(BorrowingStatus.BORROWED)
                .build();

        Borrow savedBorrow = borrowRepository.save(borrow);
        return convertToBorrowResponse(savedBorrow);
    }

    @Transactional
    public BorrowResponse returnBook(Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new CustomException(StatusCode.BORROW_NOT_FOUND));

        if (borrow.getStatus() != BorrowingStatus.BORROWED) {
            throw new CustomException(StatusCode.BORROW_ALREADY_RETURNED);
        }

        borrow.setStatus(BorrowingStatus.RETURNED);
        borrow.setReturnDate(LocalDateTime.now());
        borrowRepository.save(borrow);

        Book book = borrow.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return convertToBorrowResponse(borrow);
    }

    private BorrowResponse convertToBorrowResponse(Borrow borrow) {
        return BorrowResponse.builder()
                .id(borrow.getId())
                .userId(borrow.getUser().getId())
                .bookId(borrow.getBook().getId())
                .borrowDate(borrow.getBorrowDate())
                .dueDate(borrow.getDueDate())
                .returnDate(borrow.getReturnDate())
                .status(borrow.getStatus())
                .build();
    }
}
