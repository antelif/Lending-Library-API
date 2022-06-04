package com.antelif.library.factory;

import com.antelif.library.domain.dto.request.PersonnelRequest;

public class PersonnelFactory {

  public static PersonnelRequest createPersonnelRequest(int index) {
    var personnel = new PersonnelRequest();
    personnel.setUsername("username" + index);
    personnel.setPassword("password" + index);
    return personnel;
  }
}
