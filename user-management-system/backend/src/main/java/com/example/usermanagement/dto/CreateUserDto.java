package com.example.usermanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 创建用户请求数据传输对象
 *
 * 用于管理员创建新用户的接口。
 * 定义了创建用户时必需的字段和校验规则。
 *
 * 与 RegisterDto 的区别：
 * - CreateUserDto：管理员创建用户
 * - RegisterDto：用户自主注册
 *
 * 说明：
 * - 创建用户时可以指定角色（roleId）
 * - 密码由管理员或系统生成
 * - 不需要确认密码字段
 *
 * @see com.example.usermanagement.controller.UserController#create
 */
@Data
public class CreateUserDto {

    /**
     * 用户名
     * 登录账号，需唯一
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 手机号
     * 中国大陆手机号格式
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 密码
     * 初始密码，创建用户时设置
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 角色ID
     * - 1：管理员
     * - 2：普通用户
     *
     * 可选字段，如果为空则默认为普通用户
     */
    private Long roleId;
}
