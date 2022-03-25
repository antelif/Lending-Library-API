package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.Author;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/** Author repository. */
@Component
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

  Optional<Author> getAuthorById(Long id);
}
