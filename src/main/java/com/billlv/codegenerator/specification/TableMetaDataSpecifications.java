package com.billlv.codegenerator.specification;

import com.billlv.codegenerator.domain.entity.TableMetaData;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class TableMetaDataSpecifications {

    public static Specification<TableMetaData> getSpecifications(String tableName, String schemaName) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add conditions based on request parameters
            if (tableName != null && !tableName.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("tableName"), "%" + tableName + "%"));
            }
            if (schemaName != null && !schemaName.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("schemaName"), schemaName));
            }

            // Combine all conditions with AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
