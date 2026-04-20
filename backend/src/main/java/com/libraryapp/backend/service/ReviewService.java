package com.libraryapp.backend.service;

import com.libraryapp.backend.dto.request.ReviewRequest;
import com.libraryapp.backend.dto.response.ReviewResponse;
import com.libraryapp.backend.exception.ResourceNotFoundException;
import com.libraryapp.backend.model.Review;
import com.libraryapp.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         BookRepository bookRepository,
                         UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public ReviewResponse addReview(Long bookId, String username, ReviewRequest request) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (reviewRepository.existsByUserIdAndBookId(user.getId(), bookId))
            throw new IllegalArgumentException("Bu kitabı zaten yorumladın");

        if (request.getRating() < 1 || request.getRating() > 5)
            throw new IllegalArgumentException("Puan 1-5 arasında olmalı");

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());

        return toResponse(reviewRepository.save(review));
    }

    public List<ReviewResponse> getBookReviews(Long bookId) {
        return reviewRepository.findByBookId(bookId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Double getAverageRating(Long bookId) {
        Double avg = reviewRepository.findAverageRatingByBookId(bookId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    public void deleteReview(Long reviewId, String username) {
        var review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        if (!review.getUser().getUsername().equals(username))
            throw new IllegalArgumentException("Bu yorum sana ait değil");
        reviewRepository.deleteById(reviewId);
    }

    private ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .username(review.getUser().getUsername())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}