package com.billlv.codegenerator.exception.global;


import org.springframework.context.i18n.LocaleContextHolder;
import java.util.ResourceBundle;

/**
 * Enum for defining business error codes.
 * Error messages are loaded dynamically from i18n resources.
 */
public enum ErrorCode {

    // 公共错误码
    SYSTEM_ERROR(1000, "error.system"),
    INVALID_REQUEST(1001, "error.invalid_request"),
    UNAUTHORIZED(1002, "error.unauthorized"),

    // 用户模块错误码
    USER_NOT_FOUND(2001, "error.user_not_found"),
    USER_ALREADY_EXISTS(2002, "error.user_already_exists"),

    // 订单模块错误码
    ORDER_NOT_FOUND(3001, "error.order_not_found"),
    ORDER_CANNOT_BE_MODIFIED(3002, "error.order_cannot_be_modified");

    private final int code;
    private final String messageKey;

    ErrorCode(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public int getCode() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }

    /**
     * Get the localized error message.
     *
     * @return the error message based on the current locale
     */
    public String getLocalizedMessage() {
        return ResourceBundle
                .getBundle("messages", LocaleContextHolder.getLocale())
                .getString(this.messageKey);
    }
}
