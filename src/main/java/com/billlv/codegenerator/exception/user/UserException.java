package com.billlv.codegenerator.exception.user;


import com.billlv.codegenerator.exception.global.BaseException;
import com.billlv.codegenerator.exception.global.ErrorCode;

/**
 * User-related exceptions.
 */
public class UserException extends BaseException {

    /**
     * Constructor for UserException with default message from ErrorCode.
     *
     * @param errorCode the error code enum
     */
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Constructor for UserException with a custom message.
     *
     * @param errorCode     the error code enum
     * @param customMessage custom error message
     */
    public UserException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}
