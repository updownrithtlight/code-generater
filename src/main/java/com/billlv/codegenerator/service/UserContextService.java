package com.billlv.codegenerator.service;

import com.billlv.codegenerator.common.utils.JwtUtils;
import com.billlv.codegenerator.common.utils.RedisUtility;
import com.billlv.codegenerator.domain.dto.UsersDTO;
import com.billlv.codegenerator.domain.entity.UsersEntity;
import com.billlv.codegenerator.exception.global.ErrorCode;
import com.billlv.codegenerator.exception.user.UnauthorizedException;
import com.billlv.codegenerator.exception.user.UserException;
import com.billlv.codegenerator.repository.UsersRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class UserContextService {

    @Autowired
    private RedisUtility redisUtility;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 获取当前用户信息
     */
    public UsersEntity getCurrentUser(HttpServletRequest request, HttpServletResponse response) {
        // 从 Cookie 中获取 Token
        String accessToken = extractTokenFromCookie(request, "accessToken");

        if (accessToken == null || !jwtUtils.validateJwtToken(accessToken)) {
            throw new UnauthorizedException("Invalid or missing token");
        }

        // 从 Token 中提取用户 ID
        String userId = jwtUtils.getUserIdFromToken(accessToken);

        // 优先从缓存获取用户信息
        UsersEntity usersEntity = getCachedUserDetails(userId);
        if (usersEntity != null) {
            return usersEntity;
        }

        // 缓存未命中，从数据库查询
        usersEntity = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        // 写入缓存
        cacheUserDetails(userId, usersEntity);

        return usersEntity;
    }

    /**
     * 从 Redis 缓存获取用户信息
     */
    private UsersEntity getCachedUserDetails(String userId) {
        try {
            return (UsersEntity) redisUtility.get("user:" + userId);
        } catch (Exception e) {
            log.warn("Failed to fetch user details from cache: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 缓存用户信息到 Redis
     */
    private void cacheUserDetails(String userId, UsersEntity userDetails) {
        try {
            redisUtility.save("user:" + userId, userDetails, 36000);
        } catch (Exception e) {
            log.warn("Failed to cache user details: {}", e.getMessage());
        }
    }

    /**
     * 从 Cookie 中提取指定名称的 Token
     */
    private String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
