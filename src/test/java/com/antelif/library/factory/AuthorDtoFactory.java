package com.antelif.library.factory;

import com.antelif.library.domain.dto.request.AuthorRequest;

public class AuthorDtoFactory {

  public static AuthorRequest createAuthorRequest(int index) {
    var author = new AuthorRequest();
    author.setName("name" + index);
    author.setMiddleName("middleName" + index);
    author.setSurname("surname" + index);
    return author;
  }

  public static AuthorRequest createAuthorRequestNoMiddleName(int index) {
    var author = new AuthorRequest();
    author.setName("name" + index);
    author.setSurname("surname" + index);
    return author;
  }
}
