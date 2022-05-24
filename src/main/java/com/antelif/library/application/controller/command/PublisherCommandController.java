package com.antelif.library.application.controller.command;

import static com.antelif.library.domain.common.Constants.CREATED;

import com.antelif.library.domain.dto.request.PublisherRequest;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.antelif.library.domain.service.PublisherService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Publisher command controller. */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/library/publishers")
public class PublisherCommandController {

  private final PublisherService publisherService;

  /**
   * Add a new publisher endpoint.
   *
   * @param publisherRequest the DTO to get information to create the new author.
   * @return an author response DTO.
   */
  @PostMapping
  public ResponseEntity<Map<String, PublisherResponse>> addPublisher(
      @RequestBody PublisherRequest publisherRequest) {
    log.info("Received request to add author {}", publisherRequest);
    return ResponseEntity.ok(Map.of(CREATED, publisherService.addPublisher(publisherRequest)));
  }
}
