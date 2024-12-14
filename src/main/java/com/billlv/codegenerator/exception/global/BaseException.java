package com.billlv.codegenerator.exception.global;


import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ResourceBundle;

/**
 * Base exception for handling business errors.
 * Supports error codes and internationalized error messages.
 */
public class BaseException extends RuntimeException {

    private final int code; // 错误码
    private final String messageKey; // 国际化资源文件的键
    private final String customMessage; // 自定义消息（可选）

    /**
     * Constructor for BaseException with default message from ErrorCode.
     *
     * @param errorCode the error code enum
     */
    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessageKey());
        this.code = errorCode.getCode();
        this.messageKey = errorCode.getMessageKey();
        this.customMessage = null;
    }

    /**
     * Constructor for BaseException with a custom message.
     *
     * @param errorCode     the error code enum
     * @param customMessage custom error message
     */
    public BaseException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.code = errorCode.getCode();
        this.messageKey = errorCode.getMessageKey();
        this.customMessage = customMessage;
    }

    public int getCode() {
        return code;
    }

    /**
     * Get the localized error message.
     *
     * @return the localized error message or the custom message if provided
     */
    public String getLocalizedMessage() {
        if (customMessage != null) {
            return customMessage; // 返回自定义消息
        }

        // 动态加载国际化消息
        ResourceBundle bundle = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
        return bundle.containsKey(messageKey) ? bundle.getString(messageKey) : messageKey;
    }
}
