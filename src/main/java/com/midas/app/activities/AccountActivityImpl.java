package com.midas.app.activities;

import com.midas.app.models.Account;
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
@ActivityImpl(taskQueues = "create-account-workflow")
public class AccountActivityImpl implements AccountActivity {

  @Autowired private Environment environment;
  private final Logger logger = LoggerFactory.getLogger(AccountActivityImpl.class);

  @Override
  public Account saveAccount(Account account) throws StripeException {
    String apikey = environment.getProperty("stripe.api-key");
    Map<String, Object> accountParams = new HashMap<>();
    accountParams.put("email", account.getEmail());
    accountParams.put("type", "standard");
    accountParams.put("country", "IN");
    accountParams.put("capabilities[card_payments][requested]", false);
    accountParams.put("capabilities[transfers][requested]", false);
    RequestOptions requestOptions = RequestOptions.builder().setApiKey(apikey).build();

    com.stripe.model.Account sAccount =
        com.stripe.model.Account.create(accountParams, requestOptions);
    account.setProviderId(sAccount.getId());
    return account;
  }

  @Override
  public Account createPaymentAccount(Account account) {
    return null;
  }

  @Override
  public String updateAccount(Account account) throws StripeException {

    logger.info("received account: {}", account.toString());
    String apikey = environment.getProperty("stripe.api-key");
    RequestOptions requestOptions = RequestOptions.builder().setApiKey(apikey).build();

    com.stripe.model.Account sAccount =
        com.stripe.model.Account.retrieve(account.getProviderId(), requestOptions);

    if (sAccount != null) {
      Map<String, Object> accountParams = new HashMap<>();
      accountParams.put("email", account.getEmail());
      accountParams.put("type", "standard");
      accountParams.put("country", "IN");
      accountParams.put("capabilities[card_payments][requested]", false);
      accountParams.put("capabilities[transfers][requested]", false);
      Map<String, Object> updateParams = new HashMap<String, Object>();
      updateParams.put("email", sAccount.getEmail());
      sAccount.update(updateParams, requestOptions);
      return sAccount.toString();
    } else {
      return "no such customer";
    }
  }
}
