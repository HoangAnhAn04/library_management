package com.ntdev.library.repository.jpa;

import com.ntdev.library.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    List<Borrow> findByUserId(Long userId);
    List<Borrow> findByBookId(Long bookId);
}