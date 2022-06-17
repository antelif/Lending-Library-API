package com.antelif.library.domain.service;

import static com.antelif.library.application.error.GenericError.BOOK_COPY_CREATION_FAILED;

import com.antelif.library.domain.converter.BookCopyConverter;
import com.antelif.library.domain.dto.request.BookCopyRequest;
import com.antelif.library.domain.dto.response.BookCopyResponse;
import com.antelif.library.domain.exception.EntityCreationException;
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
   * Gets all book copies by their ids.
   *
   * @param bookCopyIds the ids of the book copies to fetch.
   * @return a list of book copy entity objects.
   */
  public List<BookCopyEntity> getBookCopiesByBookCopyIds(List<Long> bookCopyIds) {
    return new ArrayList<>(repository.getByIdIn(bookCopyIds));
  }
}
