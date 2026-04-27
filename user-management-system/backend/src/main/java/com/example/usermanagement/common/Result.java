package com.example.usermanagement.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果封装类
 *
 * 前后端交互的标准响应格式，所有 API 接口统一返回此格式的数据。
 *
 * 响应结构说明：
 * - code：状态码，200 表示成功，其他表示异常
 * - msg：消息，描述响应结果
 * - data：数据，接口返回的业务数据（可选）
 *
 * 状态码规范：
 * - 200：操作成功
 * - 400：请求参数错误
 * - 401：未登录或登录过期
 * - 403：没有权限访问
 * - 500：服务器内部错误
 *
 * 使用示例：
 * <pre>
 * // 返回成功（带数据）
 * return Result.success(user);
 *
 * // 返回成功（自定义消息）
 * return Result.success("登录成功", token);
 *
 * // 返回错误
 * return Result.error("用户名或密码错误");
 * return Result.error(400, "参数校验失败");
 * </pre>
 *
 * @param <T> data 字段的数据类型
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     * - 200：成功
     * - 其他值：表示各种错误状态
     */
    private int code;

    /**
     * 响应消息
     * 用于描述响应结果，如："操作成功"、"用户名已存在"等
     */
    private String msg;

    /**
     * 响应数据
     * 泛型字段，可以是任意类型的数据
     * 如：用户信息、列表、分页数据等
     */
    private T data;

    /**
     * 默认构造函数
     */
    public Result() {}

    /**
     * 构造函数
     *
     * @param code 状态码
     * @param msg 消息
     */
    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 构造函数
     *
     * @param code 状态码
     * @param msg 消息
     * @param data 数据
     */
    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // ==================== 成功响应 ====================

    /**
     * 返回成功（无数据）
     * 默认状态码 200，消息 "操作成功"
     *
     * @param <T> 泛型
     * @return Result 对象
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功");
    }

    /**
     * 返回成功（带数据）
     * 默认状态码 200，消息 "操作成功"
     *
     * @param data 响应数据
     * @param <T> 数据类型
     * @return Result 对象
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /**
     * 返回成功（自定义消息）
     * 默认状态码 200
     *
     * @param msg 自定义消息
     * @param <T> 泛型
     * @return Result 对象
     */
    public static <T> Result<T> success(String msg) {
        return new Result<>(200, msg);
    }

    /**
     * 返回成功（自定义消息和数据）
     *
     * @param msg 自定义消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return Result 对象
     */
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(200, msg, data);
    }

    // ==================== 错误响应 ====================

    /**
     * 返回错误（默认状态码 500）
     * 默认状态码 500，消息 "操作失败"
     *
     * @param <T> 泛型
     * @return Result 对象
     */
    public static <T> Result<T> error() {
        return new Result<>(500, "操作失败");
    }

    /**
     * 返回错误（自定义消息）
     * 默认状态码 500
     *
     * @param msg 错误消息
     * @param <T> 泛型
     * @return Result 对象
     */
    public static <T> Result<T> error(String msg) {
        return new Result<>(500, msg);
    }

    /**
     * 返回错误（自定义状态码和消息）
     *
     * @param code 自定义状态码
     * @param msg 错误消息
     * @param <T> 泛型
     * @return Result 对象
     */
    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg);
    }

    // ==================== 辅助方法 ====================

    /**
     * 判断请求是否成功
     *
     * @return true-成功，false-失败
     */
    public boolean isSuccess() {
        return code == 200;
    }
}
