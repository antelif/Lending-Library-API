package com.antelif.library.domain.converter;

import com.antelif.library.domain.dto.BookDto;
import com.antelif.library.infrastructure.entity.Book;
import com.antelif.library.infrastructure.repository.AuthorRepository;
import com.antelif.library.infrastructure.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Book and BookDto converter. */
@Component
@RequiredArgsConstructor
public class BookConverter implements Converter<Book, BookDto> {

  private final PublisherRepository publisherRepository;
  private final AuthorRepository authorRepository;

  @Override
  public Book convertToDomain(BookDto bookDto) {

    Book book = new Book();
    book.setIsbn(bookDto.getIsbn());
    book.setTitle(bookDto.getTitle());

    book.setPublisher(
        publisherRepository
            .getPublisherById(bookDto.getAuthorId())
            .orElseThrow(RuntimeException::new));

    book.setAuthor(
        authorRepository.getAuthorById(bookDto.getAuthorId()).orElseThrow(RuntimeException::new));

    return book;
  }

  @Override
  public BookDto convertToDto(Book book) {
    return null;
  }
}
