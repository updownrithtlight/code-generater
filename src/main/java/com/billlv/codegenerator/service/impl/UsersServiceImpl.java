package com.billlv.codegenerator.service.impl;

import com.billlv.codegenerator.domain.dto.UsersDTO;
import com.billlv.codegenerator.domain.vo.UsersVO;
import com.billlv.codegenerator.domain.entity.UsersEntity;
import com.billlv.codegenerator.mapper.UsersMapper;
import com.billlv.codegenerator.repository.UsersRepository;
import com.billlv.codegenerator.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.billlv.codegenerator.specification.QueryCondition;
import com.billlv.codegenerator.specification.UsersSpecification;
import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;


    @Override
    public UsersVO create(UsersDTO dto) {
        // 使用 Mapper 转换 DTO -> Entity
        UsersEntity entity = UsersMapper.INSTANCE.toEntity(dto);

        // 保存 Entity
        UsersEntity savedEntity = usersRepository.save(entity);

        // 使用 Mapper 转换 Entity -> VO
        return UsersMapper.INSTANCE.toVO(savedEntity);
    }

    @Override
    public UsersVO read(Long id) {
        // 查询 Entity
        UsersEntity entity = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Users not found with ID: " + id));

        // 使用 Mapper 转换 Entity -> VO
        return UsersMapper.INSTANCE.toVO(entity);
    }

    @Override
    public UsersVO update(Long id, UsersDTO dto) {
        // 查询 Entity
        UsersEntity entity = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Users not found with ID: " + id));

        // 使用 Mapper 更新 Entity
        UsersMapper.INSTANCE.updateEntityFromDTO(dto, entity);

        // 保存更新后的 Entity
        UsersEntity updatedEntity = usersRepository.save(entity);

        // 使用 Mapper 转换 Entity -> VO
        return UsersMapper.INSTANCE.toVO(updatedEntity);
    }

    @Override
    public void delete(Long id) {
        if (!usersRepository.existsById(id)) {
            throw new RuntimeException("Users not found with ID: " + id);
        }
        usersRepository.deleteById(id);
    }

    @Override
    public Page<UsersVO> getAll(   Pageable pageable) {
        // 查询分页数据
        Page<UsersEntity> entities = usersRepository.findAll(pageable);

        // 使用 Mapper 转换 Page<Entity> -> Page<VO>
        return entities.map(UsersMapper.INSTANCE::toVO);
    }
     @Override
     public Page<UsersVO> search(List<QueryCondition> conditions, Pageable pageable) {
            return usersRepository
                    .findAll(UsersSpecification.build(conditions), pageable)
                    .map(UsersMapper.INSTANCE::toVO);
        }

    @Override
    public UsersVO findByUsername(String username) {
        UsersEntity entity = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Users not found with username: "+username));
        return UsersMapper.INSTANCE.toVO(entity);
    }
}
