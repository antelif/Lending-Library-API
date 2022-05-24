package com.antelif.library.integration;

import com.antelif.library.config.ConfiguredPostgresqlContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@DirtiesContext
@Testcontainers
public class BaseIntegrationTest {
  private static final ConfiguredPostgresqlContainer postgres;

  public static int authorCounter = 0;
  public static int publisherCounter = 0;
  public static int bookCounter = 0;

  static {
    postgres = ConfiguredPostgresqlContainer.getInstance();
    postgres.start();
  }
}
