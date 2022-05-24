package com.antelif.library.domain.converter;

/**
 * Converter used to convert DTOs to entities and entities to DTOs.
 *
 * @param <I> the request DTO object type.
 * @param <E> the entity object type.
 * @param <O> the response DTO object type.
 */
public interface Converter<I, E, O> {

  /**
   * Converts a request DTO to an entity object.
   *
   * @param i the request DTO object.
   * @return a domain object.
   */
  E convertFromRequestToEntity(I i);

  /**
   * Converts an entity object to a response DTO object.
   *
   * @param e the entity object.
   * @return an response DTO object.
   */
  O convertFromEntityToResponse(E e);
}
