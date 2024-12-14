package com.billlv.codegenerator.controller;

import com.billlv.codegenerator.domain.dto.ProjectConfigDTO;
import com.billlv.codegenerator.specification.QueryCondition;
import com.billlv.codegenerator.service.ProjectConfigService;
import com.billlv.codegenerator.common.utils.Result;
import com.billlv.codegenerator.domain.vo.ProjectConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * REST Controller for managing ProjectConfig.
 * Provides endpoints for creating, reading, updating, and deleting ProjectConfig.
 */
@Slf4j
@RestController
@RequestMapping("/api/project_config")
public class ProjectConfigController {

    @Autowired
    private ProjectConfigService projectConfigService;

    /**
     * Retrieve a paginated list of ProjectConfig with optional search parameters.
     *
     * @param pageable pagination information
     * @param searchParam1 optional search parameter
     * @param searchParam2 optional search parameter
     * @return paginated list of ProjectConfig wrapped in a Result
     */
    @GetMapping
    public ResponseEntity<Result<Page<ProjectConfigVO>>> getAll(
        @PageableDefault(size = 10) Pageable pageable) {
        Page<ProjectConfigVO> results = projectConfigService.getAll(  pageable);
        return ResponseEntity.ok(Result.success(results));
    }


       @PostMapping("/search")
        public ResponseEntity<Result<Page<ProjectConfigVO>>> search(
                @RequestBody List<QueryCondition> conditions,
                @PageableDefault(size = 10) Pageable pageable) {

            Page<ProjectConfigVO> results = projectConfigService.search(conditions, pageable);
            return ResponseEntity.ok(Result.success(results));
        }

    /**
     * Retrieve a specific ProjectConfig by ID.
     *
     * @param id the ID of the ProjectConfig to retrieve
     * @return the ProjectConfig details wrapped in a Result
     */
    @GetMapping("/{id}")
    public ResponseEntity<Result<ProjectConfigVO>> read(@PathVariable Long id) {
        ProjectConfigVO result = projectConfigService.read(id);
        return ResponseEntity.ok(Result.success(result));
    }

    /**
     * Create a new ProjectConfig.
     *
     * @param dto the ProjectConfig data transfer object
     * @return the created ProjectConfig wrapped in a Result
     */
    @PostMapping
    public ResponseEntity<Result<ProjectConfigVO>> create( @RequestBody ProjectConfigDTO dto) {
        ProjectConfigVO createdVO = projectConfigService.create(dto);
        return ResponseEntity.ok(Result.success(createdVO));
    }

    /**
     * Update an existing ProjectConfig.
     *
     * @param id the ID of the ProjectConfig to update
     * @param dto the updated ProjectConfig data transfer object
     * @return the updated ProjectConfig wrapped in a Result
     */
    @PutMapping("/{id}")
    public ResponseEntity<Result<ProjectConfigVO>> update(@PathVariable Long id,  @RequestBody ProjectConfigDTO dto) {
        ProjectConfigVO updatedVO = projectConfigService.update(id, dto);
        return ResponseEntity.ok(Result.success(updatedVO));
    }

    /**
     * Delete a specific ProjectConfig by ID.
     *
     * @param id the ID of the ProjectConfig to delete
     * @return success response wrapped in a Result
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> delete(@PathVariable Long id) {
        projectConfigService.delete(id);
        return ResponseEntity.ok(Result.success(null));
    }
}
