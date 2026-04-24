import request from './auth'

export const login = (data) => {
  return request.post('/auth/login', data)
}

export const register = (data) => {
  return request.post('/auth/register', data)
}

export const getUserInfo = () => {
  return request.get('/auth/info')
}

export const getUserList = (params) => {
  return request.get('/users/list', { params: { pageNum: params.page, pageSize: params.size, username: params.username } })
}

export const createUser = (data) => {
  return request.post('/users', data)
}

export const updateUser = (data) => {
  return request.put('/users', data)
}

export const deleteUser = (id) => {
  return request.delete(`/users/${id}`)
}
