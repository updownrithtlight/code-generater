package com.billlv.codegenerator.service;

import com.billlv.codegenerator.domain.entity.TableFieldMetadata;
import com.billlv.codegenerator.domain.entity.TableMetaData;
import com.billlv.codegenerator.repository.TableFieldMetadataRepository;
import com.billlv.codegenerator.repository.TableMetaDataRepository;
import com.billlv.codegenerator.specification.TableMetaDataSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableMetaDataService {
    private final TableMetaDataRepository tableMetaDataRepository;

    private final TableFieldMetadataRepository tableFieldMetadataRepository;


    public TableMetaDataService(TableMetaDataRepository tableMetaDataRepository,TableFieldMetadataRepository tableFieldMetadataRepository) {
        this.tableMetaDataRepository = tableMetaDataRepository;
        this.tableFieldMetadataRepository = tableFieldMetadataRepository;
    }

    // Service method
    public Page<TableMetaData> getFilteredTableNames(Pageable pageable, String tableName, String schemaName) {
        Specification<TableMetaData> spec = TableMetaDataSpecifications.getSpecifications(tableName, schemaName);
        return tableMetaDataRepository.findAll(spec, pageable);

    }

    public TableMetaData getTableMetaDataById(Long id){
        return tableMetaDataRepository.getById(id);
    }


    /**
     * 根据 tableId 查询字段信息
     */
    @Transactional(readOnly = true)
    public List<TableFieldMetadata> getColumnsByTableId(Long tableId) {
        return tableFieldMetadataRepository.findByTableId(tableId);
    }

}
