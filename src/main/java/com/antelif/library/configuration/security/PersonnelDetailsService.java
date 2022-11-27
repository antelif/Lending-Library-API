package com.antelif.library.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Contains functionality to authenticate a database personnel. */
@Service
@RequiredArgsConstructor
public class PersonnelDetailsService implements UserDetailsService {

  private final UserRetrievalService userRetrievalService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var authorizedUser = userRetrievalService.retrievePersonnelByUserName(username);
    return new PersonnelUserDetails(authorizedUser);
  }
}
