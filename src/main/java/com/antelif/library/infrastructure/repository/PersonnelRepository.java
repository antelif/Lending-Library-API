package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.PersonnelEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** PersonnelEntity Repository. */
@Repository
public interface PersonnelRepository extends JpaRepository<PersonnelEntity, Long> {

  /**
   * Get a Personnel entity object from database with the given id.
   *
   * @param id the id for the personnel entity.
   * @return a personnel entity if it exists.
   */
  Optional<PersonnelEntity> getPersonnelById(Long id);
}
