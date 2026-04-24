<template>
  <div class="dashboard">
    <h2>首页</h2>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-value">{{ stats.userCount }}</div>
            <div class="stat-label">用户数量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-value">{{ stats.roleCount }}</div>
            <div class="stat-label">角色数量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-value">{{ stats.menuCount }}</div>
            <div class="stat-label">菜单数量</div>
          </div>
        </el-card>
      </el-col>
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

const stats = ref({
  userCount: 0,
  roleCount: 0,
  menuCount: 0,
  logCount: 0
})

onMounted(() => {
  stats.value = {
    userCount: 10,
    roleCount: 2,
    menuCount: 5,
    logCount: 100
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
