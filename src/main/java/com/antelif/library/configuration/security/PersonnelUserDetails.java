package com.antelif.library.configuration.security;

import com.antelif.library.infrastructure.entity.PersonnelEntity;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Getter
public class PersonnelUserDetails implements UserDetails {

  private final String username;
  private final String password;
  private final String role;

  public PersonnelUserDetails(PersonnelEntity personnel) {

    this.username = personnel.getUsername();
    this.password = personnel.getPassword();
    this.role = personnel.getRole();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + this.role));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
