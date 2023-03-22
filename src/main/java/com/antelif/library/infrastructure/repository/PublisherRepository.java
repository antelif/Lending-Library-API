package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.PublisherEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PublisherEntity repository.
 */
@Repository
public interface PublisherRepository extends JpaRepository<PublisherEntity, Long> {

  /**
   * Get a publisher by their id.
   *
   * @param id the id of the publisher to retrieve from database.
   * @return a publisher entity if exists.
   */
  Optional<PublisherEntity> getPublisherById(Long id);

  /**
   * Get a list of publishers for the provided publisher name.
   *
   * @param publisherName the name of the publisher to retrieve.
   * @return alist with publishers.
   */
  List<PublisherEntity> getPublishersByName(String publisherName);
}
