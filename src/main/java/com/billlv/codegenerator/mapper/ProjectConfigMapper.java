package com.billlv.codegenerator.mapper;

import com.billlv.codegenerator.domain.dto.ProjectConfigDTO;
import com.billlv.codegenerator.domain.entity.ProjectConfig;
import com.billlv.codegenerator.domain.vo.ProjectConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper interface for converting ProjectConfig entities to DTOs and VOs, and vice versa.
 */
@Mapper
public interface ProjectConfigMapper {

    /**
     * Get the Mapper instance.
     */
    ProjectConfigMapper INSTANCE = Mappers.getMapper(ProjectConfigMapper.class);

    /**
     * Convert Entity to DTO.
     *
     * @param entity the ProjectConfig entity
     * @return the corresponding ProjectConfig DTO
     */
    ProjectConfigDTO toDTO(ProjectConfig entity);

    /**
     * Convert DTO to Entity.
     *
     * @param dto the ProjectConfig DTO
     * @return the corresponding ProjectConfig entity
     */
    ProjectConfig toEntity(ProjectConfigDTO dto);

    /**
     * Convert Entity to VO.
     *
     * @param entity the ProjectConfig entity
     * @return the corresponding ProjectConfig VO
     */
    ProjectConfigVO toVO(ProjectConfig entity);

    /**
     * Convert VO to Entity.
     *
     * @param vo the ProjectConfig VO
     * @return the corresponding ProjectConfig entity
     */
    ProjectConfig toEntityFromVO(ProjectConfigVO vo);

    void updateEntityFromDTO(ProjectConfigDTO dto, @MappingTarget ProjectConfig entity);


    /**
     * Convert a list of Entities to a list of DTOs.
     *
     * @param entities the list of ProjectConfig entities
     * @return the corresponding list of ProjectConfig DTOs
     */
    List<ProjectConfigDTO> toDTOList(List<ProjectConfig> entities);

    /**
     * Convert a list of DTOs to a list of Entities.
     *
     * @param dtos the list of ProjectConfig DTOs
     * @return the corresponding list of ProjectConfig entities
     */
    List<ProjectConfig> toEntityList(List<ProjectConfigDTO> dtos);

    /**
     * Convert a list of Entities to a list of VOs.
     *
     * @param entities the list of ProjectConfig entities
     * @return the corresponding list of ProjectConfig VOs
     */
    List<ProjectConfigVO> toVOList(List<ProjectConfig> entities);

    /**
     * Convert a list of VOs to a list of Entities.
     *
     * @param vos the list of ProjectConfig VOs
     * @return the corresponding list of ProjectConfig entities
     */
    List<ProjectConfig> toEntityListFromVO(List<ProjectConfigVO> vos);
}
