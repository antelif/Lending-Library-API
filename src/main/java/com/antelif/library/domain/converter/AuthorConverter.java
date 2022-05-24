package com.antelif.library.domain.converter;

import com.antelif.library.domain.dto.request.AuthorRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.infrastructure.entity.AuthorEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/** Converter for author objects. */
@Component
@RequiredArgsConstructor
public class AuthorConverter implements Converter<AuthorRequest, AuthorEntity, AuthorResponse> {

  private final ModelMapper modelMapper;

  @Override
  public AuthorEntity convertFromRequestToEntity(AuthorRequest authorRequest) {
    return modelMapper.map(authorRequest, AuthorEntity.class);
  }

  @Override
  public AuthorResponse convertFromEntityToResponse(AuthorEntity authorEntity) {
    return modelMapper.map(authorEntity, AuthorResponse.class);
  }
}
