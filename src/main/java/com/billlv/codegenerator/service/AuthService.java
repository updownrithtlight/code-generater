package com.billlv.codegenerator.service;



import com.billlv.codegenerator.domain.vo.UsersVO;

import java.util.Map;

public interface AuthService {

     String login(String username, String password);
     UsersVO read(String id);
}