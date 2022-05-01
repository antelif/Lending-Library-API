package com.antelif.library.application.controller.command;

import static com.antelif.library.domain.common.Constants.CREATED;

import com.antelif.library.domain.dto.AuthorDto;
import com.antelif.library.domain.service.AuthorService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Author command controller. */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/library/authors")
public class AuthorCommandController {

  private final AuthorService authorService;

  /**
   * Add a new author endpoint.
   *
   * @param authorDto the DTO to get information to create the new author.
   * @return an author DTO.
   */
  @PostMapping
  public Map<String, Long> addAuthor(@RequestBody AuthorDto authorDto) {
    log.info("Received request to add author {}", authorDto);
    return Map.of(CREATED, authorService.addAuthor(authorDto));
  }
}
