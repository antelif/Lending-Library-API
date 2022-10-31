package com.antelif.library.configuration;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@Configuration
public class AppConfig {

  @Bean
  public ModelMapper modelMapper() {
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
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return objectMapper;
  }
}
