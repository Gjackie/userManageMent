/**
 * 路由配置
 *
 * Vue Router 4 的路由配置文件。
 * 采用 Vue 3 Composition API 风格的路由创建方式。
 *
 * 路由设计说明：
 * - /login：登录页（公开访问）
 * - /user-auth：用户授权页（公开访问，用于微信扫码登录）
 * - /user-welcome：欢迎页（公开访问）
 * - /：布局组件，包含左侧菜单和顶部导航
 *   - /dashboard：仪表盘/首页
 *   - /user：用户管理
 *   - /role：角色管理
 *   - /menu：菜单管理
 *   - /log：操作日志
 *
 * 路由守卫：
 * - beforeEach：全局前置守卫
 * - 功能：检查是否存在 Token，无 Token 跳转到登录页
 * - 白名单：公开访问的路径不需要 Token
 *
 * 懒加载：
 * - 使用 () => import() 实现组件懒加载
 * - 好处：首屏加载更快，组件按需加载
 */

import { createRouter, createWebHistory } from 'vue-router'

/**
 * 路由配置数组
 * 每个路由对象包含：
 * - path：路由路径
 * - name：路由名称（用于编程式导航）
 * - component：视图组件
 * - children：子路由（嵌套路由）
 */
const routes = [
  /**
   * 登录页
   * 路由：/login
   */
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },

  /**
   * 用户授权页（微信扫码登录）
   * 路由：/user-auth
   */
  {
    path: '/user-auth',
    name: 'UserAuth',
    component: () => import('../views/UserAuth.vue')
  },

  /**
   * 欢迎页
   * 路由：/user-welcome
   */
  {
    path: '/user-welcome',
    name: 'UserWelcome',
    component: () => import('../views/UserWelcome.vue')
  },

  /**
   * 主布局路由
   * 路由：/
   * 包含左侧菜单和顶部导航的通用布局
   */
  {
    path: '/',
    component: () => import('../views/Layout.vue'),

    // 重定向到仪表盘
    redirect: '/dashboard',

    // 子路由（嵌套在主布局中）
    children: [
      /**
       * 仪表盘/首页
       * 路由：/dashboard
       */
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue')
      },

      /**
       * 用户管理
       * 路由：/user
       */
      {
        path: 'user',
        name: 'User',
        component: () => import('../views/User.vue')
      },

      /**
       * 角色管理
       * 路由：/role
       */
      {
        path: 'role',
        name: 'Role',
        component: () => import('../views/Role.vue')
      },

      /**
       * 菜单管理
       * 路由：/menu
       */
      {
        path: 'menu',
        name: 'Menu',
        component: () => import('../views/Menu.vue')
      },

      /**
       * 操作日志
       * 路由：/log
       */
      {
        path: 'log',
        name: 'Log',
        component: () => import('../views/Log.vue')
      }
    ]
  }
]

/**
 * 创建路由实例
 *
 * createRouter：创建路由实例
 * createWebHistory：使用 HTML5 History 模式
 *   - URL 格式：`/user/123`（不带井号）
 *   - 需要服务器配置，支持路由刷新
 */
const router = createRouter({
  history: createWebHistory(),
  routes
})

/**
 * 全局前置路由守卫
 *
 * 在路由跳转前进行权限检查。
 * 检查用户是否已登录（通过 localStorage 中的 Token）。
 *
 * @param to 目标路由对象
 * @param from 来源路由对象
 * @param next 导航控制函数
 *   - next()：继续导航
 *   - next('/login')：跳转到指定路径
 */
router.beforeEach((to, from, next) => {
  // 从 localStorage 获取 Token
  const token = localStorage.getItem('token')

  // 公开访问路径白名单（不需要登录即可访问）
  const publicPaths = ['/login', '/user-auth', '/user-welcome']

  // 检查是否需要登录
  if (!token && !publicPaths.includes(to.path)) {
    // 未登录且不是公开页面，跳转到登录页
    next('/login')
  } else {
    // 已登录或公开页面，允许访问
    next()
  }
})

export default router
