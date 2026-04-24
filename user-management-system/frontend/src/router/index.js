import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue')
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('../views/User.vue')
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('../views/Role.vue')
      },
      {
        path: 'menu',
        name: 'Menu',
        component: () => import('../views/Menu.vue')
      },
      {
        path: 'log',
        name: 'Log',
        component: () => import('../views/Log.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (!token && to.path !== '/login') {
    next('/login')
  } else {
    next()
  }
})

export default router
