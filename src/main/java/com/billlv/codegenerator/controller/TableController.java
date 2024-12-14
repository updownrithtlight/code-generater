package com.billlv.codegenerator.controller;

import com.billlv.codegenerator.domain.entity.TableFieldMetadata;
import com.billlv.codegenerator.domain.entity.TableMetaData;
import com.billlv.codegenerator.service.TableMetaDataService;
import com.billlv.codegenerator.common.utils.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    private final TableMetaDataService tableMetaDataService;

    public TableController(TableMetaDataService tableMetaDataService) {
        this.tableMetaDataService = tableMetaDataService;
    }


    @GetMapping
    public ResponseEntity<Result<Page<TableMetaData>>> getAllCustomers(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String tableName,
            @RequestParam(required = false) String schemaName
     ){

        return ResponseEntity.ok(Result.success(tableMetaDataService.getFilteredTableNames(pageable,tableName,schemaName)));
    }



    /**
     * 根据 tableId 查询字段信息
     */
    @GetMapping("/{tableId}/columns")
    public ResponseEntity<Result<List<TableFieldMetadata>>> getColumnsByTableId(@PathVariable Long tableId) {
        return ResponseEntity.ok(Result.success(tableMetaDataService.getColumnsByTableId(tableId)));

    }


}
