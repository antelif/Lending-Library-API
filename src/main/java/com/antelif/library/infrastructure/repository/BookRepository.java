package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.BookEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * BookEntity repository.
 */
@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

  /**
   * Retrieves a book by the isbn provided.
   *
   * @param isbn the isbn of the book.
   * @return a book entity if exists.
   */
  Optional<BookEntity> getBookByIsbn(String isbn);
}
