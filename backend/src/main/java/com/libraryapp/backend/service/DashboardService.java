package com.libraryapp.backend.service;

import com.libraryapp.backend.dto.response.DashboardResponse;
import com.libraryapp.backend.model.BorrowStatus;
import com.libraryapp.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowRepository borrowRepository;

    public DashboardService(BookRepository bookRepository,
                            UserRepository userRepository,
                            BorrowRepository borrowRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowRepository = borrowRepository;
    }

    public DashboardResponse getDashboard() {
        var allBorrows = borrowRepository.findAll();
        LocalDate today = LocalDate.now();

        long activeBorrows = allBorrows.stream()
                .filter(b -> b.getStatus() == BorrowStatus.BORROWED).count();

        long overdueBorrows = allBorrows.stream()
                .filter(b -> b.getStatus() == BorrowStatus.BORROWED
                        && b.getDueDate() != null
                        && b.getDueDate().isBefore(today)).count();

        // En çok ödünç alınan kitaplar
        Map<String, Long> bookCounts = allBorrows.stream()
                .collect(Collectors.groupingBy(
                        b -> b.getBook().getTitle(), Collectors.counting()));

        List<Map<String, Object>> topBooks = bookCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(e -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("title", e.getKey());
                    m.put("count", e.getValue());
                    return m;
                }).collect(Collectors.toList());

        // Kategori istatistikleri
        Map<String, Long> catCounts = bookRepository.findAll().stream()
                .filter(b -> b.getCategory() != null)
                .collect(Collectors.groupingBy(b -> b.getCategory(), Collectors.counting()));

        List<Map<String, Object>> categoryStats = catCounts.entrySet().stream()
                .map(e -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("category", e.getKey());
                    m.put("count", e.getValue());
                    return m;
                }).collect(Collectors.toList());

        // Son 6 ay ödünç istatistikleri
        List<Map<String, Object>> monthlyBorrows = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            LocalDate month = today.minusMonths(i);
            long count = allBorrows.stream()
                    .filter(b -> b.getBorrowDate() != null
                            && b.getBorrowDate().getYear() == month.getYear()
                            && b.getBorrowDate().getMonth() == month.getMonth())
                    .count();
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("month", month.getMonth().getDisplayName(TextStyle.SHORT, new Locale("tr")));
            m.put("count", count);
            monthlyBorrows.add(m);
        }

        return DashboardResponse.builder()
                .totalBooks(bookRepository.count())
                .totalUsers(userRepository.count())
                .activeBorrows(activeBorrows)
                .overdueBorrows(overdueBorrows)
                .totalBorrows(allBorrows.size())
                .topBooks(topBooks)
                .categoryStats(categoryStats)
                .monthlyBorrows(monthlyBorrows)
                .build();
    }
}