package com.hubspot.httpql.core.filter;

public class JsonRange implements Filter {

  @Override
  public String[] names() {
    return new String[] { "json_range" };
  }
}
