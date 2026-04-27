<template>
  <div class="menu-container">
    <!-- 页面标题 -->
    <h2>菜单管理</h2>

    <!-- 菜单管理卡片 -->
    <el-card>
      <!-- 工具栏 -->
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">
          新增菜单
        </el-button>
      </div>

      <!-- 菜单列表表格 -->
      <el-table
        :data="tableData"
        border
        stripe
        style="width: 100%; margin-top: 20px"
        row-key="id"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="menuName" label="菜单名称" />
        <el-table-column prop="path" label="路径" />
        <el-table-column prop="component" label="组件" />
        <el-table-column prop="permission" label="权限标识" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.type === 0" type="warning">目录</el-tag>
            <el-tag v-else-if="row.type === 1" type="success">菜单</el-tag>
            <el-tag v-else type="info">按钮</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" />
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

    <!-- 新增/编辑菜单对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="form.menuName" />
        </el-form-item>
        <el-form-item label="父级ID" prop="parentId">
          <el-input v-model.number="form.parentId" />
        </el-form-item>
        <el-form-item label="路径" prop="path">
          <el-input v-model="form.path" />
        </el-form-item>
        <el-form-item label="组件" prop="component">
          <el-input v-model="form.component" />
        </el-form-item>
        <el-form-item label="权限标识" prop="permission">
          <el-input v-model="form.permission" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type">
            <el-option :value="0" label="目录" />
            <el-option :value="1" label="菜单" />
            <el-option :value="2" label="按钮" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model.number="form.sort" />
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
 * 菜单管理组件
 *
 * 功能：
 * 1. 展示菜单列表
 * 2. 新增菜单
 * 3. 编辑菜单
 * 4. 删除菜单
 *
 * 菜单类型：
 * - 0：目录
 * - 1：菜单
 * - 2：按钮
 */

import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMenuList, createMenu, updateMenu, deleteMenu } from '../api/menu'

/** 表格数据 */
const tableData = ref([])

/** 对话框显示状态 */
const dialogVisible = ref(false)

/** 对话框标题 */
const dialogTitle = ref('新增菜单')

/** 表单引用 */
const formRef = ref(null)

/** 表单数据 */
const form = reactive({
  id: null,
  parentId: 0,
  menuName: '',
  path: '',
  component: '',
  permission: '',
  type: 1,
  sort: 0
})

/** 表单验证规则 */
const rules = {
  menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }]
}

/** 加载菜单列表 */
const loadData = async () => {
  try {
    const res = await getMenuList()
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
  form.parentId = 0
  form.menuName = ''
  form.path = ''
  form.component = ''
  form.permission = ''
  form.type = 1
  form.sort = 0
  dialogTitle.value = '新增菜单'
  dialogVisible.value = true
}

/** 点击编辑按钮 */
const handleEdit = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑菜单'
  dialogVisible.value = true
}

/** 点击删除按钮 */
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该菜单吗？', '提示', { type: 'warning' })
    await deleteMenu(row.id)
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
      await updateMenu(form)
      ElMessage.success('更新成功')
    } else {
      await createMenu(form)
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
.menu-container h2 {
  margin-bottom: 20px;
}
</style>
