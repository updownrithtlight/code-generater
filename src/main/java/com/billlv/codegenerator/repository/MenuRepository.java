package com.billlv.codegenerator.repository;

import com.billlv.codegenerator.domain.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
