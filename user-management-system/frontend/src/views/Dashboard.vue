<template>
  <!-- 仪表盘/首页 -->
  <div class="dashboard">

    <!-- 页面标题 -->
    <h2>首页</h2>

    <!-- 统计卡片区域 -->
    <el-row :gutter="20">

      <!-- 用户数量统计卡片 -->
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-value">{{ stats.userCount }}</div>
            <div class="stat-label">用户数量</div>
          </div>
        </el-card>
      </el-col>

      <!-- 角色数量统计卡片 -->
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-value">{{ stats.roleCount }}</div>
            <div class="stat-label">角色数量</div>
          </div>
        </el-card>
      </el-col>

      <!-- 菜单数量统计卡片 -->
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-value">{{ stats.menuCount }}</div>
            <div class="stat-label">菜单数量</div>
          </div>
        </el-card>
      </el-col>

      <!-- 日志数量统计卡片 -->
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-value">{{ stats.logCount }}</div>
            <div class="stat-label">日志数量</div>
          </div>
        </el-card>
      </el-col>

    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUserList } from '../api/user'
import { getRoleList } from '../api/role'
import { getMenuList } from '../api/menu'
import { getLogList } from '../api/log'

const stats = ref({
  userCount: 0,
  roleCount: 0,
  menuCount: 0,
  logCount: 0
})

onMounted(async () => {
  try {
    const [userRes, roleRes, menuRes, logRes] = await Promise.all([
      getUserList({ page: 1, size: 1 }),
      getRoleList({ page: 1, size: 1 }),
      getMenuList({ page: 1, size: 1 }),
      getLogList({ page: 1, size: 1 })
    ])
    stats.value = {
      userCount: userRes.data?.total || 0,
      roleCount: roleRes.data?.total || 0,
      menuCount: menuRes.data?.total || 0,
      logCount: logRes.data?.total || 0
    }
  } catch (e) {
    // token 过期时会自动跳转到登录页，无需处理
  }
})
</script>

<style scoped>
.dashboard h2 {
  margin-bottom: 20px;
}

.stat-item {
  text-align: center;
  padding: 20px;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
}

.stat-label {
  margin-top: 10px;
  color: #999;
}
</style>
