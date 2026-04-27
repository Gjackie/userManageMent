package com.example.usermanagement.service;

import com.example.usermanagement.entity.SysUser;

import java.util.Map;

/**
 * 微信登录服务接口
 *
 * 定义微信登录相关的业务逻辑方法。
 *
 * @see com.example.usermanagement.service.impl.WechatLoginServiceImpl
 */
public interface WechatLoginService {

    /**
     * 获取微信登录二维码链接
     *
     * @return 包含二维码URL和state的Map
     */
    Map<String, String> getQrCodeUrl();

    /**
     * 微信登录回调处理
     *
     * @param code 授权码
     * @return 登录结果
     */
    Map<String, Object> callback(String code);
}
