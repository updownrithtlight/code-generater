package com.billlv.codegenerator.service.impl;

import com.billlv.codegenerator.domain.vo.UsersVO;
import com.billlv.codegenerator.service.AuthService;
import com.billlv.codegenerator.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UsersService usersService;



    @Override
    public UsersVO read(String id) {
        return usersService.read(Long.parseLong(id));
    }
}
