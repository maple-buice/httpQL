package com.hubspot.httpql.internal;

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.google.common.collect.ImmutableSet;
import com.hubspot.httpql.ConditionProvider;
import com.hubspot.httpql.DefaultMetaUtils;
import com.hubspot.httpql.FieldFactory;
import com.hubspot.httpql.MetaQuerySpec;
import com.hubspot.httpql.MultiParamConditionProvider;
import com.hubspot.httpql.QuerySpec;
import com.hubspot.httpql.ann.desc.JoinDescriptor;
import com.hubspot.httpql.impl.DefaultFieldFactory;
import com.hubspot.httpql.impl.FilterJoinInfo;
import com.hubspot.httpql.impl.filter.FilterImpl;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.util.Collection;

public class BoundFilterEntry<T extends QuerySpec> extends FilterEntry implements FilterEntryConditionCreator<T> {

  private final BeanPropertyDefinition prop;
  private final MetaQuerySpec<T> meta;
  private BeanPropertyDefinition actualField;

  public BoundFilterEntry(FilterImpl filter, String fieldName, String queryName, BeanPropertyDefinition prop, MetaQuerySpec<T> meta) {
    super(filter, fieldName, queryName, meta.getQueryType());
    this.prop = prop;
    this.meta = meta;
  }

  public BoundFilterEntry(FilterImpl filter, BeanPropertyDefinition prop, MetaQuerySpec<T> meta) {
    super(filter, prop.getName(), getBestQueryName(prop), meta.getQueryType());
    this.prop = prop;
    this.meta = meta;
  }

  public BoundFilterEntry(FilterEntry entry, BeanPropertyDefinition prop, MetaQuerySpec<T> meta) {
    this(entry.getFilter(), prop, meta);
  }

  public boolean isMultiValue() {
    return getConditionProvider(new DefaultFieldFactory()) instanceof MultiParamConditionProvider;
  }

  private static String getBestQueryName(BeanPropertyDefinition prop) {
    String as = DefaultMetaUtils.getFilterByAs(prop);
    return as == null ? prop.getName() : as;
  }

  public BeanPropertyDefinition getProperty() {
    return prop;
  }

  public MetaQuerySpec<T> getMeta() {
    return meta;
  }

  public Class<?> getFieldType() {
    return (actualField != null ? actualField : prop).getField().getAnnotated().getType();
  }

  public ConditionProvider<?> getConditionProvider(FieldFactory fieldFactory) {
    Field<?> field;

    FilterJoinInfo join = DefaultMetaUtils.findFilterJoin(prop);
    JoinDescriptor joinDescriptor = DefaultMetaUtils.findJoinDescriptor(prop);
    if (join != null) {
      field = DSL.field(DSL.name(join.table(), getQueryName()));
    } else if (joinDescriptor != null) {
      field = joinDescriptor.getField(this);
    } else {
      field = meta.createField(getQueryName(), fieldFactory);
    }

    return getFilter().getConditionProvider(field);
  }

  @Override
  public Condition getCondition(QuerySpec value, FieldFactory fieldFactory) {
    return getConditionProvider(fieldFactory).getCondition(getProperty().getGetter().getValue(value), getProperty().getName());
  }

  public void setActualField(BeanPropertyDefinition actualField) {
    this.actualField = actualField;
  }

  @Override
  public Collection<BoundFilterEntry<T>> getFlattenedBoundFilterEntries() {
    return ImmutableSet.of(this);
  }
}
