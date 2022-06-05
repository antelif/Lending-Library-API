package com.antelif.library.configuration;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public ModelMapper strictModelMapper() {
    ModelMapper strictMapper = new ModelMapper();
    strictMapper.getConfiguration().setMatchingStrategy(STRICT);
    return strictMapper;
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
