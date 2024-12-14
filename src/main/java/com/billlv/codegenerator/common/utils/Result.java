package com.billlv.codegenerator.common.utils;

import lombok.Data;


@Data
public class Result<T> {
    private int code;
    private Boolean success;
    private String message;
    private T data;

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>(200, "success", data);
        result.setSuccess(Boolean.TRUE);
        return result;
    }

    public static <T> Result<T> failure(int code, String message) {
        Result<T> result = new Result<>(code, message, null);
        result.setSuccess(Boolean.FALSE);
        return result;
    }
}

