package com.billlv.codegenerator.domain.entity;

import lombok.Data;
import com.billlv.codegenerator.domain.entity.AuditableBaseEntity;
import jakarta.persistence.*;


/**
 * Entity class for project_config.
 */
@Entity
@Table(name = "project_config")
@Data
public class ProjectConfig extends AuditableBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config_key")
    private String configKey;
    @Column(name = "config_value")
    private String configValue;
    @Column(name = "description")
    private String description;
}

