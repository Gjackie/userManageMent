/**
 * 角色管理 API 接口封装
 *
 * 包含角色相关的所有 API 请求：
 * - getRoleList：获取角色列表
 * - createRole：创建角色
 * - updateRole：更新角色
 * - deleteRole：删除角色
 */

import request from './request'

/**
 * 获取角色列表
 *
 * @returns {Promise} 返回角色列表
 */
export const getRoleList = (params) => {
  return request.get('/roles/list', { params })
}

/**
 * 创建角色
 *
 * @param {Object} data 角色数据
 * @returns {Promise} 返回创建的角色信息
 */
export const createRole = (data) => {
  return request.post('/roles', data)
}

/**
 * 更新角色
 *
 * @param {Object} data 角色数据
 * @returns {Promise} 返回更新后的角色信息
 */
export const updateRole = (data) => {
  return request.put('/roles', data)
}

/**
 * 删除角色
 *
 * @param {number} id 角色ID
 * @returns {Promise} 返回操作结果
 */
export const deleteRole = (id) => {
  return request.delete(`/roles/${id}`)
}
