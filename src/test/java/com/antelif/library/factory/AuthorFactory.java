package com.antelif.library.factory;

import com.antelif.library.domain.dto.request.AuthorRequest;
import com.antelif.library.domain.dto.response.AuthorResponse;

public class AuthorFactory {

  public static AuthorRequest createAuthorRequest(int index) {
    AuthorRequest author = new AuthorRequest();
    author.setName("name" + index);
    author.setMiddleName("middleName" + index);
    author.setSurname("surname" + index);
    return author;
  }

  public static AuthorRequest createAuthorRequestNoMiddleName(int index) {
    AuthorRequest author = new AuthorRequest();
    author.setName("name" + index);
    author.setSurname("surname" + index);
    return author;
  }

  public static AuthorResponse createAuthorResponse(int index) {
    AuthorResponse author = new AuthorResponse();
    author.setName("name" + index);
    author.setMiddleName("middleName" + index);
    author.setSurname("surname" + index);
    return author;
  }
}
