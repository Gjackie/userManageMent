package com.example.usermanagement.dto;

import lombok.Data;

/**
 * 微信登录数据传输对象
 *
 * 用于接收微信登录回调时携带的参数。
 */
@Data
public class WechatLoginDto {

    /**
     * 微信授权码
     * 用于换取 access_token 和 openid
     */
    private String code;

    /**
     * 状态值
     * 用于防止 CSRF 攻击
     */
    private String state;
}
