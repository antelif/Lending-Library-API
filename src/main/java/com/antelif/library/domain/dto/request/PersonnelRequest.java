package com.antelif.library.domain.dto.request;

import static com.antelif.library.domain.type.Role.ADMIN;

import com.antelif.library.configuration.AppProperties.RootUser;
import com.antelif.library.domain.type.Role;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Personnel Request DTO used as request body in HTTP requests.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PersonnelRequest {

  @NotBlank(message = "Personnel username should not be blank.")
  @Size(max = 20, message = "Personnel username should not exceed 20 characters.")
  private String username;

  @NotBlank(message = "Personnel password should not be blank.")
  private String password;

  private Role role = ADMIN;

  /**
   * Constructor to build the root user using application properties configuration.
   */
  public PersonnelRequest(RootUser rootUser) {
    this.username = rootUser.getUsername();
    this.password = rootUser.getPassword();
    this.role = ADMIN;
  }
}
