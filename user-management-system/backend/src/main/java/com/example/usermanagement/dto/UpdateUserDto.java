package com.example.usermanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 用户更新请求DTO
 */
@Data
public class UpdateUserDto {

    @NotNull(message = "用户ID不能为空")
    private Long id;

    private String username;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    private Long roleId;
}