package com.billlv.codegenerator.mapper;

import com.billlv.codegenerator.domain.dto.UsersDTO;
import com.billlv.codegenerator.domain.entity.UsersEntity;
import com.billlv.codegenerator.domain.vo.UsersVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper interface for converting Users entities to DTOs and VOs, and vice versa.
 */
@Mapper
public interface UsersMapper {

    /**
     * Get the Mapper instance.
     */
    UsersMapper INSTANCE = Mappers.getMapper(UsersMapper.class);

    /**
     * Convert Entity to DTO.
     *
     * @param entity the Users entity
     * @return the corresponding Users DTO
     */
    UsersDTO toDTO(UsersEntity entity);

    /**
     * Convert DTO to Entity.
     *
     * @param dto the Users DTO
     * @return the corresponding Users entity
     */
    UsersEntity toEntity(UsersDTO dto);

    /**
     * Convert Entity to VO.
     *
     * @param entity the Users entity
     * @return the corresponding Users VO
     */
    UsersVO toVO(UsersEntity entity);

    /**
     * Convert VO to Entity.
     *
     * @param vo the Users VO
     * @return the corresponding Users entity
     */
    UsersEntity toEntityFromVO(UsersVO vo);

    void updateEntityFromDTO(UsersDTO dto, @MappingTarget UsersEntity entity);


    /**
     * Convert a list of Entities to a list of DTOs.
     *
     * @param entities the list of Users entities
     * @return the corresponding list of Users DTOs
     */
    List<UsersDTO> toDTOList(List<UsersEntity> entities);

    /**
     * Convert a list of DTOs to a list of Entities.
     *
     * @param dtos the list of Users DTOs
     * @return the corresponding list of Users entities
     */
    List<UsersEntity> toEntityList(List<UsersDTO> dtos);

    /**
     * Convert a list of Entities to a list of VOs.
     *
     * @param entities the list of Users entities
     * @return the corresponding list of Users VOs
     */
    List<UsersVO> toVOList(List<UsersEntity> entities);

    /**
     * Convert a list of VOs to a list of Entities.
     *
     * @param vos the list of Users VOs
     * @return the corresponding list of Users entities
     */
    List<UsersEntity> toEntityListFromVO(List<UsersVO> vos);
}
