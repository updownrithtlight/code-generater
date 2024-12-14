package com.billlv.codegenerator.specification;

import com.billlv.codegenerator.domain.entity.ProjectConfig;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Specification builder for dynamic query conditions.
 */
public class ProjectConfigSpecification {

    /**
     * Build a Specification based on dynamic conditions.
     * @param conditions the list of query conditions
     * @return a Specification for ProjectConfig
     */
    public static Specification<ProjectConfig> build(List<QueryCondition> conditions) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (QueryCondition condition : conditions) {
                String fieldName = condition.getFieldName();
                String operator = condition.getOperator();
                Object value = condition.getValue();

                if (value == null || fieldName == null || operator == null) {
                    continue;
                }

                switch (operator) {
                    case "=":
                        predicates.add(criteriaBuilder.equal(root.get(fieldName), value));
                        break;
                    case ">":
                        predicates.add(criteriaBuilder.greaterThan(root.get(fieldName), (Comparable) value));
                        break;
                    case ">=":
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), (Comparable) value));
                        break;
                    case "<":
                        predicates.add(criteriaBuilder.lessThan(root.get(fieldName), (Comparable) value));
                        break;
                    case "<=":
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), (Comparable) value));
                        break;
                    case "like":
                        predicates.add(criteriaBuilder.like(root.get(fieldName), "%" + value + "%"));
                        break;
                    case "in":
                        predicates.add(root.get(fieldName).in((List<?>) value));
                        break;
                    case "notEqual":
                        predicates.add(criteriaBuilder.notEqual(root.get(fieldName), value));
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported operator: " + operator);
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
