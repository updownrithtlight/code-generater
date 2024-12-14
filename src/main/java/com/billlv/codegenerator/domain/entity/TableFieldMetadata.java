package com.billlv.codegenerator.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "table_fields_metadata")
@Data
public class TableFieldMetadata extends AuditableBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "column_name", nullable = false)
    private String columnName;

    @Column(name = "field_name", nullable = false)
    private String fieldName;

    @Column(name = "data_type", nullable = false)
    private String dataType;

    @Column(name = "java_type", nullable = false)
    private String javaType;

    @Column(name = "is_nullable", nullable = false)
    private String isNullable;

    @Column(name = "column_key")
    private String columnKey;

    @Column(name = "column_default")
    private String columnDefault;

    @Column(name = "table_id", nullable = false)
    private Long tableId; // 关联 TableMetaData 的 ID

}
