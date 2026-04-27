package com.example.usermanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 用户注册请求数据传输对象
 *
 * 用于用户注册接口，验证用户提交的注册信息。
 *
 * 字段说明：
 * - username：登录账号，需唯一
 * - phone：手机号，用于联系或找回密码
 * - password：密码，需加密存储
 * - confirmPassword：确认密码，用于两次密码一致性校验
 *
 * 安全考虑：
 * - 密码应满足复杂度要求（本项目未做强制要求）
 * - 实际生产中应添加密码强度校验
 * - 确认密码仅用于校验，不存入数据库
 *
 * @see com.example.usermanagement.controller.AuthController#register
 */
@Data
public class RegisterDto {

    /**
     * 用户名
     * 登录账号，需唯一
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 手机号
     * 中国大陆手机号格式：1开头，11位数字
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 密码
     * 注册时的密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 确认密码
     * 必须与密码一致，用于防止输入错误
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}
