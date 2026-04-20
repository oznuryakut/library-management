package com.libraryapp.backend.service;

import com.libraryapp.backend.dto.request.BookRequest;
import com.libraryapp.backend.dto.response.BookResponse;
import com.libraryapp.backend.exception.ResourceNotFoundException;
import com.libraryapp.backend.model.Book;
import com.libraryapp.backend.repository.BookRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<BookResponse> getAllBooks(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        Page<Book> books = (search == null || search.isBlank())
                ? bookRepository.findAll(pageable)
                : bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                        search, search, pageable);
        return books.map(this::toResponse);
    }

    public BookResponse getById(Long id) {
        return toResponse(findBook(id));
    }

    public BookResponse create(BookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn()))
            throw new IllegalArgumentException("ISBN already exists");
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .category(request.getCategory())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();
        return toResponse(bookRepository.save(book));
    }

    public BookResponse update(Long id, BookRequest request) {
        Book book = findBook(id);
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setCategory(request.getCategory());
        book.setPrice(request.getPrice());
        book.setStock(request.getStock());
        return toResponse(bookRepository.save(book));
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id))
            throw new ResourceNotFoundException("Book not found: " + id);
        bookRepository.deleteById(id);
    }

    private Book findBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id));
    }

    private BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .category(book.getCategory())
                .price(book.getPrice())
                .stock(book.getStock())
                .build();
    }
}