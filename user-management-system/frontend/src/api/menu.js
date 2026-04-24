import request from './auth'

export const getMenuList = (params) => {
  return request.get('/menus/list')
}

export const createMenu = (data) => {
  return request.post('/menus', data)
}

export const updateMenu = (data) => {
  return request.put('/menus', data)
}

export const deleteMenu = (id) => {
  return request.delete(`/menus/${id}`)
}
