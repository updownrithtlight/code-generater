package com.billlv.codegenerator.domain.entity;

import lombok.Data;
import com.billlv.codegenerator.domain.entity.AuditableBaseEntity;
import jakarta.persistence.*;


/**
 * Entity class for users.
 */
@Entity
@Table(name = "users")
@Data
public class UsersEntity extends AuditableBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department_id")
    private Long departmentId;
    @Column(name = "disabled")
    private Boolean disabled;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "open_id")
    private String openId;
    @Column(name = "password")
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "remark")
    private String remark;
    @Column(name = "role_id")
    private Long roleId;
    @Column(name = "username")
    private String username;
}

