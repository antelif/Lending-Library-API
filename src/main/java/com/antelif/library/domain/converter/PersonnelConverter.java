package com.antelif.library.domain.converter;

import com.antelif.library.domain.dto.request.PersonnelRequest;
import com.antelif.library.domain.dto.response.PersonnelResponse;
import com.antelif.library.infrastructure.entity.PersonnelEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Converter for personnel objects.
 */
@Component
@RequiredArgsConstructor
public class PersonnelConverter
    implements Converter<PersonnelRequest, PersonnelEntity, PersonnelResponse> {

  private final ModelMapper modelMapper;

  @Override
  public PersonnelEntity convertFromRequestToEntity(PersonnelRequest personnelRequest) {
    return modelMapper.map(personnelRequest, PersonnelEntity.class);
  }

  @Override
  public PersonnelResponse convertFromEntityToResponse(PersonnelEntity personnelEntity) {
    return modelMapper.map(personnelEntity, PersonnelResponse.class);
  }
}
