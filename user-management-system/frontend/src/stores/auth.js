/**
 * 认证状态管理（Pinia Store）
 *
 * Pinia 是 Vue 3 的官方状态管理库，简化版的 Vuex。
 *
 * 职责：
 * 1. 管理全局认证状态（token、user）
 * 2. 提供登录、登出、获取用户信息等 actions
 * 3. 状态持久化到 localStorage
 *
 * 状态说明：
 * - token：JWT 认证令牌
 * - user：当前登录用户信息
 *
 * 使用示例：
 * <script setup>
 * import { useAuthStore } from '@/stores/auth'
 * const authStore = useAuthStore()
 *
 * // 访问状态
 * authStore.token
 * authStore.user
 *
 * // 调用 action
 * await authStore.login('admin', 'xxx')
 * authStore.logout()
 * </script>
 */

import { defineStore } from 'pinia'
import { login as loginApi, getUserInfo } from '../api/user'

/**
 * 定义认证 Store
 *
 * defineStore 是 Pinia 的核心函数，
 * 用于创建一个 store 实例。
 */
export const useAuthStore = defineStore('auth', {
  // ==================== 状态（State） ====================

  /**
   * 状态定义
   *
   * 从 localStorage 读取初始值，实现页面刷新后状态不丢失。
   */
  state: () => ({
    /**
     * JWT 认证令牌
     * 登录成功后从后端获取，存储在 localStorage
     */
    token: localStorage.getItem('token') || '',

    /**
     * 当前登录用户信息
     * 包含 id, username, phone, roleId 等
     */
    user: JSON.parse(localStorage.getItem('user') || '{}')
  }),

  // ==================== 操作（Actions） ====================

  /**
   * Actions - 同步或异步操作
   *
   * 定义修改状态的方法。
   * 在 actions 中可以：
   * - 访问 this 获取状态
   * - 调用 API
   * - 修改 state
   */
  actions: {

    /**
     * 用户登录
     *
     * @param {string} username 用户名
     * @param {string} password 密码
     * @returns {boolean} true-登录成功，false-登录失败
     */
    async login(username, password) {
      // 调用登录 API
      const res = await loginApi({ username, password })

      // 判断登录是否成功
      if (res.code === 200) {
        // 保存 Token 到状态
        this.token = res.data.token

        // 保存用户信息到状态
        this.user = {
          id: res.data.userId,
          username: res.data.username
        }

        // 持久化到 localStorage
        localStorage.setItem('token', this.token)
        localStorage.setItem('user', JSON.stringify(this.user))

        return true
      }

      return false
    },

    /**
     * 获取当前用户详细信息
     *
     * 从后端获取完整的用户信息，
     * 并更新到 store 和 localStorage。
     *
     * 使用场景：
     * - 登录后获取用户信息
     * - 页面刷新后更新用户信息
     */
    async fetchUserInfo() {
      try {
        const res = await getUserInfo()
        if (res.code === 200) {
          this.user = res.data
          localStorage.setItem('user', JSON.stringify(this.user))
        }
      } catch (e) {
        console.error('获取用户信息失败', e)
      }
    },

    /**
     * 用户登出
     *
     * 清除本地存储的所有认证信息。
     * 注意：不会调用后端登出接口（JWT 无状态，无需后端处理）。
     */
    logout() {
      // 清除状态
      this.token = ''
      this.user = {}

      // 清除 localStorage
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
})
