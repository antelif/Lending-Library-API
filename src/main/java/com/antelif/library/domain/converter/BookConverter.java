package com.antelif.library.domain.converter;

import com.antelif.library.domain.dto.request.BookRequest;
import com.antelif.library.domain.dto.response.BookResponse;
import com.antelif.library.infrastructure.entity.BookEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/** BookEntity and BookDto converter. */
@Component
@RequiredArgsConstructor
public class BookConverter implements Converter<BookRequest, BookEntity, BookResponse> {

  private final ModelMapper modelMapper;

  @Override
  public BookEntity convertFromRequestToEntity(BookRequest bookRequest) {
    return modelMapper.map(bookRequest, BookEntity.class);
  }

  @Override
  public BookResponse convertFromEntityToResponse(BookEntity bookEntity) {
    return modelMapper.map(bookEntity, BookResponse.class);
  }
}
