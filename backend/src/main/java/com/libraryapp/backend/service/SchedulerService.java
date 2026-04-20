package com.libraryapp.backend.service;

import com.libraryapp.backend.model.BorrowStatus;
import com.libraryapp.backend.repository.BorrowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@EnableScheduling
public class SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);
    private final BorrowRepository borrowRepository;

    public SchedulerService(BorrowRepository borrowRepository) {
        this.borrowRepository = borrowRepository;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void checkOverdueBorrows() {
        LocalDate today = LocalDate.now();
        var overdue = borrowRepository.findAll().stream()
                .filter(b -> b.getStatus() == BorrowStatus.BORROWED
                        && b.getDueDate() != null
                        && b.getDueDate().isBefore(today))
                .toList();

        if (overdue.isEmpty()) {
            log.info("Geciken ödünç yok.");
            return;
        }

        log.warn("Geciken ödünç sayısı: {}", overdue.size());
        overdue.forEach(b -> log.warn(
            "GECİKMİŞ → Kullanıcı: {}, Kitap: {}, Son teslim: {}, Gecikme: {} gün",
            b.getUser().getUsername(),
            b.getBook().getTitle(),
            b.getDueDate(),
            java.time.temporal.ChronoUnit.DAYS.between(b.getDueDate(), today)
        ));
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void checkDueSoonBorrows() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate in3Days = LocalDate.now().plusDays(3);

        var dueSoon = borrowRepository.findAll().stream()
                .filter(b -> b.getStatus() == BorrowStatus.BORROWED
                        && b.getDueDate() != null
                        && !b.getDueDate().isBefore(tomorrow)
                        && !b.getDueDate().isAfter(in3Days))
                .toList();

        if (!dueSoon.isEmpty()) {
            log.info("Son 3 günde teslim edilecek ödünç sayısı: {}", dueSoon.size());
            dueSoon.forEach(b -> log.info(
                "SON GÜN YAKLAŞIYOR → Kullanıcı: {}, Kitap: {}, Son teslim: {}",
                b.getUser().getUsername(),
                b.getBook().getTitle(),
                b.getDueDate()
            ));
        }
    }
}