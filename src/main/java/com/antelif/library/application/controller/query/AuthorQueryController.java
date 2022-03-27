package com.antelif.library.application.controller.query;

import com.antelif.library.domain.dto.AuthorDto;
import com.antelif.library.domain.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Author query controller. */
@RestController
@Slf4j
@RequestMapping(value = "/authors")
@RequiredArgsConstructor
public class AuthorQueryController {
  private final AuthorService authorService;

  /**
   * Get author by name and surname endpoint.
   *
   * @param name the name of the author,
   * @param surname the surname of the author.
   * @return an author DTO for the provided name and surname.
   */
  @GetMapping
  public AuthorDto getAuthorByNameAndSurname(
      @RequestParam(value = "name") String name, @RequestParam(value = "surname") String surname) {
    log.info("Requested author: {} {}", name, surname);
    return authorService.getAuthorByNameAndSurname(name, surname);
  }

  /**
   * Get autor by id endpoint.
   *
   * @param id the id of the author to retrieve.
   * @return an author DTO for the provided id.
   */
  @GetMapping(value = "/{id}")
  public AuthorDto getAuthorById(@PathVariable(value = "id") Long id) {
    log.info("Received request to fetch author with id {}", id);
    return authorService.getAuthorById(id);
  }
}
