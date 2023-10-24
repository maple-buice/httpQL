package com.hubspot.httpql.lib.filter;

import com.hubspot.httpql.core.filter.Filter;
import com.hubspot.httpql.core.filter.NotLike;
import com.hubspot.httpql.lib.ConditionProvider;
import com.hubspot.httpql.lib.MultiParamConditionProvider;
import java.util.Collection;
import java.util.Iterator;
import org.jooq.Condition;
import org.jooq.Field;

public class NotLikeImpl extends FilterBase implements FilterImpl {

  @Override
  public String[] names() {
    return new String[] {
        "nlike", "not_like"
    };
  }

  @Override
  public <T> ConditionProvider<T> getConditionProvider(final Field<T> field) {
    return new MultiParamConditionProvider<T>(field) {

      @Override
      public Condition getCondition(Collection<T> values) {
        Iterator<T> iter = values.iterator();
        Condition notLikeCondition = field.notLike((String) iter.next(), '!');
        while (iter.hasNext()) {
          notLikeCondition = notLikeCondition.and(field.notLike((String) iter.next(), '!'));
        }
        return notLikeCondition;
      }

    };
  }

  @Override
  public Class<? extends Filter> getAnnotationClass() {
    return NotLike.class;
  }

}