package com.midas.app.workflows;

import static com.midas.app.workflows.CreateCustomerWorkflow.QUEUE_NAME;

import com.midas.app.activities.CustomerActivity;
import com.midas.app.models.Customer;
import com.stripe.exception.StripeException;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@RequiredArgsConstructor
@WorkflowImpl(taskQueues = "create-customer-workflow")
public class CreateCustomerWorkflowImpl implements CreateCustomerWorkflow {
  private final Logger logger = Workflow.getLogger(CreateCustomerWorkflowImpl.class);

  private final ActivityOptions customerActivityOptions =
      ActivityOptions.newBuilder()
          .setStartToCloseTimeout(Duration.ofMinutes(2))
          .setTaskQueue(QUEUE_NAME)
          .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(5).build())
          .build();
  ;
  private final CustomerActivity customerActivity =
      Workflow.newActivityStub(CustomerActivity.class, customerActivityOptions);

  @Override
  public Customer createCustomer(Customer customer) {
    try {
      return customerActivity.saveCustomer(customer);
    } catch (StripeException e) {
      throw new RuntimeException(e);
    }
  }
}
