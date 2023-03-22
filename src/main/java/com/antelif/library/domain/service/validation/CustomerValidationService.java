package com.antelif.library.domain.service.validation;

import static com.antelif.library.application.error.GenericError.INVALID_CUSTOMER_UPDATE_VALUE;

import com.antelif.library.domain.exception.UnsuccessfulTransactionException;
import com.antelif.library.infrastructure.entity.CustomerEntity;
import org.springframework.stereotype.Component;

/**
 * Customer validation service.
 */
@Component
public final class CustomerValidationService {

  private CustomerValidationService() {
  }

  /**
   * Contains all validations when updating customer's fee.
   *
   * @param customer the customer whose fee is validated,
   * @param fee      the input repay value.
   */
  public static void validateUpdate(CustomerEntity customer, double fee) {
    validateFeeIsPositive(fee);
    validateFeeIsNotOverPaid(customer, fee);
  }

  /**
   * Throws an exception if the value provided to repay fee is greater than the actual customer's
   * fee.
   */
  private static void validateFeeIsNotOverPaid(CustomerEntity customer, double fee) {
    if (customer.getFee() < fee) {
      throw new UnsuccessfulTransactionException(INVALID_CUSTOMER_UPDATE_VALUE);
    }
  }

  /**
   * Throws an exception if the value provided to repay the fee is a negative number.
   */
  private static void validateFeeIsPositive(double fee) {
    if (0 >= fee) {
      throw new UnsuccessfulTransactionException(INVALID_CUSTOMER_UPDATE_VALUE);
    }
  }
}
