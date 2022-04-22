package com.antelif.library.domain.service;

import static com.antelif.library.application.error.GenericError.AUTHOR_CONVERTER_FAILED;
import static com.antelif.library.application.error.GenericError.AUTHOR_CREATION_FAILED;
import static com.antelif.library.application.error.GenericError.AUTHOR_DOES_NOT_EXIST;

import com.antelif.library.application.error.GenericError;
import com.antelif.library.domain.converter.AuthorConverter;
import com.antelif.library.domain.dto.AuthorDto;
import com.antelif.library.domain.exception.ConverterException;
import com.antelif.library.domain.exception.DuplicateEntityException;
import com.antelif.library.domain.exception.EntityCreationException;
import com.antelif.library.domain.exception.EntityDoesNotExistException;
import com.antelif.library.infrastructure.repository.AuthorRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
  public Long addAuthor(AuthorDto authorDto) {

    var persistedEntity =
        Optional.ofNullable(authorDto.getMiddleName()).isPresent()
            ? authorRepository.getAuthorEntitiesByNameAndSurnameAndMiddleName(
                authorDto.getName(), authorDto.getSurname(), authorDto.getMiddleName())
            : authorRepository.getAuthorEntitiesByNameAndSurname(
                authorDto.getName(), authorDto.getSurname());

    if (!persistedEntity.isEmpty()) {
      throw new DuplicateEntityException(GenericError.DUPLICATE_AUTHOR);
    }
    return Optional.of(converter.convertFromDtoToDomain(authorDto))
        .map(converter::convertFromDomainToEntity)
        .map(authorRepository::save)
        .orElseThrow(() -> new EntityCreationException(AUTHOR_CREATION_FAILED))
        .getId();
  }

  /**
   * Retrieve an author from the database by provided id.
   *
   * @param id of the author to retrieve.
   * @return an author DTO.
   */
  public AuthorDto getAuthorById(Long id) {
    var persistedAuthor = authorRepository.getAuthorEntityById(id);

    if (persistedAuthor.isEmpty()) {
      throw new EntityDoesNotExistException(AUTHOR_DOES_NOT_EXIST);
    }

    return persistedAuthor
        .map(converter::convertFromEntityToDomain)
        .map(converter::convertFromDomainToDto)
        .orElseThrow(() -> new ConverterException(AUTHOR_CONVERTER_FAILED));
  }

  /**
   * Retrieve authors from the database based on the provided name and surname.
   *
   * @param surname of the author to retrieve.
   * @return a list of author DTOs.
   */
  public List<AuthorDto> getAuthorsBySurname(String surname) {
    var persistedAuthors = authorRepository.getAuthorEntitiesBySurname(surname);

    return persistedAuthors.stream()
        .map(converter::convertFromEntityToDomain)
        .map(converter::convertFromDomainToDto)
        .collect(Collectors.toList());
  }
}
