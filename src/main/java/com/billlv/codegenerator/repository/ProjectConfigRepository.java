package com.billlv.codegenerator.repository;

import com.billlv.codegenerator.domain.entity.ProjectConfig;
import com.billlv.codegenerator.domain.vo.ProjectConfigVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for ProjectConfig.
 */
@Repository
public interface ProjectConfigRepository extends JpaRepository<ProjectConfig, Long>, JpaSpecificationExecutor<ProjectConfig> {
    ProjectConfig findByConfigKey(String configKey);
    // Add custom query methods here if needed
}
