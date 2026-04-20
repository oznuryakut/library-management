package com.libraryapp.backend.dto.response;

import java.time.LocalDateTime;

public class ReviewResponse {
    private Long id;
    private String username;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    public ReviewResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final ReviewResponse r = new ReviewResponse();
        public Builder id(Long id) { r.id = id; return this; }
        public Builder username(String username) { r.username = username; return this; }
        public Builder rating(int rating) { r.rating = rating; return this; }
        public Builder comment(String comment) { r.comment = comment; return this; }
        public Builder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
        public ReviewResponse build() { return r; }
    }
}