package com.example.usermanagement.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果类
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private T data;

    public Result() {}

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功");
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> success(String msg) {
        return new Result<>(200, msg);
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(200, msg, data);
    }

    public static <T> Result<T> error() {
        return new Result<>(500, "操作失败");
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(500, msg);
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg);
    }

    public boolean isSuccess() {
        return code == 200;
    }
}