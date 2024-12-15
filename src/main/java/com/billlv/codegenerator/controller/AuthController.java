package com.billlv.codegenerator.controller;

import com.billlv.codegenerator.common.config.CookieProperties;
import com.billlv.codegenerator.domain.dto.LoginRequestDTO;
import com.billlv.codegenerator.exception.user.UserException;
import com.billlv.codegenerator.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CookieProperties cookieProperties;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest, HttpServletResponse response) {
        try {
            // 调用 AuthService 的登录逻辑
            Map<String, String> tokens = authService.login(loginRequest.getUsername(), loginRequest.getPassword());

            // 设置 Access Token 到 Cookie
            Cookie accessTokenCookie = new Cookie("accessToken", tokens.get("accessToken"));
            accessTokenCookie.setHttpOnly(cookieProperties.getAccessToken().isHttpOnly());
            accessTokenCookie.setPath(cookieProperties.getAccessToken().getPath());
            accessTokenCookie.setMaxAge(cookieProperties.getAccessToken().getMaxAge());
            response.addCookie(accessTokenCookie);

            // 设置 Refresh Token 到 Cookie
            Cookie refreshTokenCookie = new Cookie("refreshToken", tokens.get("refreshToken"));
            refreshTokenCookie.setHttpOnly(cookieProperties.getRefreshToken().isHttpOnly());
            refreshTokenCookie.setPath(cookieProperties.getRefreshToken().getPath());
            refreshTokenCookie.setMaxAge(cookieProperties.getRefreshToken().getMaxAge());
            response.addCookie(refreshTokenCookie);

            // 返回成功响应
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "登录成功",
                    "username", loginRequest.getUsername()
            ));

        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", e.getLocalizedMessage(),
                    "errorCode", e.getCode()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "服务器内部错误"
            ));
        }
    }
}
