package com.hubspot.httpql.core.filter;

public class JsonGreaterThanOrEqual implements Filter {

  @Override
  public String[] names() {
    return new String[] { "json_gte" };
  }
}