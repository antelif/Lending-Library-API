package com.antelif.library.application.controller.query;

import static com.antelif.library.domain.common.ControllerTags.PUBLISHER_CONTROLLER;
import static com.antelif.library.domain.common.Endpoints.PUBLISHERS_ENDPOINT;

import com.antelif.library.domain.converter.PublisherConverter;
import com.antelif.library.domain.dto.response.PublisherResponse;
import com.antelif.library.domain.service.PublisherService;
import io.swagger.annotations.Api;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Publisher query controller. */
@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = PUBLISHER_CONTROLLER)
@RequestMapping(PUBLISHERS_ENDPOINT)
public class PublisherQueryController {

  private final PublisherService publisherService;
  private final PublisherConverter publisherConverter;

  /**
   * Gets a list of all publishers persisted in database.
   *
   * @return a list of publisher response object.
   */
  @GetMapping
  public ResponseEntity<List<PublisherResponse>> getPublishers() {
    return ResponseEntity.ok(publisherService.getAllPublishers());
  }

  /**
   * Retrieve a publisher from the database by provided id.
   *
   * @param id of the publisher to retrieve.
   * @return a publisher response object.
   */
  @GetMapping("/{id}")
  public ResponseEntity<PublisherResponse> getPublisherById(@PathVariable("id") Long id) {

    var publisherEntity = publisherService.getPublisherById(id);
    return ResponseEntity.ok(publisherConverter.convertFromEntityToResponse(publisherEntity));
  }
}
