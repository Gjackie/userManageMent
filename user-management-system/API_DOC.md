# 用户管理系统 API 文档

> 基于 Spring Boot 2.7.0 + Vue 3 的用户管理系统后端接口文档
>
> **基础URL**: `http://localhost:8080/api`
>
> **认证方式**: JWT Bearer Token（在请求头中添加 `Authorization: Bearer <token>`）

---

## 通用说明

### 响应格式

所有接口响应统一使用 `Result<T>` 包装：

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": { ... }
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | int | 状态码，200表示成功，500表示失败 |
| msg | string | 响应消息 |
| data | object | 响应数据（可选） |

### 分页响应

分页接口使用 `PageResult<T>` 包装数据：

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "total": 100,
    "records": [ ... ]
  }
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| total | long | 总记录数 |
| records | array | 当前页数据列表 |

---

## 认证接口 `/api/auth`

### 1. 用户登录

**请求**

```
POST /api/auth/login
Content-Type: application/json
```

**请求参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | string | 是 | 用户名 |
| password | string | 是 | 密码 |
| captcha | string | 否 | 验证码 |
| uuid | string | 否 | 验证码UUID |

**请求示例**

```json
{
  "username": "admin",
  "password": "admin123"
}
```

**响应示例**

```json
{
  "code": 200,
  "msg": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userId": 1,
    "username": "admin"
  }
}
```

---

### 2. 获取用户信息

**请求**

```
GET /api/auth/info
Authorization: Bearer <token>
```

**响应示例**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "username": "admin",
    "phone": "13800138000",
    "roleId": 1
  }
}
```

---

### 3. 用户注册

**请求**

```
POST /api/auth/register
Content-Type: application/json
```

**请求参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | string | 是 | 用户名 |
| phone | string | 是 | 手机号（格式：1[3-9]开头的11位数字） |
| password | string | 是 | 密码 |
| confirmPassword | string | 是 | 确认密码 |

**请求示例**

```json
{
  "username": "newuser",
  "phone": "13900139000",
  "password": "password123",
  "confirmPassword": "password123"
}
```

**响应示例**

```json
{
  "code": 200,
  "msg": "注册成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userId": 2,
    "username": "newuser",
    "phone": "13900139000"
  }
}
```

---

### 4. 获取微信扫码登录二维码

**请求**

```
GET /api/auth/wechat/qrcode
```

**响应示例**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "qrCodeUrl": "https://open.weixin.qq.com/connect/qrconnect?appid=xxx&...",
    "state": "uuid-string"
  }
}
```

---

### 5. 微信扫码登录回调

**请求**

```
GET /api/auth/wechat/callback?code=xxx&state=xxx
```

**响应示例**

```json
{
  "code": 200,
  "msg": "微信登录成功",
  "data": {
    "success": true,
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userId": 3,
    "username": "微信用户_xxx",
    "phone": "openid_xxx"
  }
}
```

---

## 用户管理 `/api/users`

### 1. 用户列表（分页）

**请求**

```
GET /api/users/list
Authorization: Bearer <token>
```

**Query 参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | int | 否 | 页码，默认1 |
| pageSize | int | 否 | 每页条数，默认10 |
| username | string | 否 | 用户名（模糊搜索） |
| phone | string | 否 | 手机号（模糊搜索） |

**响应示例**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "total": 50,
    "records": [
      {
        "id": 1,
        "username": "admin",
        "phone": "13800138000",
        "roleId": 1,
        "createTime": "2024-01-01T10:00:00",
        "updateTime": "2024-01-01T10:00:00",
        "deleted": 0
      }
    ]
  }
}
```

---

### 2. 获取用户详情

**请求**

```
GET /api/users/{id}
Authorization: Bearer <token>
```

**路径参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 用户ID |

**响应示例**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "username": "admin",
    "phone": "13800138000",
    "roleId": 1,
    "createTime": "2024-01-01T10:00:00",
    "updateTime": "2024-01-01T10:00:00",
    "deleted": 0
  }
}
```

