package com.antelif.library.factory;

import com.antelif.library.domain.dto.request.PersonnelRequest;
import com.antelif.library.domain.dto.response.PersonnelResponse;

public class PersonnelFactory {

  public static PersonnelRequest createPersonnelRequest(int index) {
    PersonnelRequest personnel = new PersonnelRequest();
    personnel.setUsername("username" + index);
    personnel.setPassword("password" + index);
    return personnel;
  }

  public static PersonnelResponse createPersonnelResponse(int index) {
    PersonnelResponse personnel = new PersonnelResponse();
    personnel.setUsername("username" + index);
    return personnel;
  }
}
