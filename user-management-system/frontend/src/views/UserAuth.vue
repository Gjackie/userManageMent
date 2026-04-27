<template>
  <!-- 用户认证页面容器 -->
  <div class="user-auth-container">

    <!-- 认证表单卡片 -->
    <div class="auth-box">

      <!-- 登录/注册 Tab 切换 -->
      <div class="auth-tabs">
        <!-- 登录 Tab -->
        <div
          :class="['tab-item', { active: activeTab === 'login' }]"
          @click="activeTab = 'login'"
        >
          登录
        </div>
        <!-- 注册 Tab -->
        <div
          :class="['tab-item', { active: activeTab === 'register' }]"
          @click="activeTab = 'register'"
        >
          注册
        </div>
      </div>

      <!-- 登录表单 -->
      <div v-if="activeTab === 'login'" class="tab-content">
        <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" label-width="80px">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="loginForm.username" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" @keyup.enter="handleLogin" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="loading" style="width: 100%" @click="handleLogin">
              登录
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 分割线 -->
        <div class="divider">
          <span>其他登录方式</span>
        </div>

        <!-- 微信登录入口 -->
        <div class="wechat-login" @click="showWechatQrCode">
          <div class="wechat-icon">
            <svg viewBox="0 0 24 24" width="32" height="32">
              <path fill="#07C160" d="M8.691 2.188C3.891 2.188 0 5.476 0 9.53c0 2.212 1.17 4.203 3.002 5.55a.59.59 0 0 1 .213.665l-.39 1.48c-.019.07-.048.141-.048.213 0 .163.13.295.29.295a.326.326 0 0 0 .167-.054l1.903-1.114a.864.864 0 0 1 .717-.098 10.16 10.16 0 0 0 2.837.403c.276 0 .543-.027.811-.05-.857-2.578.157-4.972 1.932-6.446 1.703-1.415 3.882-1.98 5.853-1.838-.576-3.583-4.196-6.348-8.596-6.348z"/>
            </svg>
          </div>
          <span>微信扫码登录</span>
        </div>
      </div>

      <!-- 注册表单 -->
      <div v-if="activeTab === 'register'" class="tab-content">
        <el-form :model="registerForm" :rules="registerRules" ref="registerFormRef" label-width="80px">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="registerForm.username" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="registerForm.phone" placeholder="请输入手机号" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="loading" style="width: 100%" @click="handleRegister">
              注册
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- 微信扫码登录对话框 -->
    <el-dialog v-model="wechatDialogVisible" title="微信扫码登录" width="320px" center>
      <div class="wechat-qrcode-container">
        <div v-if="!wechatScanned" class="qrcode-wrapper">
          <canvas ref="qrcodeCanvas"></canvas>
          <div v-if="qrcodeLoading" class="qrcode-loading">
            <el-icon class="is-loading"><Loading /></el-icon>
            <span>加载中...</span>
          </div>
        </div>
        <div v-else class="scan-success">
          <el-icon color="#67C23A" :size="64"><CircleCheckFilled /></el-icon>
          <p>扫码成功，请在手机上确认登录</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 用户认证页面组件
 *
 * 功能：
 * 1. 登录表单
 * 2. 注册表单
 * 3. 微信扫码登录
 *
 * Tab 切换：
 * - 登录
 * - 注册
 */

import { ref, reactive, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Loading, CircleCheckFilled } from '@element-plus/icons-vue'
import { login, register } from '../api/user'
import QRCode from 'qrcode'

const router = useRouter()

const activeTab = ref('login')
const loginFormRef = ref(null)
const registerFormRef = ref(null)
const loading = ref(false)
const wechatDialogVisible = ref(false)
const qrcodeLoading = ref(false)
const wechatScanned = ref(false)
const qrcodeCanvas = ref(null)

const loginForm = reactive({
  username: '',
  password: ''
})

const registerForm = reactive({
  username: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const registerRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const handleLogin = async () => {
  const valid = await loginFormRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res = await login({ username: loginForm.username, password: loginForm.password })
    if (res.code === 200) {
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('user', JSON.stringify({
        id: res.data.userId,
        username: res.data.username
      }))
      ElMessage.success('登录成功')
      router.push('/user-welcome')
    } else {
      ElMessage.error(res.message || '登录失败')
    }
  } catch (e) {
    ElMessage.error('登录失败: ' + (e.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

const handleRegister = async () => {
  const valid = await registerFormRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res = await register({
      username: registerForm.username,
      phone: registerForm.phone,
      password: registerForm.password,
      confirmPassword: registerForm.confirmPassword
    })
    if (res.code === 200) {
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('user', JSON.stringify({
        id: res.data.userId,
        username: res.data.username
      }))
      ElMessage.success('注册成功')
      router.push('/user-welcome')
    } else {
      ElMessage.error(res.message || '注册失败')
    }
  } catch (e) {
    ElMessage.error('注册失败: ' + (e.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

const showWechatQrCode = async () => {
  wechatDialogVisible.value = true
  wechatScanned.value = false
  qrcodeLoading.value = true
  await nextTick()
  generateQrCode()
}

const generateQrCode = async () => {
  try {
    const res = await fetch('http://localhost:8080/api/auth/wechat/qrcode')
    const data = await res.json()
    if (data.code === 200) {
      const canvas = qrcodeCanvas.value
      if (canvas) {
        await QRCode.toCanvas(canvas, data.data.qrCodeUrl, {
          width: 200,
          margin: 2
        })
        qrcodeLoading.value = false
        startPollingWechatStatus(data.data.state)
      }
    }
  } catch (e) {
    qrcodeLoading.value = false
    ElMessage.error('获取二维码失败')
  }
}

const startPollingWechatStatus = (state) => {
  let pollCount = 0
  const maxPolls = 150
  const pollInterval = setInterval(async () => {
    pollCount++
    if (pollCount >= maxPolls) {
      clearInterval(pollInterval)
      return
    }
    try {
      const res = await fetch(`http://localhost:8080/api/auth/wechat/callback?code=DEMO_CODE&state=${state}`)
      const data = await res.json()
    } catch (e) {
      console.error(e)
    }
  }, 2000)
}
</script>

<style scoped>
.user-auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.auth-box {
  background: #fff;
  border-radius: 12px;
  padding: 30px;
  width: 400px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.auth-tabs {
  display: flex;
  border-bottom: 1px solid #eee;
  margin-bottom: 20px;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 12px 0;
  cursor: pointer;
  font-size: 16px;
  color: #666;
  transition: all 0.3s;
}

.tab-item.active {
  color: #667eea;
  border-bottom: 2px solid #667eea;
}

.tab-content {
  min-height: 280px;
}

.divider {
  text-align: center;
  margin: 20px 0;
  position: relative;
}

.divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: #eee;
}

.divider span {
  background: #fff;
  padding: 0 15px;
  color: #999;
  font-size: 12px;
  position: relative;
}

.wechat-login {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  padding: 15px;
  border-radius: 8px;
  transition: background 0.3s;
}

.wechat-login:hover {
  background: #f5f5f5;
}

.wechat-icon {
  margin-bottom: 10px;
}

.wechat-login span {
  color: #666;
  font-size: 14px;
}

.wechat-qrcode-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 250px;
}

.qrcode-wrapper {
  position: relative;
}

.qrcode-loading {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: rgba(255, 255, 255, 0.9);
}

.scan-success {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.scan-success p {
  color: #666;
  font-size: 14px;
}
</style>
