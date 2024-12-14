package com.billlv.codegenerator.domain.entity;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "menus")
@Data
public class Menu extends AuditableBaseEntity implements Comparable<Menu> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String code;

    private String path;

    private String icon;


    @Override
    public int compareTo(Menu o) {
        return 0;
    }
}

