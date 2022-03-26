package com.antelif.library.domain.converter;

import com.antelif.library.domain.Author;
import com.antelif.library.domain.dto.AuthorDto;
import com.antelif.library.infrastructure.entity.AuthorEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorConverter implements Converter<Author, AuthorDto, AuthorEntity> {

  private final ModelMapper modelMapper;

  @Override
  public Author convertFromDtoToDomain(AuthorDto authorDto) {
    return modelMapper.map(authorDto, Author.class);
  }

  @Override
  public AuthorEntity convertFromDomainToEntity(Author author) {
    return modelMapper.map(author, AuthorEntity.class);
  }

  @Override
  public Author convertFromEntityToDomain(AuthorEntity authorEntity) {
    return modelMapper.map(authorEntity, Author.class);
  }

  @Override
  public AuthorDto convertFromDomainToDto(Author author) {
    return modelMapper.map(author, AuthorDto.class);
  }
}
