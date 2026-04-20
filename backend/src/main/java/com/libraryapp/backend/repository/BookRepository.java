// BookRepository.java
package com.libraryapp.backend.repository;

import com.libraryapp.backend.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
        String title, String author, Pageable pageable
    );
    boolean existsByIsbn(String isbn);
}