package com.billlv.codegenerator.common.filter;

import com.billlv.codegenerator.common.utils.JwtUtils;
import com.billlv.codegenerator.domain.vo.UsersVO;
import com.billlv.codegenerator.service.UsersService;
import com.billlv.codegenerator.service.impl.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

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
            String userId = jwtUtils.getUserIdFromToken(token);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
                authenticateUser(userId, token, request);
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
            respondWithError(response, HttpServletResponse.SC_UNAUTHORIZED, "Unexpected error");
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * 检查是否是无需验证的路径
     */
    private boolean isExcludedPath(String path) {
        return "/api/auth/login".equals(path)|| "/api/auth/refresh-token".equals(path) || "/api/auth/register".equals(path);
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
            String userId = jwtUtils.getUserIdFromToken(refreshToken);
//            // 加载用户信息
//            UserDetails userDetails = userService.loadUserByUsername(usersVO.getUsername());

            // 生成新的 Access Token
            String newAccessToken = jwtUtils.generateAccessToken(userId);

            // 设置新的 Access Token 到 Cookie
            Cookie newAccessTokenCookie = new Cookie("accessToken", newAccessToken);
            newAccessTokenCookie.setHttpOnly(true);
            newAccessTokenCookie.setSecure(true); // 在 HTTPS 环境下使用
            newAccessTokenCookie.setPath("/");
            newAccessTokenCookie.setMaxAge(180);
            response.addCookie(newAccessTokenCookie);

            // 将用户重新认证到 SecurityContext
            authenticateUser(userId
                    , newAccessToken, request);

            // 继续执行后续过滤器链
            chain.doFilter(request, response);

        } catch (Exception e) {
            logger.error("Error during token refresh", e);
            respondWithError(response, HttpServletResponse.SC_UNAUTHORIZED, "Unable to refresh token");
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
     * 执行用户认证（仅基于 userId）
     */
    private void authenticateUser(String userId, String token, HttpServletRequest request) {
        // 验证 Token 的合法性
        if (jwtUtils.validateJwtToken(token)) {
            // 从数据库或缓存加载用户信息
            UsersVO usersVO = userService.read(Long.parseLong(userId));

            UserDetailsImpl userDetails = new UserDetailsImpl(
                    usersVO.getId(),
                    usersVO.getUsername(),
                    "", // 密码为空
                    List.of() // 空权限列表
            );
            // 构建认证对象
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 将认证信息存入 SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
//
//
//    private void authenticateUser(String userId, String token, HttpServletRequest request) {
//        if (jwtUtils.validateJwtToken(token)) {
//            // 加载用户信息（从数据库或缓存中）
//            UsersVO usersVO = userService.read(Long.parseLong(userId));
//            List<GrantedAuthority> authorities = usersVO.getRoles().stream()
//                    .map(SimpleGrantedAuthority::new)
//                    .collect(Collectors.toList());
//
//            // 构建 UserDetails 对象（可选）
//            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
//                    usersVO.getUsername(),
//                    "",
//                    authorities
//            );
//
//            // 构建认证对象
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                    userDetails, null, authorities);
//            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//            // 设置认证信息到 SecurityContext
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//    }



    /**
     * 返回错误响应
     */
    private void respondWithError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
