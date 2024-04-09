package com.midas.app.mappers;

import com.midas.app.models.Account;
import com.midas.app.models.Customer;
import com.midas.generated.model.AccountDto;
import com.midas.generated.model.CustomerDto;
import lombok.NonNull;

public class Mapper {
  // Prevent instantiation
  private Mapper() {}

  /**
   * toAccountDto maps an account to an account dto.
   *
   * @param account is the account to be mapped
   * @return AccountDto
   */
  public static AccountDto toAccountDto(@NonNull Account account) {
    var accountDto = new AccountDto();

    accountDto
        .id(account.getId())
        .firstName(account.getFirstName())
        .lastName(account.getLastName())
        .email(account.getEmail())
        .createdAt(account.getCreatedAt())
        .updatedAt(account.getUpdatedAt())
        .providerId(account.getProviderId())
        .providerType(
            AccountDto.ProviderTypeEnum.fromValue(String.valueOf(account.getProviderType())));

    return accountDto;
  }

  public static CustomerDto toCustomerDto(@NonNull Customer customer) {
    var customerDto = new CustomerDto();

    customerDto
        .id(customer.getId())
        .firstName(customer.getFirstName())
        .lastName(customer.getLastName())
        .email(customer.getEmail())
        .providerId(customer.getProviderId())
        .providerType(CustomerDto.ProviderTypeEnum.valueOf(customer.getProviderType().getValue()))
        .createdAt(customer.getCreatedAt())
        .updatedAt(customer.getUpdatedAt());

    return customerDto;
  }
}
