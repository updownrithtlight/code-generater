package com.billlv.codegenerator.domain.dto;

import lombok.Data;




@Data
public class UsersDTO  {


    private Long id;


    private Long departmentId;

    private Boolean disabled;

    private String fullName;

    private String openId;

    private String password;

    private String phoneNumber;

    private String remark;

    private Long roleId;

    private String username;
}

