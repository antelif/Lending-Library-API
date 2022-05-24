package com.antelif.library.domain.service;

import static com.antelif.library.application.error.GenericError.BOOK_CREATION_FAILED;
import static com.antelif.library.application.error.GenericError.DUPLICATE_BOOK;

import com.antelif.library.domain.converter.BookConverter;
import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.exception.DuplicateEntityException;
import com.antelif.library.domain.exception.EntityCreationException;
import com.antelif.library.infrastructure.repository.BookRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Book Service. */
@RequiredArgsConstructor
@Service
public class BookService {

  private final BookRepository bookRepository;
  private final BookConverter converter;

  /**
   * Adds a new book to database.
   *
   * @param bookRequest the DTO to get information about the book to create.
   * @return a book response DTO.
   */
  @Transactional
  public BookResponse addBook(BookRequest bookRequest) {
    var persistedEntity = bookRepository.getBookByIsbn(bookRequest.getIsbn());

    if (persistedEntity.isPresent()) {
      throw new DuplicateEntityException(DUPLICATE_BOOK);
    }
    return Optional.of(converter.convertFromRequestToEntity(bookRequest))
        .map(bookRepository::save)
        .map(converter::convertFromEntityToResponse)
        .orElseThrow(() -> new EntityCreationException(BOOK_CREATION_FAILED));
  }
}
