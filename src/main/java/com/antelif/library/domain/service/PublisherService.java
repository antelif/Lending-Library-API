package com.antelif.library.domain.service;

import static com.antelif.library.application.error.GenericError.DUPLICATE_PUBLISHER;
import static com.antelif.library.application.error.GenericError.PUBLISHER_CREATION_FAILED;
import static com.antelif.library.application.error.GenericError.PUBLISHER_DOES_NOT_EXIST;

import com.antelif.library.domain.converter.PublisherConverter;
import com.antelif.library.domain.dto.request.PublisherRequest;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.antelif.library.domain.exception.DuplicateEntityException;
import com.antelif.library.domain.exception.EntityCreationException;
import com.antelif.library.domain.exception.EntityDoesNotExistException;
import com.antelif.library.infrastructure.entity.PublisherEntity;
import com.antelif.library.infrastructure.repository.PublisherRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Publisher service.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PublisherService {

  private final PublisherRepository publisherRepository;
  private final PublisherConverter converter;

  /**
   * Adds publisher to database.
   *
   * @param publisherRequest the request DTO to get information about the publisher to create.
   * @return a publisher response DTO.
   */
  public PublisherResponse addPublisher(PublisherRequest publisherRequest) {

    Optional<List<PublisherEntity>> persistedEntity =
        Optional.ofNullable(publisherRepository.getPublishersByName(publisherRequest.getName()));

    if (persistedEntity.isPresent() && !persistedEntity.get().isEmpty()) {
      throw new DuplicateEntityException(DUPLICATE_PUBLISHER);
    }

    return Optional.of(converter.convertFromRequestToEntity(publisherRequest))
        .map(publisherRepository::save)
        .map(converter::convertFromEntityToResponse)
        .orElseThrow(() -> new EntityCreationException(PUBLISHER_CREATION_FAILED));
  }

  /**
   * Gets a list of all publishers persisted in database.
   *
   * @return a list of publisher response object.
   */
  public List<PublisherResponse> getAllPublishers() {
    return publisherRepository.findAll().stream()
        .map(converter::convertFromEntityToResponse)
        .collect(Collectors.toList());
  }

  /**
   * Retrieve a publisher from the database by provided id.
   *
   * @param id of the publisher to retrieve.
   * @return a publisher entity object.
   */
  public PublisherEntity getPublisherById(Long id) {
    Optional<PublisherEntity> persistedPublisher = publisherRepository.getPublisherById(id);

    return persistedPublisher.orElseThrow(
        () -> new EntityDoesNotExistException(PUBLISHER_DOES_NOT_EXIST));
  }
}
