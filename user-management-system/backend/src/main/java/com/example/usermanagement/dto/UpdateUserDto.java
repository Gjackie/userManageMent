package com.example.usermanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 更新用户请求数据传输对象
 *
 * 用于更新用户信息的接口。
 *
 * 设计特点：
 * - @NotNull 标注的字段表示更新时必须提供
 * - 普通字段（如 username、phone）为可选，只更新有值的字段
 * - 密码不在此处更新，有单独的接口处理
 *
 * @see com.example.usermanagement.controller.UserController#update
 */
@Data
public class UpdateUserDto {

    /**
     * 用户ID
     * 必须提供，用于定位要更新的用户
     */
    @NotNull(message = "用户ID不能为空")
    private Long id;

    /**
     * 用户名（可选）
     * 只更新有值的字段
     */
    private String username;

    /**
     * 手机号（可选）
     * 中国大陆手机号格式
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 角色ID（可选）
     * 用于修改用户的角色
     */
    private Long roleId;
}
