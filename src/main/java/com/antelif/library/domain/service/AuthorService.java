package com.antelif.library.domain.service;

import com.antelif.library.domain.converter.AuthorConverter;
import com.antelif.library.domain.dto.AuthorDto;
import com.antelif.library.infrastructure.repository.AuthorRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

  private final AuthorRepository authorRepository;
  private final AuthorConverter converter;

  public AuthorDto addAuthor(AuthorDto authorDto) {

    var persistedEntity =
        authorRepository.getAuthorsByNameAndSurname(authorDto.getName(), authorDto.getSurname());

    if (persistedEntity.size() > 1) {
      throw new RuntimeException("Cannot find specific author");
    }
    return Optional.of(converter.convertFromDtoToDomain(authorDto))
        .map(converter::convertFromDomainToEntity)
        .map(authorRepository::save)
        .map(converter::convertFromEntityToDomain)
        .map(converter::convertFromDomainToDto)
        .orElseThrow(RuntimeException::new);
  }

  public AuthorDto getAuthorById(Long id) {
    var persistedAuthor = authorRepository.getAuthorById(id);

    if (persistedAuthor.isEmpty()) {
      throw new RuntimeException("Cannot ind author by id " + id);
    }

    return persistedAuthor
        .map(converter::convertFromEntityToDomain)
        .map(converter::convertFromDomainToDto)
        .orElseThrow(RuntimeException::new);
  }

  public AuthorDto getAuthorByNameAndSurname(String name, String surname) {
    var persistedAuthors = authorRepository.getAuthorsByNameAndSurname(name, surname);

    if (persistedAuthors.size() > 1) {
      throw new RuntimeException("Cannot find specific author.");
    }
    if (persistedAuthors.stream().findAny().isEmpty()) {
      throw new RuntimeException("No author was found");
    }
    return converter.convertFromDomainToDto(
        converter.convertFromEntityToDomain(persistedAuthors.stream().findAny().get()));
  }
}
