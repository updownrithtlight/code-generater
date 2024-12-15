package com.billlv.codegenerator.common.filter;

import com.billlv.codegenerator.common.utils.JwtUtils;
import com.billlv.codegenerator.domain.vo.UsersVO;
import com.billlv.codegenerator.service.UsersService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UsersService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        if (isExcludedPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        String token = extractTokenFromCookie(request);
        if (token == null) {
            respondWithError(response, HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid JWT Token in Cookie");
            return;
        }

        try {
            String username = jwtUtils.getUsernameFromToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticateUser(username, token, request);
            }

        } catch (ExpiredJwtException e) {
            logger.warn("JWT Token has expired");
            handleExpiredToken(request, response, chain);
            return;
        } catch (IllegalArgumentException e) {
            logger.error("Unable to get JWT Token", e);
            respondWithError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
            return;
        } catch (Exception e) {
            logger.error("Unexpected error during token validation", e);
            respondWithError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error");
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * 检查是否是无需验证的路径
     */
    private boolean isExcludedPath(String path) {
        return "/api/auth/login".equals(path) || "/api/auth/register".equals(path);
    }

    /**
     * 从 Cookie 中提取 JWT Token
     */
    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) { // 假设 Cookie 名为 'jwt'
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 处理过期 Token
     */
    /**
     * 处理过期 Token
     */
    private void handleExpiredToken(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 从 Cookie 中提取 Refresh Token
        String refreshToken = extractRefreshTokenFromCookie(request);

        if (refreshToken == null || !jwtUtils.validateJwtToken(refreshToken)) {
            respondWithError(response, HttpServletResponse.SC_UNAUTHORIZED, "Refresh Token is invalid or missing");
            return;
        }

        try {
            // 从 Refresh Token 中提取用户名
            String userId = jwtUtils.getUsernameFromToken(refreshToken);
            UsersVO usersVO = userService.read(Long.parseLong(userId));
//            // 加载用户信息
//            UserDetails userDetails = userService.loadUserByUsername(usersVO.getUsername());

            // 生成新的 Access Token
            String newAccessToken = jwtUtils.generateAccessToken(userId);

            // 设置新的 Access Token 到 Cookie
            Cookie newAccessTokenCookie = new Cookie("accessToken", newAccessToken);
            newAccessTokenCookie.setHttpOnly(true);
            newAccessTokenCookie.setSecure(true); // 在 HTTPS 环境下使用
            newAccessTokenCookie.setPath("/");
            newAccessTokenCookie.setMaxAge(3600);
            response.addCookie(newAccessTokenCookie);

            // 将用户重新认证到 SecurityContext
            authenticateUser(usersVO.getUsername()
                    , newAccessToken, request);

            // 继续执行后续过滤器链
            chain.doFilter(request, response);

        } catch (Exception e) {
            logger.error("Error during token refresh", e);
            respondWithError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to refresh token");
        }
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


    /**
     * 执行用户认证
     */
    private void authenticateUser(String username, String token, HttpServletRequest request) {
        UserDetails userDetails = userService.loadUserByUsername(username);

        if (jwtUtils.validateJwtToken(token)) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    /**
     * 返回错误响应
     */
    private void respondWithError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
