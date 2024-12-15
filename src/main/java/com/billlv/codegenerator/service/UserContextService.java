package com.billlv.codegenerator.service;
import com.billlv.codegenerator.common.utils.RedisUtility;
import com.billlv.codegenerator.domain.vo.UsersVO;
import com.billlv.codegenerator.service.impl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class UserContextService {

    @Autowired
    private UsersService usersService;
    @Autowired
    private RedisUtility<UsersVO> redisUtility; // 使用 Redis 缓存
    private static final String REDIS_USER_PREFIX = "USER_CONTEXT:"; // Redis 缓存键前缀
    private static final long CACHE_EXPIRE_TIME = 3600; // 缓存过期时间（分钟）

    /**
     * 获取当前用户的 Authentication 对象
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前用户的 userId（从 principal 提取）
     */
    public Long getCurrentUserId() {
        Authentication authentication = getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            // 根据认证类型提取 userId
            if (principal instanceof Long) {
                return (Long) principal; // 如果 userId 是 String 类型
            } else if (principal instanceof UserDetailsImpl) {
                return ((UserDetailsImpl) principal).getUserId(); // 如果是 UserDetails，取用户名
            }
        }
        throw new RuntimeException("未认证的用户");
    }

    /**
     * 获取当前用户的完整信息，使用 Redis 缓存优化
     */
    public UsersVO getCurrentUser() {
        Long userId = getCurrentUserId();
        String redisKey = REDIS_USER_PREFIX + userId;

        // 从 Redis 缓存中获取用户信息
        UsersVO cachedUser = redisUtility.get(redisKey);
        if (cachedUser != null) {
            return cachedUser;
        }
        // 缓存未命中，加载用户信息并缓存
        UsersVO user = loadUserFromDatabase(userId);
        redisUtility.save(redisKey, user, CACHE_EXPIRE_TIME); // 设置过期时间
        return user;
    }

    /**
     * 检查当前用户是否已认证
     */
    public boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    /**
     * 模拟从数据库加载用户信息的方法
     */
    private UsersVO loadUserFromDatabase(Long userId) {
        return usersService.read(userId);
    }

    /**
     * 删除 Redis 缓存（例如用户信息更新时调用）
     */
    public void evictUserCache(String userId) {
        String redisKey = REDIS_USER_PREFIX + userId;
        redisUtility.delete(redisKey);
    }
}
