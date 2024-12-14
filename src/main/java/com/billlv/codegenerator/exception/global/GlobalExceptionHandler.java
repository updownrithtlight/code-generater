package com.billlv.codegenerator.exception.global;

import com.billlv.codegenerator.common.utils.MessageUtil;
import com.billlv.codegenerator.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



/**
 * Global exception handler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle custom business exceptions.
     */
    @ExceptionHandler(BaseException.class)
    public Result<?> handleBaseException(BaseException ex) {
        return Result.failure(ex.getCode(), ex.getLocalizedMessage());
    }

    /**
     * Handle unexpected exceptions.
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception ex) {
        return Result.failure(
                ErrorCode.SYSTEM_ERROR.getCode(),
                ErrorCode.SYSTEM_ERROR.getLocalizedMessage()
        );
    }
}
