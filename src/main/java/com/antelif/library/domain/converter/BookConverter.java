package com.antelif.library.domain.converter;

import com.antelif.library.domain.Book;
import com.antelif.library.domain.dto.BookDto;
import com.antelif.library.infrastructure.entity.BookEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/** BookEntity and BookDto converter. */
@Component
@RequiredArgsConstructor
public class BookConverter implements Converter<Book, BookDto, BookEntity> {

  private final ModelMapper modelMapper;

  @Override
  public Book convertFromDtoToDomain(BookDto bookDto) {
    return modelMapper.map(bookDto, Book.class);
  }

  @Override
  public BookEntity convertFromDomainToEntity(Book book) {
    return modelMapper.map(book, BookEntity.class);
  }

  @Override
  public Book convertFromEntityToDomain(BookEntity bookEntity) {
    return modelMapper.map(bookEntity, Book.class);
  }

  @Override
  public BookDto convertFromDomainToDto(Book book) {

    return modelMapper.map(book, BookDto.class);
  }
}
