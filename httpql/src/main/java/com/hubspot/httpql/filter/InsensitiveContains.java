package com.hubspot.httpql.filter;

import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import com.hubspot.httpql.ConditionProvider;
import com.hubspot.httpql.Filter;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Param;

/**
 * @deprecated Use #{@link com.hubspot.httpql.core.filter.InsensitiveContains}
 */
@Deprecated
public class InsensitiveContains extends FilterBase implements Filter {
  private static final Escaper ESCAPER = Escapers.builder()
      .addEscape('\\', "\\\\")
      .addEscape('%', "!%")
      .addEscape('_', "!_")
      .addEscape('!', "!!")
      .build();

  @Override
  public String[] names() {
    return new String[] {
        "icontains", "ilike"
    };
  }

  @Override
  public <T> ConditionProvider<T> getConditionProvider(final Field<T> field) {
    return new ConditionProvider<T>(field) {

      @Override
      public Condition getCondition(Param<T> value) {
        String originalValue = (String) value.getValue();
        String escapedValue = ESCAPER.escape(originalValue);
        return field.likeIgnoreCase('%' + escapedValue + '%', '!');
      }

    };
  }

}
