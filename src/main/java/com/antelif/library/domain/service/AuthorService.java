package com.antelif.library.domain.service;

import static com.antelif.library.application.error.GenericError.AUTHOR_CONVERTER_FAILED;
import static com.antelif.library.application.error.GenericError.AUTHOR_CREATION_FAILED;
import static com.antelif.library.application.error.GenericError.AUTHOR_DOES_NOT_EXIST;

import com.antelif.library.application.error.GenericError;
import com.antelif.library.domain.converter.AuthorConverter;
import com.antelif.library.domain.dto.request.AuthorRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.domain.exception.ConverterException;
import com.antelif.library.domain.exception.DuplicateEntityException;
import com.antelif.library.domain.exception.EntityCreationException;
import com.antelif.library.domain.exception.EntityDoesNotExistException;
import com.antelif.library.infrastructure.repository.AuthorRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
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
   * @param authorRequest the DTO to get information about the author to create.
   * @return an author response DTO.
   */
  @Transactional
  public AuthorResponse addAuthor(AuthorRequest authorRequest) {

    var persistedEntity =
        Optional.ofNullable(authorRequest.getMiddleName()).isPresent()
            ? authorRepository.getAuthorEntitiesByNameAndSurnameAndMiddleName(
                authorRequest.getName(), authorRequest.getSurname(), authorRequest.getMiddleName())
            : authorRepository.getAuthorEntitiesByNameAndSurname(
                authorRequest.getName(), authorRequest.getSurname());

    if (!persistedEntity.isEmpty()) {
      throw new DuplicateEntityException(GenericError.DUPLICATE_AUTHOR);
    }
    return Optional.of(converter.convertFromRequestToEntity(authorRequest))
        .map(authorRepository::save)
        .map(converter::convertFromEntityToResponse)
        .orElseThrow(() -> new EntityCreationException(AUTHOR_CREATION_FAILED));
  }

  /**
   * Retrieve an author from the database by provided id.
   *
   * @param id of the author to retrieve.
   * @return an author response DTO.
   */
  public AuthorResponse getAuthorById(Long id) {
    var persistedAuthor = authorRepository.getAuthorEntityById(id);

    if (persistedAuthor.isEmpty()) {
      throw new EntityDoesNotExistException(AUTHOR_DOES_NOT_EXIST);
    }

    return persistedAuthor
        .map(converter::convertFromEntityToResponse)
        .orElseThrow(() -> new ConverterException(AUTHOR_CONVERTER_FAILED));
  }

  /**
   * Retrieve authors from the database based on the provided name and surname.
   *
   * @param surname of the author to retrieve.
   * @return a list of author response DTOs.
   */
  public List<AuthorResponse> getAuthorsBySurname(String surname) {
    var persistedAuthors = authorRepository.getAuthorEntitiesBySurname(surname);

    return persistedAuthors.stream()
        .map(converter::convertFromEntityToResponse)
        .collect(Collectors.toList());
  }
}
