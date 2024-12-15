package com.billlv.codegenerator.controller;

import com.billlv.codegenerator.common.config.CookieProperties;
import com.billlv.codegenerator.common.utils.JwtUtils;
import com.billlv.codegenerator.domain.dto.LoginRequestDTO;
import com.billlv.codegenerator.domain.vo.UsersVO;
import com.billlv.codegenerator.exception.user.UserException;
import com.billlv.codegenerator.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CookieProperties cookieProperties;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest, HttpServletResponse response) {
        try {
            // 调用 AuthService 的登录逻辑
            String userId = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
            String accessToken = jwtUtils.generateAccessToken(userId);
            String refreshToken = jwtUtils.generateRefreshToken(userId);
            // 设置 Access Token 到 Cookie
            Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
            accessTokenCookie.setHttpOnly(cookieProperties.getAccessToken().isHttpOnly());
            accessTokenCookie.setPath(cookieProperties.getAccessToken().getPath());
            accessTokenCookie.setMaxAge(cookieProperties.getAccessToken().getMaxAge());
            response.addCookie(accessTokenCookie);

            // 设置 Refresh Token 到 Cookie
            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(cookieProperties.getRefreshToken().isHttpOnly());
            refreshTokenCookie.setPath(cookieProperties.getRefreshToken().getPath());
            refreshTokenCookie.setMaxAge(cookieProperties.getRefreshToken().getMaxAge());
            response.addCookie(refreshTokenCookie);
            Map<String, Object> responseBody = new HashMap<>();
            UsersVO usersVO = new UsersVO();
            usersVO.setUsername("admin");
            usersVO.setFullName("管理员");
            usersVO.setRoleId(1L);
            usersVO.setDisabled(false);
            responseBody.put("userInfo", usersVO);
            // 返回成功响应
            return ResponseEntity.ok(responseBody);

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


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookie(request);
        if (refreshToken == null || !jwtUtils.validateJwtToken(refreshToken)) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body(Map.of("error", "Refresh Token is invalid or missing"));
        }
        String userId = jwtUtils.getUserIdFromToken(refreshToken);

        String newAccessToken = jwtUtils.generateAccessToken(userId);

        Cookie newAccessTokenCookie = new Cookie("accessToken", newAccessToken);
        newAccessTokenCookie.setHttpOnly(true);
        newAccessTokenCookie.setSecure(true);
        newAccessTokenCookie.setPath("/");
        newAccessTokenCookie.setMaxAge(3600);
        response.addCookie(newAccessTokenCookie);
        UsersVO usersVO = authService.read(userId);
        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken,
                "userInfo", usersVO
        ));
    }

    /**
     * 从 Cookie 中提取 Refresh Token
     */
    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) { // 假设 Cookie 名为 'refreshToken'
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}
