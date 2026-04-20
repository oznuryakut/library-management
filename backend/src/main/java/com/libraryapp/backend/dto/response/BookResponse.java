package com.libraryapp.backend.dto.response;

public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private Double price;
    private Integer stock;

    public BookResponse() {}

    public BookResponse(Long id, String title, String author, String isbn, String category, Double price, Integer stock) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public static BookResponseBuilder builder() { return new BookResponseBuilder(); }

    public static class BookResponseBuilder {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private String category;
        private Double price;
        private Integer stock;

        public BookResponseBuilder id(Long id) { this.id = id; return this; }
        public BookResponseBuilder title(String title) { this.title = title; return this; }
        public BookResponseBuilder author(String author) { this.author = author; return this; }
        public BookResponseBuilder isbn(String isbn) { this.isbn = isbn; return this; }
        public BookResponseBuilder category(String category) { this.category = category; return this; }
        public BookResponseBuilder price(Double price) { this.price = price; return this; }
        public BookResponseBuilder stock(Integer stock) { this.stock = stock; return this; }
        public BookResponse build() { return new BookResponse(id, title, author, isbn, category, price, stock); }
    }
}