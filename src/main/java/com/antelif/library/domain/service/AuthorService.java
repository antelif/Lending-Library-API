package com.antelif.library.domain.service;

import com.antelif.library.domain.converter.AuthorConverter;
import com.antelif.library.domain.dto.AuthorDto;
import com.antelif.library.infrastructure.repository.AuthorRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Author service. */
@Service
@RequiredArgsConstructor
public class AuthorService {

  private final AuthorRepository authorRepository;
  private final AuthorConverter converter;

  /**
   * Adds author to database.
   *
   * @param authorDto the DTO to get information about the author to create.
   * @return n author DTO.
   */
  public AuthorDto addAuthor(AuthorDto authorDto) {

    var persistedEntity =
        authorRepository.getAuthorsByNameAndSurname(authorDto.getName(), authorDto.getSurname());

    if (persistedEntity.size() >= 1) {
      throw new RuntimeException("Cannot find specific author");
    }
    return Optional.of(converter.convertFromDtoToDomain(authorDto))
        .map(converter::convertFromDomainToEntity)
        .map(authorRepository::save)
        .map(converter::convertFromEntityToDomain)
        .map(converter::convertFromDomainToDto)
        // TODO: Add dedicated exception.
        .orElseThrow(RuntimeException::new);
  }

  /**
   * Retrieve an author from the database by provided id.
   *
   * @param id of the author to retrieve.
   * @return an author DTO.
   */
  public AuthorDto getAuthorById(Long id) {
    var persistedAuthor = authorRepository.getAuthorById(id);

    if (persistedAuthor.isEmpty()) {
      // TODO: Replace with dedicated exception.
      throw new RuntimeException("Cannot ind author by id " + id);
    }

    return persistedAuthor
        .map(converter::convertFromEntityToDomain)
        .map(converter::convertFromDomainToDto)
        // TODO: Add a dedicated exception.
        .orElseThrow(RuntimeException::new);
  }

  /**
   * Retrieve an author from the database based on the provided name and surname. If more than one
   * authors are retrieved return error.
   *
   * @param name of the author to retrieve,
   * @param surname of the author to retrieve.
   * @return and author DTO.
   */
  public AuthorDto getAuthorByNameAndSurname(String name, String surname) {
    var persistedAuthors = authorRepository.getAuthorsByNameAndSurname(name, surname);

    if (persistedAuthors.size() > 1) {
      // TODO: Replace with dedicated exception.
      throw new RuntimeException("Cannot find specific author.");
    }
    if (persistedAuthors.stream().findAny().isEmpty()) {
      // TODO: Replace wih dedicated exception.
      throw new RuntimeException("No author was found");
    }
    return converter.convertFromDomainToDto(
        converter.convertFromEntityToDomain(
            // TODO: Replace with dedicated exception.
            persistedAuthors.stream().findAny().orElseThrow(RuntimeException::new)));
  }
}
