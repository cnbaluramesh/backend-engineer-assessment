package com.midas.app.workflows;

import com.midas.app.models.Account;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface UpdateAccountWorkflow {

  String QUEUE_NAME = "update-account-workflow";

  /**
   * updateAccount updates the account for the mentioned id in the system or provider.
   *
   * @param account is the account to be updated.
   * @return Account
   */
  @WorkflowMethod
  String updateAccount(Account account);
}
