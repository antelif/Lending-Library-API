package com.antelif.library.factory;

import com.antelif.library.domain.dto.AuthorDto;

public class AuthorDtoFactory {

  public static AuthorDto createAuthorDto(int index) {
    var author = new AuthorDto();
    author.setName("name" + index);
    author.setMiddleName("middleName" + index);
    author.setSurname("surname" + index);
    return author;
  }

  public static AuthorDto createAuthorDtoNoMiddleName(int index) {
    var author = new AuthorDto();
    author.setName("name" + index);
    author.setSurname("surname" + index);
    return author;
  }
}
