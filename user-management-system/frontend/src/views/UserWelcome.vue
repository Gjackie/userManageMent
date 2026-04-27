<template>
  <!-- 欢迎页面容器 -->
  <div class="welcome-container">

    <!-- 顶部欢迎区域 -->
    <div class="welcome-header">
      <div class="user-info">
        <!-- 用户头像 -->
        <div class="avatar">
          <el-icon :size="48"><User /></el-icon>
        </div>
        <!-- 用户信息 -->
        <div class="info">
          <h2>欢迎回来，{{ userInfo.username }}</h2>
          <p class="subtitle">普通用户中心</p>
        </div>
      </div>
      <!-- 退出登录按钮 -->
      <el-button type="danger" @click="handleLogout">退出登录</el-button>
    </div>

    <!-- 主体内容区域 -->
    <div class="welcome-content">

      <!-- 第一行：基本信息、功能菜单、快捷操作 -->
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="welcome-card">
            <template #header>
              <div class="card-header">
                <span>基本信息</span>
              </div>
            </template>
            <div class="info-list">
              <div class="info-item">
                <span class="label">用户名：</span>
                <span class="value">{{ userInfo.username || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">手机号：</span>
                <span class="value">{{ userInfo.phone || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">用户ID：</span>
                <span class="value">{{ userInfo.id || '-' }}</span>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card class="welcome-card">
            <template #header>
              <div class="card-header">
                <span>功能菜单</span>
              </div>
            </template>
            <div class="menu-list">
              <div class="menu-item">
                <el-icon><Document /></el-icon>
                <span>个人信息管理</span>
              </div>
              <div class="menu-item">
                <el-icon><Setting /></el-icon>
                <span>账户设置</span>
              </div>
              <div class="menu-item">
                <el-icon><Lock /></el-icon>
                <span>修改密码</span>
              </div>
              <div class="menu-item">
                <el-icon><Message /></el-icon>
                <span>消息通知</span>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card class="welcome-card">
            <template #header>
              <div class="card-header">
                <span>快捷操作</span>
              </div>
            </template>
            <div class="action-list">
              <el-button type="primary" plain>查看订单</el-button>
              <el-button type="success" plain>我的收藏</el-button>
              <el-button type="warning" plain>浏览记录</el-button>
              <el-button type="info" plain>意见反馈</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 第二行：系统公告 -->
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="24">
          <el-card class="welcome-card">
            <template #header>
              <div class="card-header">
                <span>系统公告</span>
              </div>
            </template>
            <div class="announcement-list">
              <div class="announcement-item">
                <span class="tag tag-new">新</span>
                <span class="title">系统上线通知</span>
                <span class="date">2026-04-25</span>
              </div>
              <div class="announcement-item">
                <span class="title">关于用户隐私保护的说明</span>
                <span class="date">2026-04-20</span>
              </div>
              <div class="announcement-item">
                <span class="title">新增微信扫码登录功能</span>
                <span class="date">2026-04-15</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
/**
 * 用户欢迎页面组件
 *
 * 功能：
 * 1. 展示用户基本信息
 * 2. 展示功能菜单入口
 * 3. 提供快捷操作按钮
 * 4. 展示系统公告
 * 5. 用户登出功能
 */

import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Document, Setting, Lock, Message } from '@element-plus/icons-vue'
import { getUserInfo } from '../api/user'

const router = useRouter()

/** 用户信息 */
const userInfo = ref({
  id: '',
  username: '',
  phone: ''
})

/**
 * 组件挂载时执行
 * 从 localStorage 获取基本信息，然后调用 API 获取完整信息
 */
onMounted(async () => {
  // 从 localStorage 获取基本信息
  const storedUser = localStorage.getItem('user')
  if (storedUser) {
    const user = JSON.parse(storedUser)
    userInfo.value.id = user.id
    userInfo.value.username = user.username
  }

  // 调用 API 获取完整用户信息
  try {
    const res = await getUserInfo()
    if (res.code === 200) {
      userInfo.value.phone = res.data.phone
    }
  } catch (e) {
    console.error(e)
  }
})

/**
 * 处理退出登录
 */
const handleLogout = () => {
  // 清除 localStorage
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  ElMessage.success('已退出登录')
  // 跳转到用户认证页
  router.push('/user-auth')
}
</script>

<style scoped>
.welcome-container {
  min-height: 100vh;
  background: #f5f7fa;
  padding: 20px;
}

.welcome-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  padding: 20px 30px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  color: #fff;
}

.info h2 {
  margin: 0 0 5px 0;
  font-size: 24px;
  color: #333;
}

.subtitle {
  margin: 0;
  color: #999;
  font-size: 14px;
}

.welcome-content {
  padding: 0 5px;
}

.welcome-card {
  height: 100%;
}

.card-header {
  font-weight: 600;
  font-size: 16px;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.info-item {
  display: flex;
  align-items: center;
}

.info-item .label {
  color: #666;
  min-width: 70px;
}

.info-item .value {
  color: #333;
  font-weight: 500;
}

.menu-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.3s;
}

.menu-item:hover {
  background: #f5f7fa;
}

.menu-item span {
  color: #333;
}

.action-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.announcement-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.announcement-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.3s;
}

.announcement-item:hover {
  background: #f5f7fa;
}

.tag {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.tag-new {
  background: #f56c6c;
  color: #fff;
}

.announcement-item .title {
  flex: 1;
  color: #333;
}

.announcement-item .date {
  color: #999;
  font-size: 12px;
}
</style>
