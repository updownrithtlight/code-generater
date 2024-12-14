package com.billlv.codegenerator.service.impl;

import com.billlv.codegenerator.domain.dto.ProjectConfigDTO;
import com.billlv.codegenerator.domain.vo.ProjectConfigVO;
import com.billlv.codegenerator.domain.entity.ProjectConfig;
import com.billlv.codegenerator.mapper.ProjectConfigMapper;
import com.billlv.codegenerator.repository.ProjectConfigRepository;
import com.billlv.codegenerator.service.ProjectConfigService;
import com.billlv.codegenerator.specification.ProjectConfigSpecification;
import com.billlv.codegenerator.specification.QueryCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectConfigServiceImpl implements ProjectConfigService {

    @Autowired
    private ProjectConfigRepository projectConfigRepository;


    @Override
    public ProjectConfigVO create(ProjectConfigDTO dto) {
        // 使用 Mapper 转换 DTO -> Entity
        ProjectConfig entity = ProjectConfigMapper.INSTANCE.toEntity(dto);

        // 保存 Entity
        ProjectConfig savedEntity = projectConfigRepository.save(entity);

        // 使用 Mapper 转换 Entity -> VO
        return ProjectConfigMapper.INSTANCE.toVO(savedEntity);
    }

    @Override
    public ProjectConfigVO read(Long id) {
        // 查询 Entity
        ProjectConfig entity = projectConfigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProjectConfig not found with ID: " + id));

        // 使用 Mapper 转换 Entity -> VO
        return ProjectConfigMapper.INSTANCE.toVO(entity);
    }

    @Override
    public ProjectConfigVO update(Long id, ProjectConfigDTO dto) {
        // 查询 Entity
        ProjectConfig entity = projectConfigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProjectConfig not found with ID: " + id));

        // 使用 Mapper 更新 Entity
        ProjectConfigMapper.INSTANCE.updateEntityFromDTO(dto, entity);

        // 保存更新后的 Entity
        ProjectConfig updatedEntity = projectConfigRepository.save(entity);

        // 使用 Mapper 转换 Entity -> VO
        return ProjectConfigMapper.INSTANCE.toVO(updatedEntity);
    }

    @Override
    public void delete(Long id) {
        if (!projectConfigRepository.existsById(id)) {
            throw new RuntimeException("ProjectConfig not found with ID: " + id);
        }
        projectConfigRepository.deleteById(id);
    }

    @Override
    public Page<ProjectConfigVO> getAll(   Pageable pageable) {
        // 查询分页数据
        Page<ProjectConfig> entities = projectConfigRepository.findAll( pageable);

        // 使用 Mapper 转换 Page<Entity> -> Page<VO>
        return entities.map(ProjectConfigMapper.INSTANCE::toVO);
    }
     @Override
     public Page<ProjectConfigVO> search(List<QueryCondition> conditions, Pageable pageable) {
            return projectConfigRepository
                    .findAll(ProjectConfigSpecification.build(conditions), pageable)
                    .map(ProjectConfigMapper.INSTANCE::toVO);
        }

    @Override
    public ProjectConfigVO findByConfigKey(String configKey) {
        ProjectConfig projectConfig = projectConfigRepository.findByConfigKey(configKey);
        return ProjectConfigMapper.INSTANCE.toVO(projectConfig);
    }
}
