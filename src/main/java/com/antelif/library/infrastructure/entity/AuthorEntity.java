package com.antelif.library.infrastructure.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

/** Author entity object that gets persisted in database. */
@Getter
@Setter
@Entity
@Table(name = "author")
public class AuthorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String middleName;

  private String surname;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    AuthorEntity that = (AuthorEntity) o;
    return name.equals(that.getName())
        && surname.equals(that.getSurname())
        && middleName.equals(that.getMiddleName());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