---

### 3. 创建用户

**请求**

```
POST /api/users
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | string | 是 | 用户名 |
| phone | string | 是 | 手机号（格式：1[3-9]开头的11位数字） |
| password | string | 是 | 密码 |
| roleId | long | 否 | 角色ID |

**请求示例**

```json
{
  "username": "newuser",
  "phone": "13900139000",
  "password": "password123",
  "roleId": 2
}
```

**响应示例**

```json
{
  "code": 200,
  "msg": "创建成功",
  "data": {
    "id": 10,
    "username": "newuser",
    "phone": "13900139000",
    "roleId": 2,
    "createTime": "2024-01-15T14:30:00",
    "updateTime": "2024-01-15T14:30:00",
    "deleted": 0
  }
}
```

---

### 4. 更新用户

**请求**

```
PUT /api/users
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 用户ID |
| username | string | 否 | 用户名 |
| phone | string | 否 | 手机号（格式：1[3-9]开头的11位数字） |
| roleId | long | 否 | 角色ID |

**请求示例**

```json
{
  "id": 10,
  "username": "updateduser",
  "phone": "13900139001",
  "roleId": 1
}
```

**响应示例**

```json
{
  "code": 200,
  "msg": "更新成功",
  "data": {
    "id": 10,
    "username": "updateduser",
    "phone": "13900139001",
    "roleId": 1,
    "createTime": "2024-01-15T14:30:00",
    "updateTime": "2024-01-15T15:00:00",
    "deleted": 0
  }
}
```

---

### 5. 删除用户

**请求**

```
DELETE /api/users/{id}
Authorization: Bearer <token>
```

**路径参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 用户ID |

**响应示例**

```json
{
  "code": 200,
  "msg": "删除成功",
  "data": null
}
```

---

## 角色管理 `/api/roles`

### 1. 角色列表

**请求**

```
GET /api/roles/list
Authorization: Bearer <token>
```

**响应示例**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 1,
      "roleName": "管理员",
      "roleCode": "ADMIN",
      "description": "系统管理员",
      "createTime": "2024-01-01T10:00:00",
      "updateTime": "2024-01-01T10:00:00",
      "deleted": 0
    },
    {
      "id": 2,
      "roleName": "普通用户",
      "roleCode": "USER",
      "description": "普通用户角色",
      "createTime": "2024-01-01T10:00:00",
      "updateTime": "2024-01-01T10:00:00",
      "deleted": 0
    }
  ]
}
```

---

### 2. 获取角色详情

**请求**

```
GET /api/roles/{id}
Authorization: Bearer <token>
```

**路径参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 角色ID |

**响应示例**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "roleName": "管理员",
    "roleCode": "ADMIN",
    "description": "系统管理员",
    "createTime": "2024-01-01T10:00:00",
    "updateTime": "2024-01-01T10:00:00",
    "deleted": 0
  }
}
```

---

### 3. 创建角色

**请求**

```
POST /api/roles
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| roleName | string | 是 | 角色名称 |
| roleCode | string | 是 | 角色标识 |
| description | string | 否 | 角色描述 |

**请求示例**

```json
{
  "roleName": "测试角色",
  "roleCode": "TEST",
  "description": "用于测试的角色"
}
```

**响应示例**

```json
{
  "code": 200,
  "msg": "创建成功",
  "data": {
    "id": 3,
    "roleName": "测试角色",
    "roleCode": "TEST",
    "description": "用于测试的角色",
    "createTime": "2024-01-15T16:00:00",
    "updateTime": "2024-01-15T16:00:00",
    "deleted": 0
  }
}
```

---

### 4. 更新角色

**请求**

```
PUT /api/roles
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 角色ID |
| roleName | string | 否 | 角色名称 |
| roleCode | string | 否 | 角色标识 |
| description | string | 否 | 角色描述 |

**请求示例**

```json
{
  "id": 3,
  "roleName": "更新后的角色",
  "description": "更新后的描述"
}
```

**响应示例**

