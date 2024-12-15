package com.billlv.codegenerator.repository;

import com.billlv.codegenerator.domain.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Users.
 */
@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Long>, JpaSpecificationExecutor<UsersEntity> {
    // Add custom query methods here if needed

    Optional<UsersEntity> findByUsername(String username);
}
