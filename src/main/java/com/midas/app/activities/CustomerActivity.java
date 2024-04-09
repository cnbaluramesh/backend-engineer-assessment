package com.midas.app.activities;

import com.midas.app.models.Customer;
import com.stripe.exception.StripeException;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface CustomerActivity {

  /**
   * saveCustomer sign up the user and store in the data store.
   *
   * @param customer is the user to signup
   * @return Customer
   */
  @ActivityMethod
  Customer saveCustomer(Customer customer) throws StripeException;
}
