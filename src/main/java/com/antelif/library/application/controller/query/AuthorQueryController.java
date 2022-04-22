package com.antelif.library.application.controller.query;

import com.antelif.library.domain.dto.AuthorDto;
import com.antelif.library.domain.service.AuthorService;
import java.util.List;
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
@RequestMapping(value = "/library/authors")
@RequiredArgsConstructor
public class AuthorQueryController {
  private final AuthorService authorService;

  /**
   * Get authors by surname endpoint.
   *
   * @param surname the surname of the author
   * @return a list of author DTO for the provided name and surname.
   */
  @GetMapping
  public List<AuthorDto> getAuthorBySurname(@RequestParam(value = "surname") String surname) {
    log.info("Requested author: {} ", surname);
    return authorService.getAuthorsBySurname(surname);
  }

  /**
   * Get author by id endpoint.
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
