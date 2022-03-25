package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.Publisher;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Publisher repository. */
@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

  Optional<Publisher> getPublisherById(Long Id);
}
