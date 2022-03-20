package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

  Book getBookByIsbn(String isbn);

  List<Book> getBooksByAuthorName(String authorName);

  List<Book> getBooksByTitleContaining(String title);

  void deleteBookByIsbn(String isbn);

  List<Book> getBooksByPublisherName(String publisherName);
}
