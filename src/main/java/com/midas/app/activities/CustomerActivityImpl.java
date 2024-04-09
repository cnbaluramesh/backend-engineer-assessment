package com.midas.app.activities;

import com.midas.app.models.Customer;
import com.stripe.exception.StripeException;
import com.stripe.net.RequestOptions;
import io.temporal.spring.boot.ActivityImpl;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ActivityImpl(taskQueues = "create-customer-workflow")
public class CustomerActivityImpl implements CustomerActivity {
  @Autowired private Environment environment;
  private final Logger logger = LoggerFactory.getLogger(CustomerActivityImpl.class);

  @Override
  public Customer saveCustomer(Customer customer) throws StripeException {
    String apikey = environment.getProperty("stripe.api-key");
    Map<String, Object> customerParams = new HashMap<>();
    customerParams.put("email", customer.getEmail());
    customerParams.put("name", customer.getFirstName() + " " + customer.getLastName());
    RequestOptions requestOptions = RequestOptions.builder().setApiKey(apikey).build();
    com.stripe.model.Customer sCustomer =
        com.stripe.model.Customer.create(customerParams, requestOptions);
    customer.setProviderId(sCustomer.getId());
    return customer;
  }
}
