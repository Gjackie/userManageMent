package com.example.usermanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录请求数据传输对象
 *
 * DTO（Data Transfer Object）作用：
 * 1. 定义接口请求参数结构
 * 2. 进行参数校验（配合 @Valid 注解）
 * 3. 与实体类分离，保护数据库结构不被暴露
 *
 * 校验注解说明：
 * - @NotBlank：不能为空字符串（会对空格进行trim处理）
 * - message：校验失败时的提示信息
 *
 * 设计考虑：
 * - 只包含登录必需的字段（用户名、密码）
 * - 不包含敏感信息
 * - 验证码字段为可选，方便后续扩展
 *
 * 前端请求示例：
 * <pre>
 * {
 *     "username": "admin",
 *     "password": "admin123",
 *     "captcha": "1234",    // 可选
 *     "uuid": "xxx-xxx"     // 可选
 * }
 * </pre>
 */
@Data
public class LoginDto {

    /**
     * 用户名
     * 登录账号，唯一标识
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     * 用户密码（前端已加密或后端加密）
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 验证码（可选）
     * 用于图形验证码功能
     */
    private String captcha;

    /**
     * 验证码UUID（可选）
     * 用于关联验证码的唯一标识
     */
    private String uuid;
}
