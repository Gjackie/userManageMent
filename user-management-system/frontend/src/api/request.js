/**
 * Axios HTTP 请求封装
 *
 * 功能：
 * 1. 创建 Axios 实例，配置默认参数
 * 2. 添加请求拦截器，自动携带 Token
 * 3. 添加响应拦截器，统一处理错误
 *
 * 拦截器说明：
 * - 请求拦截器：在请求发送前执行，用于添加 Token
 * - 响应拦截器：在响应到达前执行，用于处理错误
 */

import axios from 'axios'

/**
 * 创建 Axios 实例
 *
 * baseURL：API 基础地址，所有请求都会拼接此前缀
 * timeout：请求超时时间（毫秒）
 */
const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000
})

/**
 * 请求拦截器
 *
 * 在每个请求发送前执行：
 * 1. 从 localStorage 获取 Token
 * 2. 如果存在 Token，添加到请求 Header
 * 3. 返回修改后的配置
 *
 * Token 格式：Bearer <token>
 * - Bearer 是 JWT Token 的标准前缀
 */
request.interceptors.request.use(config => {
  // 获取本地存储的 Token
  const token = localStorage.getItem('token')

  // 如果 Token 存在，添加到 Authorization Header
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }

  // 返回配置，继续发送请求
  return config
})

/**
 * 响应拦截器
 *
 * 处理两种情况：
 * 1. 正常响应：直接返回 response.data
 * 2. 错误响应：
 *    - 401 未授权：清除本地 Token，跳转登录页
 *    - 其他错误：拒绝 Promise，传递错误信息
 */
request.interceptors.response.use(
  // 成功响应：直接返回 data
  response => response.data,

  // 错误响应
  error => {
    // 检查是否是 401 错误（未登录或 Token 过期）
    if (error.response?.status === 401) {
      // 清除本地存储的认证信息
      localStorage.removeItem('token')
      localStorage.removeItem('user')

      // 跳转到登录页
      window.location.href = '/login'
    }

    // 拒绝 Promise，传递错误信息
    return Promise.reject(error)
  }
)

/**
 * 导出请求实例
 *
 * 其他模块引入此文件后，可以直接使用：
 * import request from '@/api/request'
 * request({ url: '/users', method: 'GET' })
 */
export default request
