package com.libraryapp.backend.controller;

import com.libraryapp.backend.dto.request.ReviewRequest;
import com.libraryapp.backend.dto.response.ReviewResponse;
import com.libraryapp.backend.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books/{bookId}/reviews")
@Tag(name = "Reviews", description = "Kitap yorum ve puanlama")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    @Operation(summary = "Kitap yorumları")
    public ResponseEntity<List<ReviewResponse>> getReviews(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getBookReviews(bookId));
    }

    @GetMapping("/rating")
    @Operation(summary = "Ortalama puan")
    public ResponseEntity<Map<String, Double>> getRating(@PathVariable Long bookId) {
        return ResponseEntity.ok(Map.of("rating", reviewService.getAverageRating(bookId)));
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Yorum ekle")
    public ResponseEntity<ReviewResponse> addReview(
            @PathVariable Long bookId,
            @RequestBody ReviewRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(reviewService.addReview(bookId, userDetails.getUsername(), request));
    }

    @DeleteMapping("/{reviewId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Yorum sil")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long bookId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetails userDetails) {
        reviewService.deleteReview(reviewId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}