package com.antelif.library.factory;

import com.antelif.library.domain.dto.request.CustomerRequest;

public class CustomerFactory {

  public static CustomerRequest createCustomerRequest(int index) {
    var customer = new CustomerRequest();
    customer.setName("name" + index);
    customer.setSurname("surname" + index);
    customer.setPhoneNo("phoneNo" + index);
    customer.setEmail("email" + index);
    customer.setFee(0);
    return customer;
  }
}
