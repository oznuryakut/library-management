// test/BookServiceTest.java
package com.libraryapp.backend.service;

import com.libraryapp.backend.dto.request.BookRequest;
import com.libraryapp.backend.dto.response.BookResponse;
import com.libraryapp.backend.exception.ResourceNotFoundException;
import com.libraryapp.backend.model.Book;
import com.libraryapp.backend.repository.BookRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock private BookRepository bookRepository;
    @InjectMocks private BookService bookService;

    private Book sampleBook;
    private BookRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleBook = Book.builder()
                .id(1L).title("Clean Code").author("Robert Martin")
                .isbn("978-0132350884").category("Programming")
                .price(45.0).stock(10).build();

        sampleRequest = new BookRequest();
        sampleRequest.setTitle("Clean Code");
        sampleRequest.setAuthor("Robert Martin");
        sampleRequest.setIsbn("978-0132350884");
        sampleRequest.setCategory("Programming");
        sampleRequest.setPrice(45.0);
        sampleRequest.setStock(10);
    }

    @Test
    @DisplayName("Kitap başarıyla oluşturulmalı")
    void shouldCreateBook() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);

        BookResponse response = bookService.create(sampleRequest);

        assertThat(response.getTitle()).isEqualTo("Clean Code");
        assertThat(response.getAuthor()).isEqualTo("Robert Martin");
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Var olan ISBN ile kitap oluşturmaya çalışınca hata fırlatmalı")
    void shouldThrowWhenIsbnExists() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(true);

        assertThatThrownBy(() -> bookService.create(sampleRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ISBN already exists");
    }

    @Test
    @DisplayName("Var olmayan kitap getirilince ResourceNotFoundException fırlatmalı")
    void shouldThrowWhenBookNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Tüm kitaplar sayfalı şekilde gelmeli")
    void shouldReturnPagedBooks() {
        Page<Book> page = new PageImpl<>(List.of(sampleBook));
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<BookResponse> result = bookService.getAllBooks(null, 0, 10);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Clean Code");
    }
}