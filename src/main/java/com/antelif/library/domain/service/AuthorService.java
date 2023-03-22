package com.antelif.library.domain.service;

import static com.antelif.library.application.error.GenericError.AUTHOR_CREATION_FAILED;
import static com.antelif.library.application.error.GenericError.AUTHOR_DOES_NOT_EXIST;
import static com.antelif.library.application.error.GenericError.DUPLICATE_AUTHOR;

import com.antelif.library.domain.converter.AuthorConverter;
import com.antelif.library.domain.dto.request.AuthorRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.domain.exception.DuplicateEntityException;
import com.antelif.library.domain.exception.EntityCreationException;
import com.antelif.library.domain.exception.EntityDoesNotExistException;
import com.antelif.library.infrastructure.entity.AuthorEntity;
import com.antelif.library.infrastructure.repository.AuthorRepository;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Author service.
 */
@Service
@Transactional
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
  public AuthorResponse addAuthor(AuthorRequest authorRequest) {

    List<AuthorEntity> persistedEntity =
        Optional.ofNullable(authorRequest.getMiddleName()).isPresent()
            ? authorRepository.getAuthorEntitiesByNameAndSurnameAndMiddleName(
            authorRequest.getName(), authorRequest.getSurname(), authorRequest.getMiddleName())
            : authorRepository.getAuthorEntitiesByNameAndSurname(
                authorRequest.getName(), authorRequest.getSurname());

    if (!persistedEntity.isEmpty()) {
      throw new DuplicateEntityException(DUPLICATE_AUTHOR);
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
   * @return an author entity object.
   */
  public AuthorEntity getAuthorById(Long id) {
    Optional<AuthorEntity> persistedAuthor = authorRepository.getAuthorEntityById(id);

    return persistedAuthor.orElseThrow(
        () -> new EntityDoesNotExistException(AUTHOR_DOES_NOT_EXIST));
  }
}
