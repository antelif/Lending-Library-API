package com.antelif.library.domain.converter;

import static com.antelif.library.application.error.GenericError.AUTHOR_DOES_NOT_EXIST;

import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.exception.EntityDoesNotExistException;
import com.antelif.library.infrastructure.entity.BookEntity;
import com.antelif.library.infrastructure.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/** Converter for Book objects. */
@Component
@RequiredArgsConstructor
public class BookConverter implements Converter<BookRequest, BookEntity, BookResponse> {

  private final ModelMapper modelMapper;
  private final AuthorRepository authorRepository;

  @Override
  public BookEntity convertFromRequestToEntity(BookRequest bookRequest) {
    var bookEntity = modelMapper.map(bookRequest, BookEntity.class);

    // Decorate
    // FIXME: Do not use repository.
    bookEntity.setAuthor(
        authorRepository
            .getAuthorEntityById(bookRequest.getAuthorId())
            .orElseThrow(() -> new EntityDoesNotExistException(AUTHOR_DOES_NOT_EXIST)));

    // TODO: Add publisher.

    return bookEntity;
  }

  @Override
  public BookResponse convertFromEntityToResponse(BookEntity bookEntity) {
    var bookResponse = modelMapper.map(bookEntity, BookResponse.class);

    // Decorate
    bookResponse.setAuthorId(String.valueOf(bookEntity.getAuthor().getId()));

    return bookResponse;
  }
}
