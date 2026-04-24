<template>
  <div class="user-container">
    <h2>用户管理</h2>

    <el-card>
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">新增用户</el-button>
        <div class="search">
          <el-input v-model="searchForm.username" placeholder="搜索用户名" clearable style="width: 200px" />
          <el-button type="primary" @click="loadData">搜索</el-button>
        </div>
      </div>

      <el-table :data="tableData" border stripe style="width: 100%; margin-top: 20px">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="roleId" label="角色ID" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, createUser, updateUser, deleteUser } from '../api/user'

const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增用户')
const formRef = ref(null)

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const searchForm = reactive({
  username: ''
})

const form = reactive({
  id: null,
  username: '',
  phone: '',
  password: '',
  roleId: null
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  roleId: [{ required: true, message: '请输入角色ID', trigger: 'blur' }]
}

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

const handleAdd = () => {
  form.id = null
  form.username = ''
  form.phone = ''
  form.password = ''
  form.roleId = null
  dialogTitle.value = '新增用户'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  form.password = ''
  dialogTitle.value = '编辑用户'
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该用户吗？', '提示', { type: 'warning' })
    await deleteUser(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  try {
    if (form.id) {
      await updateUser(form)
      ElMessage.success('更新成功')
    } else {
      await createUser(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error(e.message || '操作失败')
  }
}

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
