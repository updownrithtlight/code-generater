package com.billlv.codegenerator.common.auditor;


import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorProvider implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // 使用 Spring Security 获取当前用户
        // 示例代码：获取当前登录用户（实际项目中替换为动态获取逻辑）
         String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return Optional.ofNullable(currentUser);
    }
}
