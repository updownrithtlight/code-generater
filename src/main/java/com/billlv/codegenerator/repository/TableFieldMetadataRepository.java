package com.billlv.codegenerator.repository;
import com.billlv.codegenerator.domain.entity.TableFieldMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableFieldMetadataRepository extends JpaRepository<TableFieldMetadata, Long> {
    List<TableFieldMetadata> findByTableId(Long tableId);
    TableFieldMetadata findByTableIdAndColumnName(Long tableId, String columnName);

}
