package com.ntdev.library.service;

import com.ntdev.library.dto.response.BorrowResponse;
import com.ntdev.library.entity.Book;
import com.ntdev.library.entity.Borrow;
import com.ntdev.library.entity.User;
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
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BorrowService(
            BorrowRepository borrowRepository,
            BookRepository bookRepository,
            UserRepository userRepository
    ) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public List<BorrowResponse> getAll() {
        return borrowRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<BorrowResponse> getByUserId(Long userId) {
        return borrowRepository.findByUserId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BorrowResponse borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(StatusCode.USER_NOT_FOUND));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException(StatusCode.BOOK_NOT_FOUND));

        if (book.getAvailableCopies() <= 0) {
            throw new CustomException(StatusCode.BOOK_NOT_AVAILABLE);
        }

        // Kiểm tra xem user đã mượn sách này và chưa trả chưa
        List<Borrow> userBorrows = borrowRepository.findByUserId(userId);
        boolean alreadyBorrowed = userBorrows.stream()
                .anyMatch(borrow -> borrow.getBook().getId().equals(bookId) && borrow.getReturnDate() == null);
        if (alreadyBorrowed) {
            throw new CustomException(StatusCode.BOOK_ALREADY_BORROWED);
        }

        Borrow borrow = Borrow.builder()
                .user(user)
                .book(book)
                .build();

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Borrow savedBorrow = borrowRepository.save(borrow);
        return convertToResponse(savedBorrow);
    }

    @Transactional
    public BorrowResponse returnBook(Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new CustomException(StatusCode.BORROW_NOT_FOUND));

        if (borrow.getReturnDate() != null) {
            throw new CustomException(StatusCode.BORROW_ALREADY_RETURNED);
        }

        borrow.setReturnDate(LocalDateTime.now());
        Book book = borrow.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        bookRepository.save(book);
        borrowRepository.save(borrow);

        return convertToResponse(borrow);
    }

    private BorrowResponse convertToResponse(Borrow borrow) {
        return BorrowResponse.builder()
                .id(borrow.getId())
                .userId(borrow.getUser().getId())
                .bookId(borrow.getBook().getId())
                .bookTitle(borrow.getBook().getTitle())
                .borrowDate(borrow.getBorrowDate())
                .dueDate(borrow.getDueDate())
                .returnDate(borrow.getReturnDate())
                .build();
    }
}