```json
{
  "code": 200,
  "msg": "更新成功",
  "data": {
    "id": 3,
    "roleName": "更新后的角色",
    "roleCode": "TEST",
    "description": "更新后的描述",
    "createTime": "2024-01-15T16:00:00",
    "updateTime": "2024-01-15T16:30:00",
    "deleted": 0
  }
}
```

---

### 5. 删除角色

**请求**

```
DELETE /api/roles/{id}
Authorization: Bearer <token>
```

**路径参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 角色ID |

**响应示例**

```json
{
  "code": 200,
  "msg": "删除成功",
  "data": null
}
```

---

## 菜单管理 `/api/menus`

### 1. 菜单列表

**请求**

```
GET /api/menus/list
Authorization: Bearer <token>
```

**响应示例**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 1,
      "parentId": 0,
      "menuName": "系统管理",
      "path": "/system",
      "component": "Layout",
      "permission": null,
      "type": 0,
      "sort": 1,
      "icon": "Setting",
      "createTime": "2024-01-01T10:00:00",
      "updateTime": "2024-01-01T10:00:00",
      "deleted": 0
    }
  ]
}
```

**字段说明**

| type 值 | 说明 |
|---------|------|
| 0 | 目录 |
| 1 | 菜单 |
| 2 | 按钮 |

---

### 2. 获取菜单详情

**请求**

```
GET /api/menus/{id}
Authorization: Bearer <token>
```

**路径参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 菜单ID |

**响应示例**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 2,
    "parentId": 1,
    "menuName": "用户管理",
    "path": "/system/user",
    "component": "/system/User",
    "permission": "user:list",
    "type": 1,
    "sort": 1,
    "icon": "User",
    "createTime": "2024-01-01T10:00:00",
    "updateTime": "2024-01-01T10:00:00",
    "deleted": 0
  }
}
```

---

### 3. 创建菜单

**请求**

```
POST /api/menus
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| parentId | long | 否 | 父级菜单ID |
| menuName | string | 是 | 菜单名称 |
| path | string | 否 | 菜单路径 |
| component | string | 否 | 组件路径 |
| permission | string | 否 | 权限标识 |
| type | int | 否 | 类型（0-目录，1-菜单，2-按钮） |
| sort | int | 否 | 排序 |
| icon | string | 否 | 图标 |

**请求示例**

```json
{
  "parentId": 1,
  "menuName": "角色管理",
  "path": "/system/role",
  "component": "/system/Role",
  "permission": "role:list",
  "type": 1,
  "sort": 2,
  "icon": "Grid"
}
```

**响应示例**

```json
{
  "code": 200,
  "msg": "创建成功",
  "data": {
    "id": 5,
    "parentId": 1,
    "menuName": "角色管理",
    "path": "/system/role",
    "component": "/system/Role",
    "permission": "role:list",
    "type": 1,
    "sort": 2,
    "icon": "Grid",
    "createTime": "2024-01-15T17:00:00",
    "updateTime": "2024-01-15T17:00:00",
    "deleted": 0
  }
}
```

---

### 4. 更新菜单

**请求**

```
PUT /api/menus
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 菜单ID |
| parentId | long | 否 | 父级菜单ID |
| menuName | string | 否 | 菜单名称 |
| path | string | 否 | 菜单路径 |
| component | string | 否 | 组件路径 |
| permission | string | 否 | 权限标识 |
| type | int | 否 | 类型 |
| sort | int | 否 | 排序 |
| icon | string | 否 | 图标 |

**请求示例**

```json
{
  "id": 5,
  "menuName": "更新后的角色管理",
  "sort": 3
}
```

**响应示例**

```json
{
  "code": 200,
  "msg": "更新成功",
  "data": {
    "id": 5,
    "parentId": 1,
    "menuName": "更新后的角色管理",
    "path": "/system/role",
    "component": "/system/Role",
    "permission": "role:list",
    "type": 1,
    "sort": 3,
    "icon": "Grid",
    "createTime": "2024-01-15T17:00:00",
    "updateTime": "2024-01-15T17:30:00",
    "deleted": 0
  }
}
```

