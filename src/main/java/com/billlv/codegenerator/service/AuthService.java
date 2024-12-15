package com.billlv.codegenerator.service;




import java.util.Map;

public interface AuthService {

     Map<String, String> login(String username, String password);
}
