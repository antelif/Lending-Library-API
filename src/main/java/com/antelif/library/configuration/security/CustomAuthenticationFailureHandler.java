package com.antelif.library.configuration.security;

import static com.antelif.library.application.error.GenericError.AUTHENTICATION_FAILED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.antelif.library.application.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/** Handles 401 requests. */
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  public CustomAuthenticationFailureHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      org.springframework.security.core.AuthenticationException e)
      throws IOException, ServletException, InternalAuthenticationServiceException {

    ErrorResponse errorResponse = new ErrorResponse(AUTHENTICATION_FAILED, UNAUTHORIZED.value());

    httpServletResponse.getOutputStream().println(objectMapper.writeValueAsString(errorResponse));
  }
}
