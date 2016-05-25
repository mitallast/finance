package org.mitallast.finance.yahoo.form.specification;

import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FormSpecification<Form, Entity> implements Specification<Entity> {
    private final Form form;

    public FormSpecification(Form form) {
        this.form = form;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Predicate toPredicate(Root<Entity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> expressions = new ArrayList<>();
        String sort = null;
        boolean desc = false;

        for (Field field : form.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            try {
                Object fieldValue = field.get(form);
                if (fieldValue == null) {
                    continue;
                }
                Equals equalsAnnotation = field.getAnnotation(Equals.class);
                if (equalsAnnotation != null) {
                    if (equalsAnnotation.field().length == 0) {
                        expressions.add(cb.equal(root.get(fieldName), fieldValue));
                    } else if (equalsAnnotation.field().length == 1) {
                        fieldName = equalsAnnotation.field()[0];
                        expressions.add(cb.equal(root.get(fieldName), fieldValue));
                    } else if (equalsAnnotation.field().length > 1) {
                        List<Predicate> orPredicate = new ArrayList<>(equalsAnnotation.field().length);
                        for (String fieldNameItem : equalsAnnotation.field()) {
                            orPredicate.add(cb.equal(root.get(fieldNameItem), fieldValue));
                        }
                        expressions.add(cb.or(orPredicate.toArray(new Predicate[orPredicate.size()])));
                    }
                    continue;
                }

                Greater greaterAnnotation = field.getAnnotation(Greater.class);
                if (greaterAnnotation != null) {
                    if (!greaterAnnotation.field().isEmpty()) {
                        fieldName = greaterAnnotation.field();
                    }
                    expressions.add(cb.greaterThanOrEqualTo(root.<Comparable>get(fieldName), (Comparable) fieldValue));
                    continue;
                }

                Less lessAnnotation = field.getAnnotation(Less.class);
                if (lessAnnotation != null) {
                    if (!lessAnnotation.field().isEmpty()) {
                        fieldName = lessAnnotation.field();
                    }
                    expressions.add(cb.lessThanOrEqualTo(root.<Comparable>get(fieldName), (Comparable) fieldValue));
                    continue;
                }

                SortField sortFieldAnnotation = field.getAnnotation(SortField.class);
                if (sortFieldAnnotation != null) {
                    sort = fieldValue.toString();
                    continue;
                }

                SortDirection sortDirectionAnnotation = field.getAnnotation(SortDirection.class);
                if (sortDirectionAnnotation != null) {
                    if (sortDirectionAnnotation.desc()) {
                        desc = field.getBoolean(form);
                    } else {
                        desc = !field.getBoolean(form);
                    }
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        if (sort != null && !sort.isEmpty()) {
            query.orderBy(new OrderImpl(root.get(sort), !desc));
        }

        return cb.and(expressions.toArray(new Predicate[expressions.size()]));
    }
}
