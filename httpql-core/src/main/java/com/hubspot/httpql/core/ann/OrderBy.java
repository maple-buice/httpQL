package com.hubspot.httpql.core.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.FIELD, ElementType.METHOD
})
public @interface OrderBy {

  /**
   * Flag to indicate that we're sorting by a generated field (i.e. a non-db-column),
   * so that we never try to qualify the field with the table name in the ORDER BY clause.
   * <p>
   * i.e. isGenerated=true ensures we get:
   * <p>
   *  SELECT LENGTH(name) as `name_length`
   *    FROM `my_table`
   *    ORDER BY `name_length`
   * <p>
   * instead of:
   * <p>
   *  SELECT LENGTH(name) as `name_length`
   *    FROM `my_table`
   *    ORDER BY `my_table`.`name_length`
   * <p>
   * */
  boolean isGenerated() default false;
};
