package com.antelif.library.configuration;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

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
}
