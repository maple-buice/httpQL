package com.hubspot.httpql.lib.filter;

import com.hubspot.httpql.core.filter.FilterIF;
import com.hubspot.httpql.core.filter.Null;
import com.hubspot.httpql.lib.ConditionProvider;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Param;

public class NullImpl extends FilterBase implements FilterImpl {
  @Override
  public String[] names() {
    return new String[] {
        "is_null"
    };
  }

  @Override
  public <T> ConditionProvider<T> getConditionProvider(Field<T> field) {
    return new ConditionProvider<T>(field) {

      @Override
      public Condition getCondition(Param<T> value) {
        return field.isNull();
      }
    };
  }

  @Override
  public Class<? extends FilterIF> getAnnotationClass() {
    return Null.class;
  }
}
