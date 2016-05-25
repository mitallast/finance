package org.mitallast.finance.yahoo.form.specification;

import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.List;

public class FormRange<Form, Entity> {
    private final Form form;
    private final JpaSpecificationExecutor<Entity> repository;

    public FormRange(Form form, JpaSpecificationExecutor<Entity> repository) {
        this.form = form;
        this.repository = repository;
    }

    @SuppressWarnings("unchecked")
    public void load() {

        for (final Field field : form.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            try {
                Min minAnnotation = field.getAnnotation(Min.class);
                if (minAnnotation != null) {
                    if (!minAnnotation.field().isEmpty()) {
                        fieldName = minAnnotation.field();
                    }
                    final String finalFieldName = fieldName;
                    List<Entity> content = repository.findAll(new Specification<Entity>() {
                        @Override
                        public Predicate toPredicate(Root<Entity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                            query.orderBy(new OrderImpl(root.get(finalFieldName), true));
                            return cb.isNotNull(root.get(finalFieldName));
                        }
                    }, new PageRequest(0, 1)).getContent();
                    if (!content.isEmpty()) {
                        Entity minimalEntity = content.get(0);
                        Field entityField = minimalEntity.getClass().getDeclaredField(fieldName);
                        entityField.setAccessible(true);
                        setValue(field, entityField.get(minimalEntity));
                    } else {
                        setValue(field, null);
                    }
                    continue;
                }
                Max maxAnnotation = field.getAnnotation(Max.class);
                if (maxAnnotation != null) {
                    if (!maxAnnotation.field().isEmpty()) {
                        fieldName = maxAnnotation.field();
                    }
                    final String finalFieldName = fieldName;
                    List<Entity> content = repository.findAll(new Specification<Entity>() {
                        @Override
                        public Predicate toPredicate(Root<Entity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                            query.orderBy(new OrderImpl(root.get(finalFieldName), false));
                            return cb.isNotNull(root.get(finalFieldName));
                        }
                    }, new PageRequest(0, 1)).getContent();
                    if (!content.isEmpty()) {
                        Entity maximalEntity = content.get(0);
                        Field entityField = maximalEntity.getClass().getDeclaredField(fieldName);
                        entityField.setAccessible(true);
                        setValue(field, entityField.get(maximalEntity));
                    } else {
                        setValue(field, null);
                    }
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setValue(Field field, Object value) throws IllegalAccessException {
        if (value == null) {
            if (field.getType().isAssignableFrom(Integer.class)) {
                field.set(form, 0);
            } else if (field.getType().isAssignableFrom(Long.class)) {
                field.set(form, 0l);
            } else if (field.getType().isAssignableFrom(Float.class)) {
                field.set(form, 0.0f);
            } else if (field.getType().isAssignableFrom(Double.class)) {
                field.set(form, 0.0);
            } else {
                throw new RuntimeException("Unsupported value type " + field.getType());
            }
            return;
        }
        Number numberValue = (Number) value;
        if (field.getType().equals(Integer.class)) {
            field.set(form, numberValue.intValue());
        } else if (field.getType().equals(Long.class)) {
            field.set(form, numberValue.longValue());
        } else if (field.getType().equals(Float.class)) {
            field.set(form, numberValue.floatValue());
        } else if (field.getType().equals(Double.class)) {
            field.set(form, numberValue.doubleValue());
        } else {
            throw new RuntimeException("Unsupported value type " + field.getType());
        }
    }
}
