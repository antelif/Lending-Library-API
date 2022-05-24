package com.antelif.library.domain.factory;

import com.antelif.library.domain.converter.Converter;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Factory with methods to retrieves appropriate converter for each class. */
@Component
@RequiredArgsConstructor
public class ConverterFactory {

  private final Map<String, Converter> converterMap;

  /**
   * Gets the converter for the class provided.
   *
   * @param className the name of the class to retrieve converter for.
   * @return the appropriate converter implementation.
   */
  public Optional<Converter> getConverter(String className) {
    return Optional.ofNullable(converterMap.get(className));
  }
}
