/**
 * 认证相关 API 接口
 *
 * 封装与后端认证相关的 HTTP 请求。
 *
 * 接口列表：
 * - login：用户登录
 * - getUserInfo：获取当前用户信息
 * - register：用户注册
 * - getWechatQrCode：获取微信登录二维码
 * - wechatCallback：微信登录回调
 *
 * 使用方式：
 * import { login, getUserInfo } from '@/api/auth'
 * const result = await login(data)
 */

import request from './request'

/**
 * 用户登录
 *
 * @param {Object} data 登录参数
 * @param {string} data.username 用户名
 * @param {string} data.password 密码
 * @returns {Promise} 返回 Token 和用户信息
 *
 * 请求示例：
 * login({ username: 'admin', password: 'admin123' })
 *
 * 响应示例：
 * {
 *   code: 200,
 *   msg: '登录成功',
 *   data: {
 *     token: 'eyJhbGciOiJIUzI1NiIs...',
 *     userId: 1,
 *     username: 'admin'
 *   }
 * }
 */
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'POST',
    data
  })
}

/**
 * 获取当前登录用户信息
 *
 * 需要在请求 Header 中携带 Token。
 *
 * @returns {Promise} 返回用户详细信息
 *
 * 响应示例：
 * {
 *   code: 200,
 *   msg: '操作成功',
 *   data: {
 *     id: 1,
 *     username: 'admin',
 *     phone: '13800138000',
 *     roleId: 1
 *   }
 * }
 */
export function getUserInfo() {
  return request({
    url: '/auth/info',
    method: 'GET'
  })
}

/**
 * 用户注册
 *
 * @param {Object} data 注册参数
 * @param {string} data.username 用户名
 * @param {string} data.phone 手机号
 * @param {string} data.password 密码
 * @param {string} data.confirmPassword 确认密码
 * @returns {Promise} 返回 Token 和用户信息
 *
 * 请求示例：
 * register({
 *   username: 'test',
 *   phone: '13800138000',
 *   password: 'xxx',
 *   confirmPassword: 'xxx'
 * })
 */
export function register(data) {
  return request({
    url: '/auth/register',
    method: 'POST',
    data
  })
}

/**
 * 获取微信登录二维码
 *
 * @returns {Promise} 返回二维码 URL 和场景值
 *
 * 响应示例：
 * {
 *   code: 200,
 *   data: {
 *     qrcodeUrl: 'weixin://...',
 *     sceneStr: 'xxx'
 *   }
 * }
 */
export function getWechatQrCode() {
  return request({
    url: '/auth/wechat/qrcode',
    method: 'GET'
  })
}

/**
 * 微信登录回调
 *
 * @param {string} code 微信授权码
 * @param {string} state 场景值
 * @returns {Promise} 返回登录结果
 */
export function wechatCallback(code, state) {
  return request({
    url: '/auth/wechat/callback',
    method: 'GET',
    params: { code, state }
  })
}
