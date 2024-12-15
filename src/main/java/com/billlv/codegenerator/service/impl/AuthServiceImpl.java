package com.billlv.codegenerator.service.impl;

import com.billlv.codegenerator.common.utils.JwtUtils;
import com.billlv.codegenerator.domain.vo.UsersVO;
import com.billlv.codegenerator.exception.global.ErrorCode;
import com.billlv.codegenerator.exception.user.UserException;
import com.billlv.codegenerator.service.AuthService;
import com.billlv.codegenerator.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 校验用户密码
     * @param rawPassword 用户输入的密码
     * @param encodedPassword 数据库中存储的加密密码
     * @return 是否验证通过
     */
    private boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 登录逻辑
     * @param username 用户名
     * @param password 密码
     * @return 包含 Access Token 和 Refresh Token 的 Map
     */
    @Override
    public Map<String, String> login(String username, String password) {
        // 查找用户信息
        UsersVO usersVO = usersService.findByUsername(username);

        if (usersVO == null) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }

        if (usersVO.getDisabled()) {
            throw new UserException(ErrorCode.USER_DISABLED, "该用户已被禁用，请联系管理员！");
        }

        if (!passwordEncoder.matches(password, usersVO.getPassword())) {
            throw new UserException(ErrorCode.INVALID_CREDENTIALS);
        }


        // 生成 JWT Access Token 和 Refresh Token
        String accessToken = jwtUtils.generateAccessToken(usersVO.getId().toString());
        String refreshToken = jwtUtils.generateRefreshToken(usersVO.getId().toString());

        // 封装返回结果
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }
}
