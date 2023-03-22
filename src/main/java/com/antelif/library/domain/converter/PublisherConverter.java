package com.antelif.library.domain.converter;

import com.antelif.library.domain.dto.request.PublisherRequest;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.antelif.library.infrastructure.entity.PublisherEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Converter for publisher objects.
 */
@Component
@RequiredArgsConstructor
public class PublisherConverter
    implements Converter<PublisherRequest, PublisherEntity, PublisherResponse> {

  private final ModelMapper modelMapper;

  @Override
  public PublisherEntity convertFromRequestToEntity(PublisherRequest publisherRequest) {
    return modelMapper.map(publisherRequest, PublisherEntity.class);
  }

  @Override
  public PublisherResponse convertFromEntityToResponse(PublisherEntity publisherEntity) {
    return modelMapper.map(publisherEntity, PublisherResponse.class);
  }
}
