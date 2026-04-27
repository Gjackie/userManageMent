<template>
  <div class="role-container">
    <!-- 页面标题 -->
    <h2>角色管理</h2>

    <!-- 角色管理卡片 -->
    <el-card>
      <!-- 工具栏 -->
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">
          新增角色
        </el-button>
      </div>

      <!-- 角色列表表格 -->
      <el-table
        :data="tableData"
        border
        stripe
        style="width: 100%; margin-top: 20px"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="roleName" label="角色名称" />
        <el-table-column prop="roleCode" label="角色标识" />
        <el-table-column prop="description" label="描述" />
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
    </el-card>

    <!-- 新增/编辑角色对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" />
        </el-form-item>
        <el-form-item label="角色标识" prop="roleCode">
          <el-input v-model="form.roleCode" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" />
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
 * 角色管理组件
 *
 * 功能：
 * 1. 展示角色列表
 * 2. 新增角色
 * 3. 编辑角色
 * 4. 删除角色
 */

import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRoleList, createRole, updateRole, deleteRole } from '../api/role'

/** 表格数据 */
const tableData = ref([])

/** 对话框显示状态 */
const dialogVisible = ref(false)

/** 对话框标题 */
const dialogTitle = ref('新增角色')

/** 表单引用 */
const formRef = ref(null)

/** 表单数据 */
const form = reactive({
  id: null,
  roleName: '',
  roleCode: '',
  description: ''
})

/** 表单验证规则 */
const rules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  roleCode: [{ required: true, message: '请输入角色标识', trigger: 'blur' }]
}

/** 加载角色列表 */
const loadData = async () => {
  try {
    const res = await getRoleList()
    if (res.code === 200) {
      tableData.value = res.data.records || res.data.list || []
    }
  } catch (e) {
    console.error(e)
  }
}

/** 点击新增按钮 */
const handleAdd = () => {
  form.id = null
  form.roleName = ''
  form.roleCode = ''
  form.description = ''
  dialogTitle.value = '新增角色'
  dialogVisible.value = true
}

/** 点击编辑按钮 */
const handleEdit = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑角色'
  dialogVisible.value = true
}

/** 点击删除按钮 */
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该角色吗？', '提示', { type: 'warning' })
    await deleteRole(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

/** 提交表单 */
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  try {
    if (form.id) {
      await updateRole(form)
      ElMessage.success('更新成功')
    } else {
      await createRole(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error(e.message || '操作失败')
  }
}

/** 组件挂载时加载数据 */
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.role-container h2 {
  margin-bottom: 20px;
}
</style>
