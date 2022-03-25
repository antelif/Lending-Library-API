package com.antelif.library.domain.converter;

/**
 * Converter used to convert DTOs to entities and entities to DTOs.
 *
 * @param <E> the entity object type.
 * @param <D> the domain object type.
 */
public interface Converter<E, D> {

  /**
   * Converts a DTO to an entity object.
   *
   * @param u the DTO object.
   * @return an entity object.
   */
  E convertToDomain(D u);

  /**
   * Converts an entity object to a DTO object.
   *
   * @param e the entity object.
   * @return a DTO objecto.
   */
  D convertToDto(E e);
}
