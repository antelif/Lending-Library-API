package com.antelif.library.integration;

import com.antelif.library.config.ConfiguredPostgresqlContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class BaseIntegrationTest {

  @Autowired public ObjectMapper objectMapper;

  private static final ConfiguredPostgresqlContainer postgres;

  public static int authorCounter = 0;
  public static int publisherCounter = 0;
  public static int bookCounter = 0;
  public static int personnelCounter = 0;
  public static int customerCounter = 0;

  static {
    postgres = ConfiguredPostgresqlContainer.getInstance();
    postgres.start();
  }
}
