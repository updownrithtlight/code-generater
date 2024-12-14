package com.billlv.codegenerator.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "table_meta_data")
public class TableMetaData extends AuditableBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "table_name", nullable = false)
    private String tableName;
    @Column(name = "class_name", nullable = false)
    private String className;
    @Column(name = "schema_name", nullable = false)
    private String schemaName;

}