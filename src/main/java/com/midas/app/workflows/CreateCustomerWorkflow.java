package com.midas.app.workflows;

import com.midas.app.models.Customer;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CreateCustomerWorkflow {
  String QUEUE_NAME = "create-customer-workflow";

  /**
   * createCustomer creates a new customer in the system or provider.
   *
   * @param customer is the details of the account to be created.
   * @return customer
   */
  @WorkflowMethod
  Customer createCustomer(Customer customer);
}
