package com.billlv.codegenerator.domain.vo;

import lombok.Data;




@Data
public class UsersVO  {


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

