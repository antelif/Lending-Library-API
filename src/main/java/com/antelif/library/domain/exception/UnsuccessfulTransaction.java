package com.antelif.library.domain.exception;

import com.antelif.library.application.error.GenericError;
import java.util.List;

public class UnsuccessfulTransaction extends GenericException {

  public UnsuccessfulTransaction(List<GenericError> errors) {
    super(errors);
  }
}
