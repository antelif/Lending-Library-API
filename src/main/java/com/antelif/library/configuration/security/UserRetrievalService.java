package com.antelif.library.configuration.security;

import static com.antelif.library.application.error.GenericError.PERSONNEL_DOES_NOT_EXIST;

import com.antelif.library.domain.exception.EntityDoesNotExistException;
import com.antelif.library.infrastructure.entity.PersonnelEntity;
import com.antelif.library.infrastructure.repository.PersonnelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Contains functionality to retrieve a personnel from the database for security purposes.
 */
@Service
@RequiredArgsConstructor
public class UserRetrievalService {

  private final PersonnelRepository personnelRepository;

  /**
   * Retrieves a personnel entity from database.
   *
   * @param username the username of teh personnel.
   * @return a personnel entity if an entity was found for the provided username.
   */
  public PersonnelEntity retrievePersonnelByUserName(String username) {
    return personnelRepository
        .getPersonnelEntityByUsername(username)
        .orElseThrow(() -> new EntityDoesNotExistException(PERSONNEL_DOES_NOT_EXIST));
  }
}
