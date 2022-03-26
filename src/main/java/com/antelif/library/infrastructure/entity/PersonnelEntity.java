package com.antelif.library.infrastructure.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/** PersonnelEntity entity object that gets persisted in database. */
@Setter
@Getter
@Entity
@Table(name = "personnel")
public class PersonnelEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  private String password;
}
