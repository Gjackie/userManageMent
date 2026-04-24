# 用户管理系统 - 前端

基于 Vue 3 + Element Plus 的用户管理系统前端项目。

## 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **Vue Router 4** - Vue.js 官方路由管理器
- **Pinia** - Vue.js 轻量级状态管理库
- **Element Plus** - 基于 Vue 3 的组件库
- **Axios** - HTTP 请求库
- **Vite** - 新一代前端构建工具

## 项目结构

```
frontend/
├── src/
│   ├── api/              # API 接口模块
│   │   ├── auth.js       # 登录认证接口
│   │   ├── user.js       # 用户管理接口
│   │   ├── role.js       # 角色管理接口
│   │   ├── menu.js       # 菜单管理接口
│   │   └── log.js        # 日志查询接口
│   ├── router/
│   │   └── index.js      # 路由配置
│   ├── stores/
│   │   └── auth.js       # 认证状态管理
│   ├── views/            # 页面组件
│   │   ├── Login.vue     # 登录页
│   │   ├── Layout.vue    # 布局组件
│   │   ├── Dashboard.vue # 首页
│   │   ├── User.vue      # 用户管理
│   │   ├── Role.vue      # 角色管理
│   │   ├── Menu.vue      # 菜单管理
│   │   └── Log.vue       # 日志管理
│   ├── App.vue           # 根组件
│   └── main.js           # 入口文件
├── index.html
├── package.json
└── vite.config.js
```

## 安装与运行

```bash
# 安装依赖
npm install

# 开发环境运行
npm run dev

# 生产环境构建
npm run build

# 预览生产构建
npm run preview
```

## 开发环境

- 前端开发服务器: http://localhost:5173
- 后端 API 地址: http://localhost:8080

## 页面说明

### 1. 登录页 (Login)

**路由**: `/login`

**功能**:
- 用户名密码登录
- 表单验证
- 登录成功自动跳转首页
- 默认填充管理员账号

**默认账号**:
- 用户名: `admin`
- 密码: `admin123`

---

### 2. 布局页 (Layout)

**路由**: `/` (根布局)

**功能**:
- 左侧导航菜单
- 顶部用户信息展示
- 退出登录功能

**导航菜单**:
- 首页 `/dashboard`
- 用户管理 `/user`
- 角色管理 `/role`
- 菜单管理 `/menu`
- 日志管理 `/log`

---

### 3. 首页 (Dashboard)

**路由**: `/dashboard`

**功能**:
- 用户数量统计
- 角色数量统计
- 菜单数量统计
- 日志数量统计

---

### 4. 用户管理 (User)

**路由**: `/user`

**功能**:
- 用户列表展示（分页）
- 用户名搜索
- 新增用户
- 编辑用户
- 删除用户（软删除）

**用户字段**:
| 字段 | 说明 |
|------|------|
| id | 用户ID |
| username | 用户名 |
| phone | 手机号 |
| roleId | 角色ID |
| createTime | 创建时间 |

---

### 5. 角色管理 (Role)

**路由**: `/role`

**功能**:
- 角色列表展示
- 新增角色
- 编辑角色
- 删除角色（软删除）

**角色字段**:
| 字段 | 说明 |
|------|------|
| id | 角色ID |
| roleName | 角色名称 |
| roleCode | 角色标识（唯一） |
| description | 角色描述 |
| createTime | 创建时间 |

---

### 6. 菜单管理 (Menu)

**路由**: `/menu`

**功能**:
- 菜单列表展示
- 新增菜单
- 编辑菜单
- 删除菜单

**菜单字段**:
| 字段 | 说明 |
|------|------|
| id | 菜单ID |
| parentId | 父级菜单ID |
| menuName | 菜单名称 |
| path | 菜单路径 |
| component | 组件路径 |
| permission | 权限标识 |
| type | 类型（0-目录，1-菜单，2-按钮） |
| sort | 排序 |
| createTime | 创建时间 |

**菜单类型说明**:
- `0` - 目录
- `1` - 菜单
- `2` - 按钮

---

### 7. 日志管理 (Log)

**路由**: `/log`

**功能**:
- 操作日志列表（分页）
- 查看操作详情
- 操作状态筛选

**日志字段**:
| 字段 | 说明 |
|------|------|
| id | 日志ID |
| username | 操作用户 |
| operationType | 操作类型 |
| operationContent | 操作内容 |
| method | 请求方法 |
| params | 请求参数 |
| ip | IP地址 |
| time | 耗时（毫秒） |
| status | 状态（0-成功，1-失败） |
| errorMsg | 错误信息 |
| createTime | 操作时间 |

---

## API 接口

### 认证接口

