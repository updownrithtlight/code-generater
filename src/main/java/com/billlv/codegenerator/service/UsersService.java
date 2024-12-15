package com.billlv.codegenerator.service;

import com.billlv.codegenerator.domain.entity.UsersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.billlv.codegenerator.domain.dto.UsersDTO;
import com.billlv.codegenerator.domain.vo.UsersVO;
import com.billlv.codegenerator.specification.QueryCondition;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Users.
 * Handles business logic for managing Users.
 */
public interface UsersService {

    /**
     * Create a new Users.
     *
     * @param dto the Users DTO containing creation data
     * @return the created Users as a VO
     */
    UsersVO create(UsersDTO dto);

    /**
     * Retrieve details of a specific Users.
     *
     * @param id the ID of the Users to retrieve
     * @return the Users details as a VO
     */
    UsersVO read(Long id);

    /**
     * Update an existing Users.
     *
     * @param id the ID of the Users to update
     * @param dto the Users DTO containing updated data
     * @return the updated Users as a VO
     */
    UsersVO update(Long id, UsersDTO dto);

    /**
     * Delete a specific Users.
     *
     * @param id the ID of the Users to delete
     */
    void delete(Long id);


    Page<UsersVO> search(
                   List<QueryCondition> conditions,
                  Pageable pageable);

    /**
     * Query Users with pagination and optional keyword search.
     *
     * @param keyword optional search keyword
     * @param pageable pagination details
     * @return a paginated list of Users as VO
     */
    Page<UsersVO> getAll(  Pageable pageable);


    UsersVO findByUsername(String username);
}
