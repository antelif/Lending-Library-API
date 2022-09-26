package com.antelif.library.domain.service;

import static com.antelif.library.application.error.GenericError.BOOK_CREATION_FAILED;
import static com.antelif.library.application.error.GenericError.BOOK_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.DUPLICATE_BOOK;

import com.antelif.library.domain.converter.BookConverter;
import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.exception.DuplicateEntityException;
import com.antelif.library.domain.exception.EntityCreationException;
import com.antelif.library.domain.exception.EntityDoesNotExistException;
import com.antelif.library.infrastructure.entity.BookEntity;
import com.antelif.library.infrastructure.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Book Service. */
@RequiredArgsConstructor
@Transactional
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

  /**
   * Retrieve a book from the database by provided isbn.
   *
   * @param isbn of the book to retrieve.
   * @return a book entity if book was retrieved.
   */
  public BookEntity getBookByIsbn(String isbn) {
    var persistedBook = bookRepository.getBookByIsbn(isbn);

    return persistedBook.orElseThrow(() -> new EntityDoesNotExistException(BOOK_DOES_NOT_EXIST));
  }

  /**
   * Retrieve all books from database.
   *
   * @return a list of book response objects.
   */
  public List<BookResponse> getAllBooks() {
    return bookRepository.findAll().stream()
        .map(converter::convertFromEntityToResponse)
        .collect(Collectors.toList());
  }

  /**
   * Retrieve a book from the database by provided id.
   *
   * @param id of the book to retrieve.
   * @return a book response if book was retrieved, else throw a handled exception.
   */
  public BookResponse getBookById(Long id) {
    return bookRepository
        .findById(id)
        .map(converter::convertFromEntityToResponse)
        .orElseThrow(() -> new EntityDoesNotExistException(BOOK_DOES_NOT_EXIST));
  }
}
