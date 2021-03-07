package com.antelif.lendinglibrary.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Customer {

  String name;
  String phoneNo;
  String email;
  int id;
  double fee;


}
