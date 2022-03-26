package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.PublisherEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** PublisherEntity repository. */
@Repository
public interface PublisherRepository extends JpaRepository<PublisherEntity, Long> {

  Optional<PublisherEntity> getPublisherById(Long id);

  List<PublisherEntity> getPublishersByName(String publisherName);
}
