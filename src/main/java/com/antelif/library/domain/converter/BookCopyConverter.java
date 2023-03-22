package com.antelif.library.domain.converter;

import com.antelif.library.domain.dto.request.BookCopyRequest;
import com.antelif.library.domain.dto.response.BookCopyResponse;
import com.antelif.library.domain.service.BookService;
import com.antelif.library.infrastructure.entity.BookCopyEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/** Converter for book copy objects. */
@Component
@RequiredArgsConstructor
public class BookCopyConverter
    implements Converter<BookCopyRequest, BookCopyEntity, BookCopyResponse> {

  private final ModelMapper modelMapper;

  private final BookService bookService;

  private final BookConverter bookConverter;

  @Override
  public BookCopyEntity convertFromRequestToEntity(BookCopyRequest bookCopyRequest) {

    BookCopyEntity bookCopy = modelMapper.map(bookCopyRequest, BookCopyEntity.class);
    bookCopy.setBook(bookService.getBookByIsbn(bookCopyRequest.getIsbn()));

    return bookCopy;
  }

  @Override
  public BookCopyResponse convertFromEntityToResponse(BookCopyEntity bookCopyEntity) {
    return modelMapper.map(bookCopyEntity, BookCopyResponse.class);
  }
}
