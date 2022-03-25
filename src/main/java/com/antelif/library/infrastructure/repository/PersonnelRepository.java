package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.Personnel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Personnel Repository. */
@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Long> {

  Optional<Personnel> getPersonnelById(Long id);
}
