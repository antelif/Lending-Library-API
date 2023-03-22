package com.antelif.library.domain.service;

import static com.antelif.library.application.error.GenericError.BOOK_COPY_CREATION_FAILED;
import static com.antelif.library.application.error.GenericError.BOOK_COPY_DOES_NOT_EXIST;
import static com.antelif.library.domain.service.validation.BookCopyValidationService.validateUpdate;

import com.antelif.library.domain.converter.BookCopyConverter;
import com.antelif.library.domain.dto.request.BookCopyRequest;
import com.antelif.library.domain.dto.response.BookCopyResponse;
import com.antelif.library.domain.exception.EntityCreationException;
import com.antelif.library.domain.exception.EntityDoesNotExistException;
import com.antelif.library.domain.service.validation.BookCopyValidationService;
import com.antelif.library.domain.type.State;
import com.antelif.library.infrastructure.entity.BookCopyEntity;
import com.antelif.library.infrastructure.repository.BookCopyRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Book copy service. */
@Service
@Transactional
@RequiredArgsConstructor
public class BookCopyService {

  private final BookCopyConverter converter;
  private final BookCopyRepository repository;

  /**
   * Adds a book copy to database.
   *
   * @param bookCopyRequest the DTO to get information about the book copy to create.
   * @return a book copy response DTO.
   */
  public BookCopyResponse addBookCopy(BookCopyRequest bookCopyRequest) {

    return Optional.of(converter.convertFromRequestToEntity(bookCopyRequest))
        .map(repository::save)
        .map(converter::convertFromEntityToResponse)
        .orElseThrow(() -> new EntityCreationException(BOOK_COPY_CREATION_FAILED));
  }

  /**
   * Updates the state of a book copy.
   * @param bookCopyId the book copy id of which to change the state.
   * @param state the new book copy state.
   * @return a book copy response DTO.
   */
  public BookCopyResponse updateBookCopyState(Long bookCopyId, State state) {

    var bookCopy = repository.findById(bookCopyId)
        .orElseThrow(() -> new EntityDoesNotExistException(BOOK_COPY_DOES_NOT_EXIST));

    validateUpdate(bookCopy, state);

    bookCopy.setState(state);

    return converter.convertFromEntityToResponse(bookCopy);
  }

  /**
   * Gets all book copies by their ids.
   *
   * @param bookCopyIds the ids of the book copies to fetch.
   * @return a list of book copy entity objects.
   */
  public List<BookCopyEntity> getBookCopiesByBookCopyIds(List<Long> bookCopyIds) {
    return new ArrayList<>(repository.findAllById(bookCopyIds));
  }
}
