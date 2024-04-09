package com.midas.app.services;

import com.midas.app.models.Customer;
import com.midas.app.repositories.CustomerRepository;
import com.midas.app.workflows.CreateCustomerWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final Logger logger = Workflow.getLogger(CustomerServiceImpl.class);

  @Autowired private final WorkflowClient workflowClient;
  @Autowired private final CustomerRepository customerRepository;

  @Override
  public Customer createCustomer(Customer customer) {
    var options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(CreateCustomerWorkflow.QUEUE_NAME)
            .setWorkflowId(customer.getEmail())
            .build();

    logger.info("initiating workflow to sign up user with email: {}", customer.getEmail());

    var workflow = workflowClient.newWorkflowStub(CreateCustomerWorkflow.class, options);
    Customer customer1 = workflow.createCustomer(customer);
    customerRepository.save(customer1);
    return customer1;
  }
}
