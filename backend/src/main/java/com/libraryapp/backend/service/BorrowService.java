package com.libraryapp.backend.service;

import com.libraryapp.backend.dto.response.BorrowResponse;
import com.libraryapp.backend.exception.ResourceNotFoundException;
import com.libraryapp.backend.model.*;
import com.libraryapp.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowService {

    private static final int MAX_BORROW_LIMIT = 3;
    private static final int BORROW_DAYS = 14;

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BorrowService(BorrowRepository borrowRepository,
                         BookRepository bookRepository,
                         UserRepository userRepository) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public BorrowResponse borrowBook(Long bookId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (book.getStock() <= 0)
            throw new IllegalArgumentException("Kitapta stok yok");

        long activeCount = borrowRepository.findByUserId(user.getId())
                .stream().filter(b -> b.getStatus() == BorrowStatus.BORROWED).count();

        if (activeCount >= MAX_BORROW_LIMIT)
            throw new IllegalArgumentException("Maksimum " + MAX_BORROW_LIMIT + " kitap ödünç alabilirsin");

        if (borrowRepository.existsByUserIdAndBookIdAndStatus(user.getId(), bookId, BorrowStatus.BORROWED))
            throw new IllegalArgumentException("Bu kitabı zaten ödünç almışsın");

        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        Borrow borrow = new Borrow();
        borrow.setUser(user);
        borrow.setBook(book);
        borrow.setBorrowDate(LocalDate.now());
        borrow.setDueDate(LocalDate.now().plusDays(BORROW_DAYS));
        borrow.setStatus(BorrowStatus.BORROWED);

        return toResponse(borrowRepository.save(borrow));
    }

    public BorrowResponse returnBook(Long borrowId, String username) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow not found"));

        if (!borrow.getUser().getUsername().equals(username))
            throw new IllegalArgumentException("Bu ödünç sana ait değil");

        if (borrow.getStatus() == BorrowStatus.RETURNED)
            throw new IllegalArgumentException("Bu kitap zaten iade edilmiş");

        borrow.setStatus(BorrowStatus.RETURNED);
        borrow.setReturnDate(LocalDate.now());

        Book book = borrow.getBook();
        book.setStock(book.getStock() + 1);
        bookRepository.save(book);

        return toResponse(borrowRepository.save(borrow));
    }

    public List<BorrowResponse> getUserBorrows(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return borrowRepository.findByUserId(user.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<BorrowResponse> getAllBorrows() {
        return borrowRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<BorrowResponse> getOverdueBorrows() {
        return borrowRepository.findAll().stream()
                .filter(b -> b.getStatus() == BorrowStatus.BORROWED
                        && b.getDueDate() != null
                        && b.getDueDate().isBefore(LocalDate.now()))
                .map(this::toResponse).collect(Collectors.toList());
    }

    private BorrowResponse toResponse(Borrow borrow) {
        LocalDate today = LocalDate.now();
        boolean overdue = borrow.getStatus() == BorrowStatus.BORROWED
                && borrow.getDueDate() != null
                && borrow.getDueDate().isBefore(today);
        long penaltyDays = overdue ? ChronoUnit.DAYS.between(borrow.getDueDate(), today) : 0;

        return BorrowResponse.builder()
                .id(borrow.getId())
                .bookTitle(borrow.getBook().getTitle())
                .bookAuthor(borrow.getBook().getAuthor())
                .bookId(borrow.getBook().getId())
                .username(borrow.getUser().getUsername())
                .borrowDate(borrow.getBorrowDate())
                .dueDate(borrow.getDueDate())
                .returnDate(borrow.getReturnDate())
                .status(borrow.getStatus())
                .overdue(overdue)
                .penaltyDays(penaltyDays)
                .build();
    }
}