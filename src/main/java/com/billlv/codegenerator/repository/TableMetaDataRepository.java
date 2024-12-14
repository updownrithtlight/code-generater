package com.billlv.codegenerator.repository;

import com.billlv.codegenerator.domain.entity.TableMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

// Repository
public interface TableMetaDataRepository extends JpaRepository<TableMetaData, Long>, JpaSpecificationExecutor<TableMetaData> {
    TableMetaData findByTableNameAndSchemaName(String tableName, String schemaName);

}