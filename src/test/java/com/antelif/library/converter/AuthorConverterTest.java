package com.antelif.library.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.antelif.library.domain.converter.AuthorConverter;
import com.antelif.library.domain.dto.request.AuthorRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;
import com.antelif.library.infrastructure.entity.AuthorEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuthorConverterTest {

  @Autowired private AuthorConverter authorConverter;

  private final AuthorRequest expectedRequest = new AuthorRequest();
  private final AuthorResponse authorResponse = new AuthorResponse();
  private final AuthorEntity expectedEntity = new AuthorEntity();

  private final String name = "Sarah";
  private final String surname = "Maas";
  private final String middleName = "J.";

  @BeforeEach
  public void setUp() {
    expectedRequest.setName(name);
    expectedRequest.setSurname(surname);
    expectedRequest.setMiddleName(middleName);

    authorResponse.setName(name);
    authorResponse.setSurname(surname);
    authorResponse.setMiddleName(middleName);

    expectedEntity.setName(name);
    expectedEntity.setSurname(surname);
    expectedEntity.setMiddleName(middleName);
  }

  @Test
  @DisplayName("Convert Author - Request to Entity")
  void convertFromDtoToDomain_isSuccessful() {
    var actualEntity = authorConverter.convertFromRequestToEntity(expectedRequest);

    assertEquals(expectedEntity, actualEntity);
  }

  @Test
  @DisplayName("Convert Author - Entity to Response")
  void convertFromDomainToEntity_isSuccessful() {
    var actualOutput = authorConverter.convertFromEntityToResponse(expectedEntity);

    assertEquals(authorResponse, actualOutput);
  }
}
