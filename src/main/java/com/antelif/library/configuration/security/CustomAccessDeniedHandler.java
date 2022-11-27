package com.antelif.library.configuration.security;

import static com.antelif.library.application.error.GenericError.AUTHORIZATION_FAILED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import com.antelif.library.application.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/** Handles 403 requests. */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void handle(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AccessDeniedException exception)
      throws IOException {

    var errorResponse = new ErrorResponse(AUTHORIZATION_FAILED, FORBIDDEN.value());

    httpServletResponse.getOutputStream().println(objectMapper.writeValueAsString(errorResponse));
  }
}
