package com.antelif.library.domain.converter;

import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.domain.service.AuthorService;
import com.antelif.library.domain.service.PublisherService;
import com.antelif.library.infrastructure.entity.AuthorEntity;
import com.antelif.library.infrastructure.entity.BookEntity;
import com.antelif.library.infrastructure.entity.PublisherEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/** Converter for Book objects. */
@Component
@RequiredArgsConstructor
public class BookConverter implements Converter<BookRequest, BookEntity, BookResponse> {

  private final ModelMapper modelMapper;
  private final AuthorService authorService;
  private final PublisherService publisherService;

  private final AuthorConverter authorConverter;
  private final PublisherConverter publisherConverter;

  @Override
  public BookEntity convertFromRequestToEntity(BookRequest bookRequest) {
    var bookEntity = modelMapper.map(bookRequest, BookEntity.class);

    // Decorate
    var authorResponse = authorService.getAuthorById(bookRequest.getAuthorId());
    var authorEntity = modelMapper.map(authorResponse, AuthorEntity.class);

    bookEntity.setAuthor(authorEntity);

    var publisherResponse = publisherService.getPublisherById(bookRequest.getPublisherId());
    var publisherEntity = modelMapper.map(publisherResponse, PublisherEntity.class);

    bookEntity.setPublisher(publisherEntity);

    return bookEntity;
  }

  @Override
  public BookResponse convertFromEntityToResponse(BookEntity bookEntity) {
    var bookResponse = modelMapper.map(bookEntity, BookResponse.class);

    // Decorate
    bookResponse.setAuthor(authorConverter.convertFromEntityToResponse(bookEntity.getAuthor()));
    bookResponse.setPublisher(
        publisherConverter.convertFromEntityToResponse(bookEntity.getPublisher()));

    return bookResponse;
  }
}
