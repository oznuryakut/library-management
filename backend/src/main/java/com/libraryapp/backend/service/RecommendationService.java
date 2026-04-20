package com.libraryapp.backend.service;

import com.libraryapp.backend.dto.response.BookResponse;
import com.libraryapp.backend.model.Book;
import com.libraryapp.backend.model.BorrowStatus;
import com.libraryapp.backend.repository.BookRepository;
import com.libraryapp.backend.repository.BorrowRepository;
import com.libraryapp.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public RecommendationService(BorrowRepository borrowRepository,
                                  BookRepository bookRepository,
                                  UserRepository userRepository) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public List<BookResponse> getRecommendations(String username) {
        var user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return getPopularBooks();

        var userBorrows = borrowRepository.findByUserId(user.getId());
        var borrowedBookIds = userBorrows.stream()
                .map(b -> b.getBook().getId())
                .collect(Collectors.toSet());

        if (borrowedBookIds.isEmpty()) return getPopularBooks();

        // Kullanıcının en çok ödünç aldığı kategoriyi bul
        var categoryCount = userBorrows.stream()
                .filter(b -> b.getBook().getCategory() != null)
                .collect(Collectors.groupingBy(
                        b -> b.getBook().getCategory(), Collectors.counting()));

        if (categoryCount.isEmpty()) return getPopularBooks();

        String favoriteCategory = categoryCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Aynı kategorideki ödünç alınmamış kitapları öner
        return bookRepository.findAll().stream()
                .filter(b -> favoriteCategory.equals(b.getCategory()))
                .filter(b -> !borrowedBookIds.contains(b.getId()))
                .filter(b -> b.getStock() > 0)
                .limit(6)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BookResponse> getPopularBooks() {
        var borrowCounts = borrowRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        b -> b.getBook().getId(), Collectors.counting()));

        return bookRepository.findAll().stream()
                .filter(b -> b.getStock() > 0)
                .sorted((a, b) -> Long.compare(
                        borrowCounts.getOrDefault(b.getId(), 0L),
                        borrowCounts.getOrDefault(a.getId(), 0L)))
                .limit(6)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .category(book.getCategory())
                .price(book.getPrice())
                .stock(book.getStock())
                .build();
    }
}