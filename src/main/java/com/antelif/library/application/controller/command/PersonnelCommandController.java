package com.antelif.library.application.controller.command;

import static com.antelif.library.domain.common.Constants.CREATED;

import com.antelif.library.domain.dto.request.PersonnelRequest;
import com.antelif.library.domain.dto.response.PersonnelResponse;
import com.antelif.library.domain.service.PersonnelService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Personnel command controller. */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/library/personnel")
public class PersonnelCommandController {

  private final PersonnelService personnelService;

  /**
   * Add new personnel endpoint.
   *
   * @param personnelRequest the DTO to get information to create the new personnel.
   * @return a personnel response DTO.
   */
  @PostMapping
  public ResponseEntity<Map<String, PersonnelResponse>> addPersonnel(
      @RequestBody PersonnelRequest personnelRequest) {
    log.info("Received request to add personnel {}", personnelRequest);
    return ResponseEntity.ok(Map.of(CREATED, personnelService.addPersonnel(personnelRequest)));
  }
}