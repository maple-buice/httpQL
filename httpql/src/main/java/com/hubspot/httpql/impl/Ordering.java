package com.hubspot.httpql.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Splitter;
import com.hubspot.httpql.core.OrderingIF;
import org.jooq.SortOrder;

import java.util.List;
import java.util.Objects;

/**
 * @deprecated Use {@link com.hubspot.httpql.core.Ordering}
 */
@Deprecated
public class Ordering implements OrderingIF {
  private static final Splitter FIELD_SPLITTER = Splitter.on(',');

  private final String queryName;
  private final String fieldName;
  private SortOrder order;

  public Ordering(String fieldName, SortOrder order) {
    List<String> fields = FIELD_SPLITTER.splitToList(fieldName);

    this.fieldName = fields.get(0);
    this.queryName = fields.size() > 1 ? fields.get(1) : fields.get(0);
    this.order = order;
  }

  public Ordering(String fieldName, String queryName, SortOrder order) {
    this.fieldName = fieldName;
    this.order = order;
    this.queryName = queryName;
  }

  @JsonCreator
  public static Ordering fromString(String ordering) {
    SortOrder order;
    if (ordering.startsWith("-")) {
      ordering = ordering.substring(1);
      order = SortOrder.DESC;
    } else {
      order = SortOrder.ASC;
    }
    return new Ordering(ordering, order);
  }

  @JsonValue
  public String jsonValue() {
    StringBuilder json = new StringBuilder();

    if (order == SortOrder.DESC) {
      json.append('-');
    }

    json.append(fieldName);

    if (!Objects.equals(fieldName, queryName)) {
      json.append(',').append(queryName);
    }

    return json.toString();
  }

  public String getQueryName() {
    return queryName;
  }

  public String getFieldName() {
    return fieldName;
  }

  @Override
  public int getSortOrdinal() {
    return getOrder().ordinal();
  }

  /**
   * @return either "asc" or "desc"
   */
  @Override
  public String getOrderString() {
    return getOrder().toSQL();
  }

  public SortOrder getOrder() {
    return order;
  }

  public void setOrder(SortOrder order) {
    this.order = order;
  }

  @Override
  public String toString() {
    return String.format("Ordering<%s %s>", queryName, order);
  }

  @Override
  public int hashCode() {
    return Objects.hash(queryName, order);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Ordering)) {
      return false;
    }

    Ordering o = (Ordering) obj;

    return Objects.equals(queryName, o.queryName)
        && Objects.equals(order, o.order);
  }

}
