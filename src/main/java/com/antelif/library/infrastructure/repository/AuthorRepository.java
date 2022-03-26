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

  /**
   * Retrieves an author by their id.
   *
   * @param id the author id.
   * @return an author entity if exists.
   */
  Optional<AuthorEntity> getAuthorById(Long id);

  /**
   * Retrieves all authors by their name and surname.
   *
   * @param name the name of the author,
   * @param surname the surname of the author.
   * @return a list with all authors retrieved.
   */
  List<AuthorEntity> getAuthorsByNameAndSurname(String name, String surname);
}
