package com.example.usermanagement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信配置类
 *
 * 用于读取 application.yml 中微信相关的配置。
 *
 * 配置项：
 * - wechat.appid：微信应用 AppID
 * - wechat.appsecret：微信应用 AppSecret
 * - wechat.callbackUrl：回调地址
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wechat")
public class WechatConfig {

    /**
     * 微信应用 AppID
     */
    private String appid;

    /**
     * 微信应用 AppSecret
     */
    private String appsecret;

    /**
     * 授权回调地址
     */
    private String callbackUrl;
}
