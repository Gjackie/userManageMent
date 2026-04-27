<template>
  <div class="log-container">
    <!-- 页面标题 -->
    <h2>日志管理</h2>

    <!-- 日志管理卡片 -->
    <el-card>
      <!-- 日志列表表格 -->
      <el-table
        :data="tableData"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="操作用户" width="120" />
        <el-table-column prop="operationType" label="操作类型" width="180" />
        <el-table-column prop="operationContent" label="操作内容" show-overflow-tooltip />
        <el-table-column prop="method" label="请求方法" width="80" />
        <el-table-column prop="ip" label="IP地址" width="140" />
        <el-table-column prop="time" label="耗时(ms)" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : 'danger'">
              {{ row.status === 0 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="操作时间" width="180" />
      </el-table>

      <!-- 分页组件 -->
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next"
        style="margin-top: 20px; justify-content: flex-end"
        @size-change="loadData"
        @current-change="loadData"
      />
    </el-card>
  </div>
</template>

<script setup>
/**
 * 日志管理组件
 *
 * 功能：
 * 1. 展示操作日志列表（分页）
 * 2. 查看操作详情
 *
 * 日志来源：
 * - 后端通过 AOP 切面自动记录
 * - 记录所有 Controller 的操作
 */

import { ref, reactive, onMounted } from 'vue'
import { getLogList } from '../api/log'

/** 表格数据 */
const tableData = ref([])

/** 分页参数 */
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

/**
 * 加载日志列表
 */
const loadData = async () => {
  try {
    const res = await getLogList({
      page: pagination.page,
      size: pagination.size
    })
    if (res.code === 200) {
      tableData.value = res.data.records || res.data.list || []
      pagination.total = res.data.total || 0
    }
  } catch (e) {
    console.error(e)
  }
}

/** 组件挂载时加载数据 */
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.log-container h2 {
  margin-bottom: 20px;
}
</style>
