/**
 * 应用入口文件
 *
 * 职责：
 * 1. 创建 Vue 3 应用实例
 * 2. 注册插件（Pinia、Router、Element Plus）
 * 3. 注册全局组件（Element Plus 图标）
 * 4. 挂载应用到 DOM
 *
 * 执行顺序：
 * 1. 导入依赖和组件
 * 2. 创建 app 实例
 * 3. 注册全局组件
 * 4. 注册插件
 * 5. 挂载到 DOM
 */

// ==================== 导入部分 ====================

// Vue 3 的 createApp 函数
import { createApp } from 'vue'

// 状态管理插件
import { createPinia } from 'pinia'

// Element Plus UI 组件库
import ElementPlus from 'element-plus'

// Element Plus 样式
import 'element-plus/dist/index.css'

// Element Plus 图标库
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// 根组件
import App from './App.vue'

// 路由实例
import router from './router'

// ==================== 应用创建部分 ====================

/**
 * 创建 Vue 应用实例
 *
 * createApp 是 Vue 3 的核心函数，
 * 创建一个新的应用实例，参数是根组件。
 */
const app = createApp(App)

// ==================== 全局组件注册 ====================

/**
 * 注册所有 Element Plus 图标为全局组件
 *
 * 循环遍历所有图标组件，
 * 通过 app.component() 注册为全局组件。
 *
 * 注册后可以在任意模板中使用：
 * <el-icon><Plus /></el-icon>
 *
 * 图标名称格式：
 * - ElIconPlus
 * - ElIconDelete
 * - ElIconUser
 * 等
 */
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// ==================== 插件注册 ====================

/**
 * 注册 Pinia 状态管理
 *
 * Pinia 是 Vue 3 推荐的全新状态管理库，
 * 比 Vuex 更轻量、更简洁。
 */
app.use(createPinia())

/**
 * 注册 Vue Router
 *
 * 必须在使用 app.mount() 之前注册。
 */
app.use(router)

/**
 * 注册 Element Plus
 *
 * Element Plus 是 Element UI 的 Vue 3 版本，
 * 提供了丰富的 UI 组件。
 */
app.use(ElementPlus)

// ==================== 应用挂载 ====================

/**
 * 挂载应用到 DOM
 *
 * 将 Vue 应用挂载到 #app 元素上。
 * #app 应该在 index.html 中存在。
 *
 * 挂载后：
 * - Vue 应用接管了 #app 及其子元素
 * - 开始响应式更新
 */
app.mount('#app')
