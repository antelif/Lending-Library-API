package com.antelif.library.infrastructure.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

<<<<<<< HEAD:src/main/java/com/antelif/library/infrastructure/entity/AuthorEntity.java
/** AuthorEntity entity object that gets persisted in database. */
@Getter
@Setter
@Entity
@Table(name = "author")
public class AuthorEntity {
=======
/** PublisherEntity entity object that gets persisted in database. */
@Getter
@Setter
@Entity
@Table(name = "publisher")
public class PublisherEntity {
>>>>>>> adcfe2e... Add Author controller:src/main/java/com/antelif/library/infrastructure/entity/PublisherEntity.java

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String middleName;

  private String surname;
}
