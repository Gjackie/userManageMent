/**
 * 菜单管理 API 接口封装
 *
 * 包含菜单相关的所有 API 请求：
 * - getMenuList：获取菜单列表
 * - createMenu：创建菜单
 * - updateMenu：更新菜单
 * - deleteMenu：删除菜单
 */

import request from './request'

/**
 * 获取菜单列表
 *
 * @returns {Promise} 返回菜单列表
 */
export const getMenuList = (params) => {
  return request.get('/menus/list', { params })
}

/**
 * 创建菜单
 *
 * @param {Object} data 菜单数据
 * @returns {Promise} 返回创建的菜单信息
 */
export const createMenu = (data) => {
  return request.post('/menus', data)
}

/**
 * 更新菜单
 *
 * @param {Object} data 菜单数据
 * @returns {Promise} 返回更新后的菜单信息
 */
export const updateMenu = (data) => {
  return request.put('/menus', data)
}

/**
 * 删除菜单
 *
 * @param {number} id 菜单ID
 * @returns {Promise} 返回操作结果
 */
export const deleteMenu = (id) => {
  return request.delete(`/menus/${id}`)
}
