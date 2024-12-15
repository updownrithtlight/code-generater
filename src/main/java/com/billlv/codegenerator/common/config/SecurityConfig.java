package com.billlv.codegenerator.common.config;
import com.billlv.codegenerator.common.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtRequestFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 禁用 CSRF
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 配置 CORS
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 允许所有请求（调试阶段使用）
                );

        return http.build();
    }



//    @Value("${cors.allowed-origins}")
//    private String[] allowedOrigins;
//
//    @Value("${cors.allowed-methods}")
//    private String[] allowedMethods;
//
//    @Value("${cors.allowed-headers}")
//    private String allowedHeaders;
//
//    @Value("${cors.allow-credentials}")
//    private boolean allowCredentials;
//    @Value("${cors.allow-max-age}")
//    private long allowMaxAge;
    /**
     * 配置 CORS
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 允许的前端域名
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 允许的方法
        config.setAllowedHeaders(Arrays.asList("*")); // 允许的请求头
        config.setAllowCredentials(true); // 允许携带凭据（如 Cookie）

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 对所有路径生效
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 使用 BCrypt 作为密码加密算法
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
