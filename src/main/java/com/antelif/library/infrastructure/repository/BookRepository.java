package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** BookEntity repository. */
@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

  BookEntity getBookByIsbn(String isbn);

  //  List<BookEntity> getBooksByAuthorName(String authorName);
  //
  //  List<BookEntity> getBooksByTitleContaining(String title);

  void deleteBookByIsbn(String isbn);

  //  List<BookEntity> getBooksByPublisherName(String publisherName);
}
