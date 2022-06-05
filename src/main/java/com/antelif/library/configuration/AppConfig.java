package com.antelif.library.configuration;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
    var javaTimeModule = new JavaTimeModule();
    var objectMapper = new ObjectMapper();
    objectMapper
        .registerModule(javaTimeModule)
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    return objectMapper;
  }
}
