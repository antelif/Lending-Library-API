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

  static {
    postgres = ConfiguredPostgresqlContainer.getInstance();
    postgres.start();
  }
}
