package com.libraryapp.backend.controller;

import com.libraryapp.backend.dto.response.BookResponse;
import com.libraryapp.backend.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@Tag(name = "Recommendations", description = "Kitap öneri sistemi")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping
    @Operation(summary = "Kişisel öneriler (giriş yapılmışsa) veya popüler kitaplar")
    public ResponseEntity<List<BookResponse>> getRecommendations(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            return ResponseEntity.ok(recommendationService.getRecommendations(userDetails.getUsername()));
        }
        return ResponseEntity.ok(recommendationService.getPopularBooks());
    }

    @GetMapping("/popular")
    @Operation(summary = "En popüler kitaplar")
    public ResponseEntity<List<BookResponse>> getPopular() {
        return ResponseEntity.ok(recommendationService.getPopularBooks());
    }
}