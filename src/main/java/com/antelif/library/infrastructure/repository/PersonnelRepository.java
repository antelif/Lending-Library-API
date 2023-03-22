package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.PersonnelEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PersonnelEntity repository.
 */
@Repository
public interface PersonnelRepository extends JpaRepository<PersonnelEntity, Long> {

  /**
   * Retrieve personnel by their id.
   *
   * @param id the id of the personnel.
   * @return a personnel entity if exists.
   */
  Optional<PersonnelEntity> getPersonnelById(Long id);

  /**
   * Retrieve personnel by their username.
   *
   * @param username the username for the personnel.
   * @return a personnel entity if exists.
   */
  Optional<PersonnelEntity> getPersonnelEntityByUsername(String username);
}
