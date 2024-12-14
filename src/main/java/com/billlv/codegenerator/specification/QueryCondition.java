package com.billlv.codegenerator.specification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single query condition.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryCondition {
    private String fieldName; // 实体字段名
    private String operator; // 查询操作，例如 =, >, <, like
    private Object value;    // 查询值
}