#### 登录
```
POST /api/auth/login
Content-Type: application/json

Request:
{
  "username": "admin",
  "password": "admin123"
}

Response:
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "xxx",
    "userId": 1,
    "username": "admin"
  }
}
```

#### 获取用户信息
```
GET /api/auth/info
Authorization: Bearer {token}

Response:
{
  "code": 200,
  "data": {
    "id": 1,
    "username": "admin",
    "phone": "13800138000",
    "roleId": 1
  }
}
```

---

### 用户接口

#### 获取用户列表
```
GET /api/users?page=1&size=10&username=admin
Authorization: Bearer {token}

Response:
{
  "code": 200,
  "data": {
    "records": [...],
    "total": 100
  }
}
```

#### 创建用户
```
POST /api/users
Authorization: Bearer {token}
Content-Type: application/json

{
  "username": "newuser",
  "password": "password123",
  "phone": "13900139000",
  "roleId": 2
}
```

#### 更新用户
```
PUT /api/users
Authorization: Bearer {token}
Content-Type: application/json

{
  "id": 1,
  "username": "admin",
  "phone": "13900139000",
  "roleId": 1
}
```

#### 删除用户
```
DELETE /api/users/{id}
Authorization: Bearer {token}
```

---

### 角色接口

#### 获取角色列表
```
GET /api/roles
Authorization: Bearer {token}
```

#### 创建角色
```
POST /api/roles
Authorization: Bearer {token}
Content-Type: application/json

{
  "roleName": "角色名称",
  "roleCode": "ROLE_CODE",
  "description": "角色描述"
}
```

#### 更新角色
```
PUT /api/roles
Authorization: Bearer {token}
Content-Type: application/json

{
  "id": 1,
  "roleName": "角色名称",
  "roleCode": "ROLE_CODE",
  "description": "角色描述"
}
```

#### 删除角色
```
DELETE /api/roles/{id}
Authorization: Bearer {token}
```

---

### 菜单接口

#### 获取菜单列表
```
GET /api/menus
Authorization: Bearer {token}
```

#### 创建菜单
```
POST /api/menus
Authorization: Bearer {token}
Content-Type: application/json

{
  "parentId": 0,
  "menuName": "菜单名称",
  "path": "/menu",
  "component": "menu/index",
  "permission": "system:menu:list",
  "type": 1,
  "sort": 1
}
```

#### 更新菜单
```
PUT /api/menus
Authorization: Bearer {token}
Content-Type: application/json

{
  "id": 1,
  "parentId": 0,
  "menuName": "菜单名称",
  "path": "/menu",
  "component": "menu/index",
  "permission": "system:menu:list",
  "type": 1,
  "sort": 1
}
```

#### 删除菜单
```
DELETE /api/menus/{id}
Authorization: Bearer {token}
```

---

### 日志接口

#### 获取日志列表
```
GET /api/logs?page=1&size=10
Authorization: Bearer {token}

Response:
{
  "code": 200,
  "data": {
    "records": [
      {
        "id": 1,
        "username": "admin",
        "operationType": "com.example.usermanagement.controller.AuthController.login",
        "operationContent": "执行失败: xxx",
        "method": "POST",
        "params": null,
        "ip": "127.0.0.1",
        "time": 100,
        "status": 1,
        "errorMsg": "xxx",
        "createTime": "2026-04-24 10:00:00"
      }
    ],
    "total": 100
  }
}
```

---

## 状态管理

### Auth Store

```javascript
// 状态
{
  token: localStorage.getItem('token') || '',
  user: { id, username, ... }
}

// actions
login(username, password)  // 登录
fetchUserInfo()             // 获取用户信息
logout()                    // 退出登录
```

---

## 路由守卫

项目使用 `beforeEach` 路由守卫进行权限控制：

- 未登录访问非登录页 → 重定向到 `/login`
- 已登录访问登录页 → 重定向到首页

---

## 请求拦截器

### 请求拦截
- 自动添加 `Authorization` 请求头
- 格式: `Bearer {token}`

### 响应拦截
- 401 未授权 → 清除 token 并跳转登录页
- 其他错误 → 传递给调用方处理

---

## 环境变量

如需修改 API 地址，修改 `src/api/auth.js` 中的 `baseURL`:

```javascript
const request = axios.create({
  baseURL: 'http://localhost:8080/api',  // 修改此处
  timeout: 10000
})
```

---

## 注意事项

1. **跨域问题**: 开发环境下 Vite 已配置代理，生产环境需后端配置 CORS
2. **Token 存储**: Token 存储在 localStorage，生产环境建议使用 httpOnly Cookie
3. **权限控制**: 当前前端仅做路由层面的拦截，后端已实现完整的权限控制
4. **软删除**: 用户、角色、菜单均使用软删除，数据不会真正从数据库删除
