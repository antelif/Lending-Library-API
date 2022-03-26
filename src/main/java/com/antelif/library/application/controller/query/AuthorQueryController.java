package com.antelif.library.application.controller.query;

import com.antelif.library.domain.dto.AuthorDto;
import com.antelif.library.domain.service.AuthorService;
import javax.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/authors")
@RequiredArgsConstructor
public class AuthorQueryController {
  private final AuthorService authorService;

  @GetMapping
  public AuthorDto getAuthorByNameAndSurname(
      @RequestParam(value = "name") String name, @RequestParam(value = "surname") String surname) {
    log.info("Requested author: {} {}", name, surname);
    return authorService.getAuthorByNameAndSurname(name, surname);
  }

  @GetMapping
  public AuthorDto getAuthorById(@PathParam(value = "id") Long id) {
    log.info("Received request to fetch author with id {}", id);
    return authorService.getAuthorById(id);
  }
}
