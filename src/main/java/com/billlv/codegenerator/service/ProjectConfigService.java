package com.billlv.codegenerator.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.billlv.codegenerator.domain.dto.ProjectConfigDTO;
import com.billlv.codegenerator.domain.vo.ProjectConfigVO;
import com.billlv.codegenerator.specification.QueryCondition;

import java.util.List;

/**
 * Service interface for ProjectConfig.
 * Handles business logic for managing ProjectConfig.
 */
public interface ProjectConfigService {

    /**
     * Create a new ProjectConfig.
     *
     * @param dto the ProjectConfig DTO containing creation data
     * @return the created ProjectConfig as a VO
     */
    ProjectConfigVO create(ProjectConfigDTO dto);

    /**
     * Retrieve details of a specific ProjectConfig.
     *
     * @param id the ID of the ProjectConfig to retrieve
     * @return the ProjectConfig details as a VO
     */
    ProjectConfigVO read(Long id);

    /**
     * Update an existing ProjectConfig.
     *
     * @param id the ID of the ProjectConfig to update
     * @param dto the ProjectConfig DTO containing updated data
     * @return the updated ProjectConfig as a VO
     */
    ProjectConfigVO update(Long id, ProjectConfigDTO dto);

    /**
     * Delete a specific ProjectConfig.
     *
     * @param id the ID of the ProjectConfig to delete
     */
    void delete(Long id);


    Page<ProjectConfigVO> search(
                   List<QueryCondition> conditions,
                  Pageable pageable);

    /**
     * Query ProjectConfig with pagination and optional keyword search.
     *
     * @param keyword optional search keyword
     * @param pageable pagination details
     * @return a paginated list of ProjectConfig as VO
     */
    Page<ProjectConfigVO> getAll(  Pageable pageable);


    ProjectConfigVO findByConfigKey(String configKey);
}
