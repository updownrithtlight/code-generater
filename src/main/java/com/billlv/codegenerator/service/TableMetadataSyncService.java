package com.billlv.codegenerator.service;

import com.billlv.codegenerator.domain.entity.TableFieldMetadata;
import com.billlv.codegenerator.domain.entity.TableMetaData;
import com.billlv.codegenerator.repository.TableFieldMetadataRepository;
import com.billlv.codegenerator.repository.TableMetaDataRepository;
import com.billlv.codegenerator.common.utils.CodeGenUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class TableMetadataSyncService {

    private final JdbcTemplate jdbcTemplate;
    private final TableMetaDataRepository tableMetaDataRepository;
    private final TableFieldMetadataRepository tableFieldMetadataRepository;

    public TableMetadataSyncService(JdbcTemplate jdbcTemplate,
                                    TableMetaDataRepository tableMetaDataRepository,
                                    TableFieldMetadataRepository tableFieldMetadataRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableMetaDataRepository = tableMetaDataRepository;
        this.tableFieldMetadataRepository = tableFieldMetadataRepository;
    }

    /**
     * 同步表和字段元数据
     */
    @Transactional
    public void syncTableAndColumnMetadata() {
        // 获取所有表名
        String sqlForTables = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = DATABASE()";
        List<String> tableNames = jdbcTemplate.queryForList(sqlForTables, String.class);

        for (String tableName : tableNames) {
            // 检查表元数据是否已存在
            TableMetaData existingTableMetaData = tableMetaDataRepository.findByTableNameAndSchemaName(
                    tableName, getCurrentSchemaName()
            );

            TableMetaData tableMetaData;
            if (existingTableMetaData == null) {
                // 如果不存在，插入表元数据
                tableMetaData = new TableMetaData();
                tableMetaData.setTableName(tableName);
                tableMetaData.setClassName(CodeGenUtils.convertToClassName(tableName));
                tableMetaData.setSchemaName(getCurrentSchemaName());
                tableMetaData = tableMetaDataRepository.save(tableMetaData);
            } else {
                tableMetaData = existingTableMetaData; // 使用已存在的元数据
            }

            // 获取列信息
            String sqlForColumns = "SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_KEY, COLUMN_DEFAULT " +
                    "FROM INFORMATION_SCHEMA.COLUMNS " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?";
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(sqlForColumns, tableName);

            // 插入或更新列元数据
            for (Map<String, Object> column : columns) {
                String columnName = (String) column.get("COLUMN_NAME");
                String dataType = (String) column.get("DATA_TYPE");
                String isNullable = (String) column.get("IS_NULLABLE");
                String columnKey = (String) column.get("COLUMN_KEY");
                String columnDefault = column.get("COLUMN_DEFAULT") != null ? column.get("COLUMN_DEFAULT").toString() : null;

                // 检查列元数据是否已存在
                TableFieldMetadata existingFieldMetadata = tableFieldMetadataRepository.findByTableIdAndColumnName(
                        tableMetaData.getId(), columnName
                );

                if (existingFieldMetadata == null) {
                    // 如果不存在，插入列元数据
                    TableFieldMetadata fieldMetadata = new TableFieldMetadata();
                    fieldMetadata.setTableId(tableMetaData.getId());
                    fieldMetadata.setColumnName(columnName);
                    fieldMetadata.setFieldName(CodeGenUtils.capitalizeFirstLetter(CodeGenUtils.convertToClassName(columnName)));
                    fieldMetadata.setDataType(dataType);
                    fieldMetadata.setJavaType(CodeGenUtils.mapSqlTypeToJavaType(dataType));
                    fieldMetadata.setIsNullable(isNullable);
                    fieldMetadata.setColumnKey(columnKey);
                    fieldMetadata.setColumnDefault(columnDefault);

                    tableFieldMetadataRepository.save(fieldMetadata);
                } else {
                    // 如果存在，更新字段（只在有变化时更新）
                    boolean updated = false;

                    if (!dataType.equals(existingFieldMetadata.getDataType())) {
                        existingFieldMetadata.setDataType(dataType);
                        updated = true;
                    }
                    if (!isNullable.equals(existingFieldMetadata.getIsNullable())) {
                        existingFieldMetadata.setIsNullable(isNullable);
                        updated = true;
                    }
                    if (!columnKey.equals(existingFieldMetadata.getColumnKey())) {
                        existingFieldMetadata.setColumnKey(columnKey);
                        updated = true;
                    }
                    if ((columnDefault != null && !columnDefault.equals(existingFieldMetadata.getColumnDefault())) ||
                            (columnDefault == null && existingFieldMetadata.getColumnDefault() != null)) {
                        existingFieldMetadata.setColumnDefault(columnDefault);
                        updated = true;
                    }

                    if (updated) {
                        tableFieldMetadataRepository.save(existingFieldMetadata);
                    }
                }
            }
        }
    }

    /**
     * 获取当前数据库的 Schema 名
     */
    private String getCurrentSchemaName() {
        String sql = "SELECT DATABASE()";
        return jdbcTemplate.queryForObject(sql, String.class);
    }
}
