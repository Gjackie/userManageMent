import request from './auth'

export const getRoleList = (params) => {
  return request.get('/roles/list')
}

export const createRole = (data) => {
  return request.post('/roles', data)
}

export const updateRole = (data) => {
  return request.put('/roles', data)
}

export const deleteRole = (id) => {
  return request.delete(`/roles/${id}`)
}
