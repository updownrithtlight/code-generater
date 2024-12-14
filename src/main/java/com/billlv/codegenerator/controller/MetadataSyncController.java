package com.billlv.codegenerator.controller;

import com.billlv.codegenerator.service.TableMetadataSyncService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metadata")
public class MetadataSyncController {

    private final TableMetadataSyncService tableMetadataSyncService;

    public MetadataSyncController(TableMetadataSyncService tableMetadataSyncService) {
        this.tableMetadataSyncService = tableMetadataSyncService;
    }

    @PostMapping("/sync")
    public String syncMetadata() {
        tableMetadataSyncService.syncTableAndColumnMetadata();
        return "Metadata synchronization completed successfully!";
    }
}
