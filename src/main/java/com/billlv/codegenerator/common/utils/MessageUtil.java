package com.billlv.codegenerator.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 工具类，用于获取国际化消息。
 */
@Component
public class MessageUtil {

    @Autowired
    private MessageSource messageSource;

    /**
     * 获取国际化消息。
     *
     * @param code  消息代码
     * @param args  消息参数（可选）
     * @param locale 语言环境
     * @return 国际化消息
     */
    public String getMessage(String code, Object[] args, Locale locale) {
        return messageSource.getMessage(code, args, locale);
    }

    /**
     * 获取默认语言的国际化消息。
     *
     * @param code 消息代码
     * @return 国际化消息
     */
    public String getMessage(String code) {
        return getMessage(code, null, Locale.getDefault());
    }
}
