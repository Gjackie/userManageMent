import request from './auth'

export const getLogList = (params) => {
  return request.get('/logs/list', { params: { pageNum: params.page, pageSize: params.size } })
}
