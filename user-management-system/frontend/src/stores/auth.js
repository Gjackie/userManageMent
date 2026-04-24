import { defineStore } from 'pinia'
import { login as loginApi, getUserInfo } from '../api/user'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: JSON.parse(localStorage.getItem('user') || '{}')
  }),
  actions: {
    async login(username, password) {
      const res = await loginApi({ username, password })
      if (res.code === 200) {
        this.token = res.data.token
        this.user = {
          id: res.data.userId,
          username: res.data.username
        }
        localStorage.setItem('token', this.token)
        localStorage.setItem('user', JSON.stringify(this.user))
        return true
      }
      return false
    },
    async fetchUserInfo() {
      try {
        const res = await getUserInfo()
        if (res.code === 200) {
          this.user = res.data
          localStorage.setItem('user', JSON.stringify(this.user))
        }
      } catch (e) {
        console.error(e)
      }
    },
    logout() {
      this.token = ''
      this.user = {}
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
})
