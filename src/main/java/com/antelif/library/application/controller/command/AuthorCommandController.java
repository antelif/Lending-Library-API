package com.antelif.library.application.controller.command;

import com.antelif.library.domain.dto.AuthorDto;
import com.antelif.library.domain.service.AuthorService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/authors")
public class AuthorCommandController {

  private final AuthorService authorService;

  @PostMapping
  public AuthorDto addAuthor(@RequestBody AuthorDto authorDto) {
    log.info("Received request to add author {}", authorDto.getName());
    return authorService.addAuthor(authorDto);
  }


}
