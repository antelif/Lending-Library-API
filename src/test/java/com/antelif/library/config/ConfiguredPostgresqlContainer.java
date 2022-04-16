package com.antelif.library.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class ConfiguredPostgresqlContainer
    extends PostgreSQLContainer<ConfiguredPostgresqlContainer> {

  private static final String IMAGE_VERSION = "postgres:12.7-alpine";
  private static ConfiguredPostgresqlContainer postgresqlContainer;

  ConfiguredPostgresqlContainer() {
    super(IMAGE_VERSION);
  }

  public static ConfiguredPostgresqlContainer getInstance() {

    if (postgresqlContainer == null) {
      postgresqlContainer = new ConfiguredPostgresqlContainer();
    }
    return postgresqlContainer;
  }

  @Override
  public void start() {
    super.start();
    System.setProperty("spring.datasource.url", postgresqlContainer.getJdbcUrl());
    System.setProperty("spring.datasource.username", postgresqlContainer.getUsername());
    System.setProperty("spring.datasource.password", postgresqlContainer.getPassword());
  }
}
