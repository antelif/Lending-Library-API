package com.antelif.library.domain.service;

import static com.antelif.library.application.error.GenericError.DUPLICATE_PERSONNEL;
import static com.antelif.library.application.error.GenericError.PERSONNEL_CREATION_FAILED;
import static com.antelif.library.application.error.GenericError.PERSONNEL_DOES_NOT_EXIST;

import com.antelif.library.domain.converter.PersonnelConverter;
import com.antelif.library.domain.dto.request.PersonnelRequest;
import com.antelif.library.domain.dto.response.PersonnelResponse;
import com.antelif.library.domain.exception.AuthorizationException;
import com.antelif.library.domain.exception.DuplicateEntityException;
import com.antelif.library.domain.exception.EntityCreationException;
import com.antelif.library.domain.exception.EntityDoesNotExistException;
import com.antelif.library.infrastructure.entity.PersonnelEntity;
import com.antelif.library.infrastructure.repository.PersonnelRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Personnel service. */
@Service
@RequiredArgsConstructor
public class PersonnelService {

  private final PersonnelRepository personnelRepository;
  private final PersonnelConverter converter;

  /**
   * Adds personnel to database.
   *
   * @param personnelRequest the DTO to get information about the personnel to create.
   * @return a personnel response DTO.
   */
  @Transactional
  public PersonnelResponse addPersonnel(PersonnelRequest personnelRequest) {

    var persistedPersonnel =
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
   * Retrieve the logged in personnel.
   *
   * @param personnelRequest the object that contains personnel credentials.
   * @return a personnel response DTO.
   */
  @Transactional
  public PersonnelResponse logInPersonnel(PersonnelRequest personnelRequest) {
    var persistedPersonnel =
        personnelRepository.getPersonnelEntityByUsername(personnelRequest.getUsername());

    if (persistedPersonnel.isEmpty()
        || !persistedPersonnel.get().getPassword().equals(personnelRequest.getPassword())) {
      throw new AuthorizationException();
    }

    return converter.convertFromEntityToResponse(persistedPersonnel.get());
  }

  /**
   * Retrieve personnel from the database by provided id.
   *
   * @param id of the personnel to retrieve.
   * @return a personnel entity object.
   */
  public PersonnelEntity getPersonnelById(Long id) {
    var persistedPersonnel = personnelRepository.getPersonnelById(id);

    return persistedPersonnel.orElseThrow(
        () -> new EntityDoesNotExistException(PERSONNEL_DOES_NOT_EXIST));
  }
}
