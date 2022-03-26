package com.antelif.library.domain.converter;

/**
 * Converter used to convert DTOs to entities and entities to DTOs.
 *
 * @param <D> the entity object type.
 * @param <D> the domain object type.
 */
public interface Converter<D, P, E> {

  /**
   * Converts a DTO to a domain object.
   *
   * @param p the DTO object.
   * @return a domain object.
   */
  D convertFromDtoToDomain(P p);

  /**
   * Converts a domain object to an entity object.
   *
   * @param d the domain object.
   * @return an entity object.
   */
  E convertFromDomainToEntity(D d);

  /**
   * Convert
   * @param d
   * @return
   */
  D convertFromEntityToDomain(E d);

  P convertFromDomainToDto(D d);


}
