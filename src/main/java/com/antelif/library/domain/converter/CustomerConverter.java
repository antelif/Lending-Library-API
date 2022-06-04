package com.antelif.library.domain.converter;

import com.antelif.library.domain.dto.request.CustomerRequest;
import com.antelif.library.domain.dto.response.CustomerResponse;
import com.antelif.library.infrastructure.entity.CustomerEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerConverter
    implements Converter<CustomerRequest, CustomerEntity, CustomerResponse> {

  private final ModelMapper mapper;

  @Override
  public CustomerEntity convertFromRequestToEntity(CustomerRequest customerRequest) {
    return mapper.map(customerRequest, CustomerEntity.class);
  }

  @Override
  public CustomerResponse convertFromEntityToResponse(CustomerEntity customerEntity) {
    return mapper.map(customerEntity, CustomerResponse.class);
  }
}
