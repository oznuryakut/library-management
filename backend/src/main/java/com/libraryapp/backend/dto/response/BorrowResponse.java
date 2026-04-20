package com.libraryapp.backend.dto.response;

import com.libraryapp.backend.model.BorrowStatus;
import java.time.LocalDate;

public class BorrowResponse {
    private Long id;
    private String bookTitle;
    private String bookAuthor;
    private Long bookId;
    private String username;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BorrowStatus status;
    private boolean overdue;
    private long penaltyDays;

    public BorrowResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public BorrowStatus getStatus() { return status; }
    public void setStatus(BorrowStatus status) { this.status = status; }
    public boolean isOverdue() { return overdue; }
    public void setOverdue(boolean overdue) { this.overdue = overdue; }
    public long getPenaltyDays() { return penaltyDays; }
    public void setPenaltyDays(long penaltyDays) { this.penaltyDays = penaltyDays; }

    public static BorrowResponseBuilder builder() { return new BorrowResponseBuilder(); }

    public static class BorrowResponseBuilder {
        private Long id;
        private String bookTitle;
        private String bookAuthor;
        private Long bookId;
        private String username;
        private LocalDate borrowDate;
        private LocalDate dueDate;
        private LocalDate returnDate;
        private BorrowStatus status;
        private boolean overdue;
        private long penaltyDays;

        public BorrowResponseBuilder id(Long id) { this.id = id; return this; }
        public BorrowResponseBuilder bookTitle(String bookTitle) { this.bookTitle = bookTitle; return this; }
        public BorrowResponseBuilder bookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; return this; }
        public BorrowResponseBuilder bookId(Long bookId) { this.bookId = bookId; return this; }
        public BorrowResponseBuilder username(String username) { this.username = username; return this; }
        public BorrowResponseBuilder borrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; return this; }
        public BorrowResponseBuilder dueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
        public BorrowResponseBuilder returnDate(LocalDate returnDate) { this.returnDate = returnDate; return this; }
        public BorrowResponseBuilder status(BorrowStatus status) { this.status = status; return this; }
        public BorrowResponseBuilder overdue(boolean overdue) { this.overdue = overdue; return this; }
        public BorrowResponseBuilder penaltyDays(long penaltyDays) { this.penaltyDays = penaltyDays; return this; }

        public BorrowResponse build() {
            BorrowResponse r = new BorrowResponse();
            r.id = id; r.bookTitle = bookTitle; r.bookAuthor = bookAuthor;
            r.bookId = bookId; r.username = username; r.borrowDate = borrowDate;
            r.dueDate = dueDate; r.returnDate = returnDate; r.status = status;
            r.overdue = overdue; r.penaltyDays = penaltyDays;
            return r;
        }
    }
}