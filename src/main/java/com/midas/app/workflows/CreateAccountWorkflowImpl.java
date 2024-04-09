package com.midas.app.workflows;

import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import com.stripe.exception.StripeException;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@RequiredArgsConstructor
@WorkflowImpl(taskQueues = "create-account-workflow")
public class CreateAccountWorkflowImpl implements CreateAccountWorkflow {

  private final Logger logger = Workflow.getLogger(CreateAccountWorkflowImpl.class);

  private final ActivityOptions accountActivityOptions =
      ActivityOptions.newBuilder()
          .setStartToCloseTimeout(Duration.ofMinutes(2))
          .setTaskQueue(QUEUE_NAME)
          .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(5).build())
          .build();
  ;
  private final AccountActivity accountActivity =
      Workflow.newActivityStub(AccountActivity.class, accountActivityOptions);

  @Override
  public Account createAccount(Account details) {
    try {
      return accountActivity.saveAccount(details);
    } catch (StripeException e) {
      throw new RuntimeException(e);
    }
  }
}