---

### 5. 删除菜单

**请求**

```
DELETE /api/menus/{id}
Authorization: Bearer <token>
```

**路径参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 菜单ID |

**响应示例**

```json
{
  "code": 200,
  "msg": "删除成功",
  "data": null
}
```

---

## 日志管理 `/api/logs`

### 1. 日志列表（分页）

**请求**

```
GET /api/logs/list
Authorization: Bearer <token>
```

**Query 参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | int | 否 | 页码，默认1 |
| pageSize | int | 否 | 每页条数，默认10 |
| username | string | 否 | 用户名（模糊搜索） |
| operationType | string | 否 | 操作类型 |

**响应示例**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "total": 100,
    "records": [
      {
        "id": 1,
        "userId": 1,
        "username": "admin",
        "operationType": "CREATE",
        "operationContent": "创建用户",
        "method": "com.example.usermanagement.controller.UserController.create()",
        "params": "{\"username\":\"test\",\"phone\":\"13800138000\"}",
        "ip": "127.0.0.1",
        "createTime": "2024-01-15T10:00:00",
        "time": 150,
        "status": 0,
        "errorMsg": null
      }
    ]
  }
}
```

**字段说明**

| 字段 | 类型 | 说明 |
|------|------|------|
| status | int | 0-成功，1-失败 |
| time | long | 消耗时间（毫秒） |
| errorMsg | string | 错误消息（失败时） |

---

## 数据模型

### SysUser 用户实体

| 字段 | 类型 | 说明 |
|------|------|------|
| id | long | 用户ID |
| username | string | 用户名 |
| phone | string | 手机号 |
| password | string | 密码（加密） |
| roleId | long | 角色ID |
| createTime | datetime | 创建时间 |
| updateTime | datetime | 更新时间 |
| deleted | int | 删除标志（0-未删除，1-已删除） |

### SysRole 角色实体

| 字段 | 类型 | 说明 |
|------|------|------|
| id | long | 角色ID |
| roleName | string | 角色名称 |
| roleCode | string | 角色标识 |
| description | string | 角色描述 |
| createTime | datetime | 创建时间 |
| updateTime | datetime | 更新时间 |
| deleted | int | 删除标志 |

### SysMenu 菜单实体

| 字段 | 类型 | 说明 |
|------|------|------|
| id | long | 菜单ID |
| parentId | long | 父级ID |
| menuName | string | 菜单名称 |
| path | string | 路由路径 |
| component | string | 组件路径 |
| permission | string | 权限标识 |
| type | int | 类型（0-目录，1-菜单，2-按钮） |
| sort | int | 排序 |
| icon | string | 图标 |
| createTime | datetime | 创建时间 |
| updateTime | datetime | 更新时间 |
| deleted | int | 删除标志 |

### SysLog 日志实体

| 字段 | 类型 | 说明 |
|------|------|------|
| id | long | 日志ID |
| userId | long | 操作用户ID |
| username | string | 操作用户名 |
| operationType | string | 操作类型 |
| operationContent | string | 操作内容 |
| method | string | 请求方法 |
| params | string | 请求参数 |
| ip | string | IP地址 |
| createTime | datetime | 操作时间 |
| time | long | 消耗时间（毫秒） |
| status | int | 状态（0-成功，1-失败） |
| errorMsg | string | 错误消息 |

---

## 错误码

| code | 说明 |
|------|------|
| 200 | 操作成功 |
| 500 | 操作失败/服务器错误 |

---

## 认证说明

以下接口无需认证即可访问：
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `GET /api/auth/wechat/qrcode` - 获取微信登录二维码
- `GET /api/auth/wechat/callback` - 微信登录回调

其他所有接口均需要认证。请在请求头中添加：

```
Authorization: Bearer <token>
```

其中 `<token>` 为登录接口返回的 JWT 令牌。

**示例**

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.xxx
```
