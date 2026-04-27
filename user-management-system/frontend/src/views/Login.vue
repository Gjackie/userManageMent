<template>
  <!-- 登录页面容器 - 全屏居中布局 -->
  <div class="login-container">
    <!-- 登录卡片 -->
    <el-card class="login-card">
      <!-- 卡片头部 -->
      <template #header>
        <h2>用户管理系统</h2>
      </template>

      <!-- 登录表单 -->
      <el-form
        :model="form"
        :rules="rules"
        ref="formRef"
        label-width="80px"
      >
        <!-- 用户名输入框 -->
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
          />
        </el-form-item>

        <!-- 密码输入框 -->
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <!-- 登录按钮 -->
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            style="width: 100%"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>

        <!-- 提示信息 -->
        <div class="tips">
          <span>默认账号: admin / admin123</span>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
/**
 * Vue 3 Composition API setup 语法
 *
 * 本组件功能：
 * 1. 展示登录表单
 * 2. 验证用户输入
 * 3. 调用登录接口
 * 4. 成功后跳转到首页
 */

// ==================== 导入部分 ====================

// ref, reactive: Vue 3 响应式 API
import { ref, reactive } from 'vue'

// useRouter: Vue Router 路由导航
import { useRouter } from 'vue-router'

// ElMessage: Element Plus 消息提示组件
import { ElMessage } from 'element-plus'

// useAuthStore: Pinia 认证状态管理
import { useAuthStore } from '../stores/auth'

// ==================== 组件逻辑 ====================

// 获取路由实例
const router = useRouter()

// 获取认证 store
const authStore = useAuthStore()

// 表单引用（用于调用表单方法）
const formRef = ref(null)

// 加载状态
const loading = ref(false)

// 表单数据
const form = reactive({
  username: 'admin',    // 默认用户名（方便测试）
  password: 'admin123'  // 默认密码
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

/**
 * 处理登录
 *
 * 流程：
 * 1. 验证表单
 * 2. 调用登录接口
 * 3. 成功跳转到首页
 * 4. 失败显示错误消息
 */
const handleLogin = async () => {
  // 验证表单（validate 返回 Promise）
  const valid = await formRef.value.validate().catch(() => false)

  // 验证失败，终止登录
  if (!valid) return

  // 开始加载
  loading.value = true

  try {
    // 调用 store 的登录方法
    const success = await authStore.login(form.username, form.password)

    if (success) {
      // 登录成功
      ElMessage.success('登录成功')

      // 跳转到首页
      router.push('/')
    } else {
      // 登录失败（用户名或密码错误）
      ElMessage.error('登录失败')
    }
  } catch (e) {
    // 捕获异常
    ElMessage.error('登录失败: ' + (e.message || '未知错误'))
  } finally {
    // 结束加载
    loading.value = false
  }
}
</script>

<style scoped>
/* scoped 表示这些样式只作用于当前组件 */

/* 全屏容器 - 使用 Flexbox 居中 */
.login-container {
  display: flex;
  justify-content: center;  /* 水平居中 */
  align-items: center;      /* 垂直居中 */
  height: 100vh;           /* 占满整个视口高度 */
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); /* 渐变背景 */
}

/* 登录卡片样式 */
.login-card {
  width: 400px;  /* 固定宽度 */
}

/* 提示文字样式 */
.tips {
  text-align: center;      /* 居中 */
  color: #999;            /* 灰色 */
  font-size: 12px;         /* 小字号 */
}
</style>
