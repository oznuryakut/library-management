package com.libraryapp.backend.controller;

import com.libraryapp.backend.dto.response.BorrowResponse;
import com.libraryapp.backend.service.BorrowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
@Tag(name = "Borrows", description = "Ödünç alma işlemleri")
@SecurityRequirement(name = "bearerAuth")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/{bookId}")
    @Operation(summary = "Kitap ödünç al")
    public ResponseEntity<BorrowResponse> borrowBook(
            @PathVariable Long bookId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(borrowService.borrowBook(bookId, userDetails.getUsername()));
    }

    @PutMapping("/{borrowId}/return")
    @Operation(summary = "Kitap iade et")
    public ResponseEntity<BorrowResponse> returnBook(
            @PathVariable Long borrowId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(borrowService.returnBook(borrowId, userDetails.getUsername()));
    }

    @GetMapping("/my")
    @Operation(summary = "Benim ödünçlerim")
    public ResponseEntity<List<BorrowResponse>> myBorrows(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(borrowService.getUserBorrows(userDetails.getUsername()));
    }

    @GetMapping("/admin/all")
    @Operation(summary = "Tüm ödünçler (ADMIN)")
    public ResponseEntity<List<BorrowResponse>> allBorrows() {
        return ResponseEntity.ok(borrowService.getAllBorrows());
    }

    @GetMapping("/admin/overdue")
    @Operation(summary = "Geciken ödünçler (ADMIN)")
    public ResponseEntity<List<BorrowResponse>> overdueBorrows() {
        return ResponseEntity.ok(borrowService.getOverdueBorrows());
    }
}