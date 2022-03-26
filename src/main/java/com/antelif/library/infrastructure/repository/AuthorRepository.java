package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.AuthorEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/** AuthorEntity repository. */
@Component
@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

  Optional<AuthorEntity> getAuthorById(Long id);

  List<AuthorEntity> getAuthorsByNameAndSurname(String authorName, String surname);
}
