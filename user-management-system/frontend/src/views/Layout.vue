<template>
  <!-- 整体布局容器 - 使用 Element Plus 的 Container 组件 -->
  <el-container class="layout-container">

    <!-- 左侧侧边栏 -->
    <el-aside width="200px">

      <!-- 系统 Logo / 标题 -->
      <div class="logo">用户管理系统</div>

      <!-- 导航菜单 -->
      <!--
        el-menu: Element Plus 菜单组件
        :default-active="$route.path" - 高亮当前路由对应的菜单
        router - 启用 Vue Router 模式，点击菜单自动跳转
      -->
      <el-menu
        :default-active="$route.path"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <!-- 首页菜单项 -->
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>

        <!-- 用户管理菜单项 -->
        <el-menu-item index="/user">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>

        <!-- 角色管理菜单项 -->
        <el-menu-item index="/role">
          <el-icon><UserFilled /></el-icon>
          <span>角色管理</span>
        </el-menu-item>

        <!-- 菜单管理菜单项 -->
        <el-menu-item index="/menu">
          <el-icon><Menu /></el-icon>
          <span>菜单管理</span>
        </el-menu-item>

        <!-- 日志管理菜单项 -->
        <el-menu-item index="/log">
          <el-icon><Document /></el-icon>
          <span>日志管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区域 -->
    <el-container>

      <!-- 顶部 Header -->
      <el-header>
        <div class="header-content">
          <!-- 显示当前登录用户名 -->
          <span class="username">{{ authStore.user.username }}</span>

          <!-- 退出登录按钮 -->
          <el-button type="danger" size="small" @click="handleLogout">
            退出
          </el-button>
        </div>
      </el-header>

      <!-- 主内容区 - 渲染子路由页面 -->
      <el-main>
        <!--
          router-view: Vue Router 的路由视图组件
          用于渲染匹配到的子路由页面
        -->
        <router-view />
      </el-main>

    </el-container>
  </el-container>
</template>

<script setup>
/**
 * 布局组件
 *
 * 功能：
 * 1. 提供整体页面布局（侧边栏 + 顶部 + 主内容）
 * 2. 渲染顶部导航菜单
 * 3. 处理用户登出逻辑
 *
 * 布局结构：
 * +------------------------------------------+
 * |  Logo  |         顶部 Header              |
 * |--------|---------------------------------|
 * |        |                                 |
 * |  侧边栏  |         主内容区域              |
 * |  菜单   |      <router-view>             |
 * |        |                                 |
 * +------------------------------------------+
 */

// ==================== 导入部分 ====================

// Vue Router 钩子
import { useRouter } from 'vue-router'

// Element Plus 图标组件
import { HomeFilled, User, UserFilled, Menu, Document } from '@element-plus/icons-vue'

// Pinia 认证 Store
import { useAuthStore } from '../stores/auth'

// ==================== 组件逻辑 ====================

// 获取路由实例
const router = useRouter()

// 获取认证 Store
const authStore = useAuthStore()

/**
 * 处理退出登录
 *
 * 流程：
 * 1. 调用 store 的 logout 方法清除登录状态
 * 2. 跳转到登录页面
 */
const handleLogout = () => {
  // 清除登录状态
  authStore.logout()

  // 跳转到登录页
  router.push('/login')
}
</script>

<style scoped>
/* scoped 样式，仅作用于当前组件 */

/* 整体布局容器 - 占满整个视口 */
.layout-container {
  height: 100vh;  /* 100% 视口高度 */
}

/* 侧边栏样式 */
.el-aside {
  background-color: #304156;  /* 深灰蓝色 */
}

/* Logo 样式 */
.logo {
  height: 60px;              /* 固定高度 */
  line-height: 60px;         /* 垂直居中 */
  text-align: center;        /* 水平居中 */
  color: #fff;               /* 白色文字 */
  font-size: 18px;          /* 字号 */
  font-weight: bold;         /* 加粗 */
  background-color: #263445; /* 稍深的背景色 */
}

/* 顶部 Header 样式 */
.el-header {
  background-color: #fff;    /* 白色背景 */
  box-shadow: 0 1px 4px rgba(0,0,0,0.1); /* 底部阴影 */
  display: flex;             /* Flex 布局 */
  align-items: center;       /* 垂直居中 */
}

/* Header 内容区 */
.header-content {
  width: 100%;               /* 占满宽度 */
  display: flex;             /* Flex 布局 */
  justify-content: flex-end; /* 右对齐 */
  align-items: center;       /* 垂直居中 */
  gap: 15px;                 /* 元素间距 */
}

/* 用户名样式 */
.username {
  color: #333;               /* 深灰色 */
}

/* 主内容区样式 */
.el-main {
  background-color: #f0f2f5; /* 浅灰背景 */
}
</style>
