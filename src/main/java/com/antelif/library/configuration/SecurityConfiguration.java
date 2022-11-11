package com.antelif.library.configuration;

import static com.antelif.library.configuration.Roles.ADMIN;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import com.antelif.library.configuration.security.CustomAccessDeniedHandler;
import com.antelif.library.configuration.security.CustomAuthenticationFailureHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;
  private final ObjectMapper objectMapper;

  // Authentication: who is the user.
  @Override
  protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
      throws Exception {
    authenticationManagerBuilder.userDetailsService(userDetailsService);
  }

  // Authorization: what  the user can access.
  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .httpBasic()
        .and()
        .rememberMe()
        .disable()
        .authorizeRequests()
        .antMatchers(GET, "/**")
        .hasRole(ADMIN)
        .antMatchers(POST, "/**")
        .hasRole(ADMIN)
        .antMatchers(PUT, "/**")
        .hasRole(ADMIN)
        .antMatchers(PATCH, "/**")
        .hasRole(ADMIN)
        .and()
        .csrf()
        .disable()
        .formLogin()
        .failureHandler(authenticationFailureHandler())
        .and()
        .exceptionHandling()
        .accessDeniedHandler(accessDeniedHandler())
        .and()
        .logout();
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new CustomAuthenticationFailureHandler(objectMapper);
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new CustomAccessDeniedHandler(objectMapper);
  }
}
