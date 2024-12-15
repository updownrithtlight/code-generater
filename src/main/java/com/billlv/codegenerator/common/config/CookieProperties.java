package com.billlv.codegenerator.common.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth.cookie")
public class CookieProperties {

    private TokenProperties accessToken;
    private TokenProperties refreshToken;

    public TokenProperties getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(TokenProperties accessToken) {
        this.accessToken = accessToken;
    }

    public TokenProperties getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(TokenProperties refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static class TokenProperties {
        private int maxAge; // 有效期（秒）
        private String path; // 生效路径
        private boolean httpOnly; // 是否 HttpOnly

        public int getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(int maxAge) {
            this.maxAge = maxAge;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public boolean isHttpOnly() {
            return httpOnly;
        }

        public void setHttpOnly(boolean httpOnly) {
            this.httpOnly = httpOnly;
        }
    }
}
