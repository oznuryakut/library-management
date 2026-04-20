package com.libraryapp.backend.repository;

import com.libraryapp.backend.model.Borrow;
import com.libraryapp.backend.model.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    List<Borrow> findByUserId(Long userId);
    List<Borrow> findByUserIdAndStatus(Long userId, BorrowStatus status);
    boolean existsByUserIdAndBookIdAndStatus(Long userId, Long bookId, BorrowStatus status);
}