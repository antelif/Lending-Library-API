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
   * @param id the author id
   * @return an author entity if exists.
   */
  Optional<AuthorEntity> getAuthorEntityById(Long id);

  /**
   * Retrieves all authors by their name and surname.
   *
   * @param name the name of the author,
   * @param surname the surname of the author,
   * @return a list with all authors retrieved
   */
  List<AuthorEntity> getAuthorEntitiesByNameAndSurname(String name, String surname);

  /**
   * Retrieves all authors by their surname.
   *
   * @param surname the surname of the author,
   * @return a list with all authors retrieved
   */
  List<AuthorEntity> getAuthorEntitiesBySurname(String surname);

  /**
   * Retrieves all authos by their name, surname and middle name.
   *
   * @param name the name of the author,
   * @param surname the surname of the author,
   * @param middleName the middle name of the author
   * @return a list with all author retrieved.
   */
  List<AuthorEntity> getAuthorEntitiesByNameAndSurnameAndMiddleName(
      String name, String surname, String middleName);
}
