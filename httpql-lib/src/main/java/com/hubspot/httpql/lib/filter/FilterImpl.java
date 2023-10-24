package com.hubspot.httpql.lib.filter;

import com.hubspot.httpql.core.filter.Filter;
import com.hubspot.httpql.lib.ConditionProvider;
import org.jooq.Field;

/**
 * Effectively the operator in a WHERE clause condition.
 *
 * @author tdavis
 */
public interface FilterImpl {
  /**
   * List of names the operator goes by in queries; the {@code gt} in {@code foo_gt=1}
   */
  String[] names();

  <T> ConditionProvider<T> getConditionProvider(Field<T> field);

  Class<? extends Filter> getAnnotationClass();

}