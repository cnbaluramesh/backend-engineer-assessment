package com.midas.app.constant;

public enum ProviderType {
  STRIPE("STRIPE");
  private String value;

  ProviderType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
