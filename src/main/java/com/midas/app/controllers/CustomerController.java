package com.midas.app.controllers;

import com.midas.app.constant.ProviderType;
import com.midas.app.mappers.Mapper;
import com.midas.app.models.Customer;
import com.midas.app.services.CustomerService;
import com.midas.generated.api.CustomersApi;
import com.midas.generated.model.CreateCustomerDto;
import com.midas.generated.model.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomersApi {
  @Autowired private final CustomerService customerService;
  private final Logger logger = LoggerFactory.getLogger(CustomerController.class);

  @Override
  @RequestMapping(value = "/createCustomers", method = RequestMethod.POST)
  public ResponseEntity<CustomerDto> createCustomer(CreateCustomerDto createCustomerDto) {
    logger.info("Sign up user with email: {}", createCustomerDto.getEmail());

    java.util.Date date = new java.util.Date();
    // setting milliseconds here as provider id.

    var customer =
        customerService.createCustomer(
            Customer.builder()
                .firstName(createCustomerDto.getFirstName())
                .lastName(createCustomerDto.getLastName())
                .email(createCustomerDto.getEmail())
                .providerType(ProviderType.STRIPE)
                .build());

    return new ResponseEntity<>(Mapper.toCustomerDto(customer), HttpStatus.CREATED);
  }
}
