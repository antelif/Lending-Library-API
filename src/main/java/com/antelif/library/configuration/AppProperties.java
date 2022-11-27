package com.antelif.library.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** Application properties to load environmental variables. */
@ConfigurationProperties("app.properties")
@Configuration
@Getter
@Setter
public class AppProperties {
  private Double dailyFeeRate;
  private RootUser rootUser;

  @Getter
  @Setter
  public static class RootUser {
    private String username;
    private String password;
  }
}
