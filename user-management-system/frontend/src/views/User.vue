<template>
  <div class="user-container">
    <!-- 页面标题 -->
    <h2>用户管理</h2>

    <!-- 用户管理卡片 -->
    <el-card>

      <!-- 工具栏：新增按钮 + 搜索框 -->
      <div class="toolbar">
        <!-- 新增用户按钮 -->
        <el-button type="primary" @click="handleAdd">
          新增用户
        </el-button>

        <!-- 搜索区域 -->
        <div class="search">
          <el-input
            v-model="searchForm.username"
            placeholder="搜索用户名"
            clearable
            style="width: 200px"
          />
          <el-button type="primary" @click="loadData">
            搜索
          </el-button>
        </div>
      </div>

      <!-- 用户列表表格 -->
      <el-table
        :data="tableData"
        border
        stripe
        style="width: 100%; margin-top: 20px"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="roleId" label="角色ID" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
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

    <!-- 新增/编辑用户对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!form.id">
          <el-input v-model="form.password" type="password" />
        </el-form-item>
        <el-form-item label="角色ID" prop="roleId">
          <el-input v-model.number="form.roleId" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 用户管理组件
 *
 * 功能：
 * 1. 展示用户列表（分页）
 * 2. 搜索用户
 * 3. 新增用户
 * 4. 编辑用户
 * 5. 删除用户（确认对话框）
 */

import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, createUser, updateUser, deleteUser } from '../api/user'

// ==================== 数据定义 ====================

/** 表格数据 */
const tableData = ref([])

/** 对话框显示状态 */
const dialogVisible = ref(false)

/** 对话框标题 */
const dialogTitle = ref('新增用户')

/** 表单引用 */
const formRef = ref(null)

/** 分页参数 */
const pagination = reactive({
  page: 1,    // 当前页
  size: 10,   // 每页条数
  total: 0    // 总记录数
})

/** 搜索表单 */
const searchForm = reactive({
  username: ''  // 用户名（模糊搜索）
})

/** 新增/编辑表单 */
const form = reactive({
  id: null,      // 用户ID（编辑时有值，新增时为null）
  username: '',
  phone: '',
  password: '',
  roleId: null
})

/** 表单验证规则 */
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  roleId: [{ required: true, message: '请输入角色ID', trigger: 'blur' }]
}

// ==================== 方法 ====================

/**
 * 加载用户列表数据
 */
const loadData = async () => {
  try {
    const res = await getUserList({
      page: pagination.page,
      size: pagination.size,
      username: searchForm.username
    })
    if (res.code === 200) {
      tableData.value = res.data.records || res.data.list || []
      pagination.total = res.data.total || 0
    }
  } catch (e) {
    console.error(e)
  }
}

/**
 * 点击新增按钮
 * 重置表单，打开对话框
 */
const handleAdd = () => {
  // 重置表单
  form.id = null
  form.username = ''
  form.phone = ''
  form.password = ''
  form.roleId = null
  dialogTitle.value = '新增用户'
  dialogVisible.value = true
}

/**
 * 点击编辑按钮
 * 填充表单数据，打开对话框
 */
const handleEdit = (row) => {
  // 复制行数据到表单
  Object.assign(form, row)
  form.password = ''  // 编辑时不清空密码字段
  dialogTitle.value = '编辑用户'
  dialogVisible.value = true
}

/**
 * 点击删除按钮
 * 弹出确认对话框，确认后删除
 */
const handleDelete = async (row) => {
  try {
    // 弹出确认对话框
    await ElMessageBox.confirm('确定删除该用户吗？', '提示', { type: 'warning' })
    // 调用删除接口
    await deleteUser(row.id)
    ElMessage.success('删除成功')
    // 刷新列表
    loadData()
  } catch (e) {
    // 如果用户点击取消，不显示错误
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

/**
 * 提交表单（新增或编辑）
 */
const handleSubmit = async () => {
  // 验证表单
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  try {
    if (form.id) {
      // 编辑模式
      await updateUser(form)
      ElMessage.success('更新成功')
    } else {
      // 新增模式
      await createUser(form)
      ElMessage.success('新增成功')
    }
    // 关闭对话框
    dialogVisible.value = false
    // 刷新列表
    loadData()
  } catch (e) {
    ElMessage.error(e.message || '操作失败')
  }
}

// ==================== 生命周期 ====================

/**
 * 组件挂载时加载数据
 */
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.user-container h2 {
  margin-bottom: 20px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
}

.search {
  display: flex;
  gap: 10px;
}
</style>
