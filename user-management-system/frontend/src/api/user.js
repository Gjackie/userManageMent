/**
 * 用户管理 API 接口封装
 *
 * 包含用户相关的所有 API 请求：
 * 1. 认证相关：登录、注册、获取用户信息
 * 2. 用户管理：列表、详情、创建、更新、删除
 *
 * 使用方式：
 * import { getUserList, createUser } from '@/api/user'
 */

import request from './request'

/**
 * 用户登录
 *
 * @param {Object} data 登录参数
 * @param {string} data.username 用户名
 * @param {string} data.password 密码
 * @returns {Promise} 返回 Token 和用户信息
 */
export const login = (data) => {
  return request.post('/auth/login', data)
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
 */
export const register = (data) => {
  return request.post('/auth/register', data)
}

/**
 * 获取当前登录用户信息
 *
 * @returns {Promise} 返回用户详细信息
 */
export const getUserInfo = () => {
  return request.get('/auth/info')
}

/**
 * 获取用户列表（分页）
 *
 * @param {Object} params 查询参数
 * @param {number} params.page 页码
 * @param {number} params.size 每页大小
 * @param {string} [params.username] 用户名（可选，用于筛选）
 * @returns {Promise} 返回分页后的用户列表
 */
export const getUserList = (params) => {
  return request.get('/users/list', {
    params: {
      pageNum: params.page,
      pageSize: params.size,
      username: params.username
    }
  })
}

/**
 * 创建用户
 *
 * @param {Object} data 用户数据
 * @param {string} data.username 用户名
 * @param {string} data.phone 手机号
 * @param {string} data.password 密码
 * @param {number} [data.roleId] 角色ID
 * @returns {Promise} 返回创建的用户信息
 */
export const createUser = (data) => {
  return request.post('/users', data)
}

/**
 * 更新用户
 *
 * @param {Object} data 用户数据
 * @param {number} data.id 用户ID
 * @param {string} [data.username] 用户名
 * @param {string} [data.phone] 手机号
 * @param {number} [data.roleId] 角色ID
 * @returns {Promise} 返回更新后的用户信息
 */
export const updateUser = (data) => {
  return request.put('/users', data)
}

/**
 * 删除用户
 *
 * @param {number} id 用户ID
 * @returns {Promise} 返回操作结果
 */
export const deleteUser = (id) => {
  return request.delete(`/users/${id}`)
}
