package com.billlv.codegenerator.common.config;


import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * 配置国际化支持。
 */
@Configuration
public class InternationalizationConfig {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages"); // 基础文件名
        messageSource.setDefaultEncoding("UTF-8"); // 防止中文乱码
        messageSource.setUseCodeAsDefaultMessage(true); // 未找到消息时返回代码本身
        return messageSource;
    }
}
