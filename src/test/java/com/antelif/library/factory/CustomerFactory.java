package com.antelif.library.factory;

import com.antelif.library.domain.dto.request.CustomerRequest;
import com.antelif.library.domain.dto.response.CustomerResponse;

public class CustomerFactory {

  public static CustomerRequest createCustomerRequest(int index) {
    CustomerRequest customer = new CustomerRequest();
    customer.setName("name" + index);
    customer.setSurname("surname" + index);
    customer.setPhoneNo("123456789" + index);
    customer.setEmail("email@" + index + ".com");
    return customer;
  }

  public static CustomerResponse createCustomerResponse(int index) {
    CustomerResponse customer = new CustomerResponse();
    customer.setName("name" + index);
    customer.setSurname("surname" + index);
    customer.setPhoneNo("123456789" + index);
    customer.setEmail("email@" + index + ".com");
    customer.setFee(0);
    return customer;
  }
}
