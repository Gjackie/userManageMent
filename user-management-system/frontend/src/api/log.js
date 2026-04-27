/**
 * 日志管理 API 接口封装
 *
 * 包含日志相关的所有 API 请求：
 * - getLogList：获取日志列表（分页）
 */

import request from './request'

/**
 * 获取日志列表（分页）
 *
 * @param {Object} params 查询参数
 * @param {number} params.page 页码
 * @param {number} params.size 每页条数
 * @returns {Promise} 返回分页后的日志列表
 */
export const getLogList = (params) => {
  return request.get('/logs/list', {
    params: {
      pageNum: params.page,
      pageSize: params.size
    }
  })
}
