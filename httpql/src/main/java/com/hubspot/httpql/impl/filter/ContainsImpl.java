package com.hubspot.httpql.impl.filter;

import com.google.common.collect.ImmutableSet;
import com.hubspot.httpql.ConditionProvider;
import com.hubspot.httpql.core.filter.Contains;
import com.hubspot.httpql.core.filter.Filter;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Param;

import java.util.Set;

public class ContainsImpl extends FilterBase implements FilterImpl {

  @Override
  public <T> ConditionProvider<T> getConditionProvider(final Field<T> field) {
    return new ConditionProvider<>(field) {

      @Override
      public Condition getCondition(Param<T> value) {
        return field.contains(value);
      }

    };
  }

  @Override
  public Set<Class<? extends Filter>> getAnnotationClasses() {
    return ImmutableSet.of(Contains.class, com.hubspot.httpql.filter.Contains.class);
  }

}
