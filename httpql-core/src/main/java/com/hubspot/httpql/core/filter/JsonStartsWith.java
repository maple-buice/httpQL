package com.hubspot.httpql.core.filter;

public class JsonStartsWith implements Filter {

  @Override
  public String[] names() {
    return new String[] { "json_startswith" };
  }
}
