package com.antelif.library.converter;

import static com.antelif.library.factory.AuthorFactory.createAuthorRequest;
import static com.antelif.library.factory.AuthorFactory.createAuthorResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.antelif.library.domain.converter.AuthorConverter;
import com.antelif.library.domain.dto.request.AuthorRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.infrastructure.entity.AuthorEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Author converter")
class AuthorConverterTest {

  @Autowired private AuthorConverter authorConverter;
  @Autowired private ModelMapper modelMapper;

  private AuthorRequest expectedRequest;
  private AuthorResponse authorResponse;
  private AuthorEntity expectedEntity;

  @BeforeEach
  void setUp() {
    expectedRequest = createAuthorRequest(1);
    authorResponse = createAuthorResponse(1);
    expectedEntity = modelMapper.map(authorResponse, AuthorEntity.class);
  }

  @Test
  @DisplayName("Convert Author: Request to Entity")
  void convertFromDtoToDomain_isSuccessful() {
    AuthorEntity actualEntity = authorConverter.convertFromRequestToEntity(expectedRequest);

    assertEquals(expectedEntity.getName(), actualEntity.getName());
    assertEquals(expectedEntity.getMiddleName(), actualEntity.getMiddleName());
    assertEquals(expectedEntity.getSurname(), actualEntity.getSurname());
  }

  @Test
  @DisplayName("Convert Author: Entity to Response")
  void convertFromDomainToEntity_isSuccessful() {
    AuthorResponse actualOutput = authorConverter.convertFromEntityToResponse(expectedEntity);

    assertEquals(authorResponse.getName(), actualOutput.getName());
    assertEquals(authorResponse.getMiddleName(), actualOutput.getMiddleName());
    assertEquals(authorResponse.getSurname(), actualOutput.getSurname());
  }
}
