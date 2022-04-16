package com.antelif.library.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.antelif.library.domain.Author;
import com.antelif.library.domain.converter.AuthorConverter;
import com.antelif.library.domain.dto.AuthorDto;
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

  private final AuthorDto expectedDto = new AuthorDto();
  private final Author expectedDomain = new Author();
  private final AuthorEntity expectedEntity = new AuthorEntity();

  private final String name = "Sarah";
  private final String surname = "Maas";
  private final String middleName = "J.";

  @BeforeEach
  public void setUp() {
    expectedDto.setName(name);
    expectedDto.setSurname(surname);
    expectedDto.setMiddleName(middleName);

    expectedDomain.setName(name);
    expectedDomain.setSurname(surname);
    expectedDomain.setMiddleName(middleName);

    expectedEntity.setName(name);
    expectedEntity.setSurname(surname);
    expectedEntity.setMiddleName(middleName);
  }

  @Test
  @DisplayName("Convert Author - Dto to Domain")
  void convertFromDtoToDomain_isSuccessful() {
    var actualDomain = authorConverter.convertFromDtoToDomain(expectedDto);

    assertEquals(expectedDomain, actualDomain);
  }

  @Test
  @DisplayName("Convert Author - Domain to Entity")
  void convertFromDomainToEntity_isSuccessful() {
    var actualEntity = authorConverter.convertFromDomainToEntity(expectedDomain);

    assertEquals(expectedEntity, actualEntity);
  }

  @Test
  @DisplayName("Convert Author - Entity to Domain")
  void convertFromEntityToDomain_isSuccessful() {
    var actualDomain = authorConverter.convertFromEntityToDomain(expectedEntity);

    assertEquals(expectedDomain, actualDomain);
  }

  @Test
  @DisplayName("Convert Author - Domain to Dto")
  void convertFromDomainToDto_isSuccessful() {
    var actualDto = authorConverter.convertFromDomainToDto(expectedDomain);

    assertEquals(expectedDto, actualDto);
  }
}
