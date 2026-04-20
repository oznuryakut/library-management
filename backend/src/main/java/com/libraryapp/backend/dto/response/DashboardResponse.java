package com.libraryapp.backend.dto.response;

import java.util.List;
import java.util.Map;

public class DashboardResponse {
    private long totalBooks;
    private long totalUsers;
    private long activeBorrows;
    private long overdueBorrows;
    private long totalBorrows;
    private List<Map<String, Object>> topBooks;
    private List<Map<String, Object>> categoryStats;
    private List<Map<String, Object>> monthlyBorrows;

    public DashboardResponse() {}

    public long getTotalBooks() { return totalBooks; }
    public void setTotalBooks(long totalBooks) { this.totalBooks = totalBooks; }
    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    public long getActiveBorrows() { return activeBorrows; }
    public void setActiveBorrows(long activeBorrows) { this.activeBorrows = activeBorrows; }
    public long getOverdueBorrows() { return overdueBorrows; }
    public void setOverdueBorrows(long overdueBorrows) { this.overdueBorrows = overdueBorrows; }
    public long getTotalBorrows() { return totalBorrows; }
    public void setTotalBorrows(long totalBorrows) { this.totalBorrows = totalBorrows; }
    public List<Map<String, Object>> getTopBooks() { return topBooks; }
    public void setTopBooks(List<Map<String, Object>> topBooks) { this.topBooks = topBooks; }
    public List<Map<String, Object>> getCategoryStats() { return categoryStats; }
    public void setCategoryStats(List<Map<String, Object>> categoryStats) { this.categoryStats = categoryStats; }
    public List<Map<String, Object>> getMonthlyBorrows() { return monthlyBorrows; }
    public void setMonthlyBorrows(List<Map<String, Object>> monthlyBorrows) { this.monthlyBorrows = monthlyBorrows; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final DashboardResponse r = new DashboardResponse();
        public Builder totalBooks(long v) { r.totalBooks = v; return this; }
        public Builder totalUsers(long v) { r.totalUsers = v; return this; }
        public Builder activeBorrows(long v) { r.activeBorrows = v; return this; }
        public Builder overdueBorrows(long v) { r.overdueBorrows = v; return this; }
        public Builder totalBorrows(long v) { r.totalBorrows = v; return this; }
        public Builder topBooks(List<Map<String, Object>> v) { r.topBooks = v; return this; }
        public Builder categoryStats(List<Map<String, Object>> v) { r.categoryStats = v; return this; }
        public Builder monthlyBorrows(List<Map<String, Object>> v) { r.monthlyBorrows = v; return this; }
        public DashboardResponse build() { return r; }
    }
}