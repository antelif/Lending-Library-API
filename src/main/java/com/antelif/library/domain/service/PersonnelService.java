package com.antelif.library.domain.service;

import static com.antelif.library.application.error.GenericError.DUPLICATE_PERSONNEL;
import static com.antelif.library.application.error.GenericError.PERSONNEL_CREATION_FAILED;
import static com.antelif.library.application.error.GenericError.PERSONNEL_DOES_NOT_EXIST;

import com.antelif.library.configuration.AppProperties;
import com.antelif.library.domain.converter.PersonnelConverter;
import com.antelif.library.domain.dto.request.PersonnelRequest;
import com.antelif.library.domain.dto.response.PersonnelResponse;
import com.antelif.library.domain.exception.DuplicateEntityException;
import com.antelif.library.domain.exception.EntityCreationException;
import com.antelif.library.domain.exception.EntityDoesNotExistException;
import com.antelif.library.infrastructure.entity.PersonnelEntity;
import com.antelif.library.infrastructure.repository.PersonnelRepository;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** Personnel service. */
@Service
@Slf4j
@RequiredArgsConstructor
public class PersonnelService {

  private final PersonnelRepository personnelRepository;
  private final AppProperties appProperties;
  private final PersonnelConverter converter;
  private final PasswordEncoder passwordEncoder;

  /**
   * Adds personnel to database.
   *
   * @param personnelRequest the DTO to get information about the personnel to create.
   * @return a personnel response DTO.
   */
  @Transactional
  public PersonnelResponse addPersonnel(PersonnelRequest personnelRequest) {

    personnelRequest.setPassword(passwordEncoder.encode(personnelRequest.getPassword()));

    Optional<PersonnelEntity> persistedPersonnel =
        personnelRepository.getPersonnelEntityByUsername(personnelRequest.getUsername());

    if (persistedPersonnel.isPresent()) {
      throw new DuplicateEntityException(DUPLICATE_PERSONNEL);
    }

    return Optional.of(converter.convertFromRequestToEntity(personnelRequest))
        .map(personnelRepository::save)
        .map(converter::convertFromEntityToResponse)
        .orElseThrow(() -> new EntityCreationException(PERSONNEL_CREATION_FAILED));
  }

  /**
   * Retrieve personnel from the database by provided id.
   *
   * @param id of the personnel to retrieve.
   * @return a personnel entity object.
   */
  public PersonnelEntity getPersonnelById(Long id) {
    Optional<PersonnelEntity> persistedPersonnel = personnelRepository.getPersonnelById(id);

    return persistedPersonnel.orElseThrow(
        () -> new EntityDoesNotExistException(PERSONNEL_DOES_NOT_EXIST));
  }

  /** Initializes a root user to enable access to requests until new users are created. */
  @PostConstruct
  public void initializeRootUser() {
    PersonnelRequest rootPersonnel = new PersonnelRequest(appProperties.getRootUser());

    try {
      log.info("Creating root user.");
      addPersonnel(rootPersonnel);
    } catch (DuplicateEntityException exception) {
      log.info("Root user has been already created.");
    }
  }
}
