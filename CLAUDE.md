# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在本代码库中工作时提供指导。

## 项目概述

这是一个基于 Spring Boot 2.7.0 + Vue 3 的用户管理系统，包含：
- **后端** - Spring Boot 2.7.0，JWT 认证，MyBatis Plus ORM，Spring Security
- **前端** - Vue 3 + Element Plus + Vite

## 项目结构

```
user-management-system/
├── backend/              # Spring Boot 后端
│   ├── src/main/java/
│   │   └── com/example/usermanagement/
│   │       ├── controller/   # REST 控制器
│   │       ├── service/      # 业务逻辑
│   │       ├── mapper/       # 数据访问层
│   │       ├── entity/       # 实体类
│   │       ├── dto/          # 数据传输对象
│   │       ├── security/     # JWT 认证
│   │       ├── config/       # 配置类
│   │       └── utils/        # 工具类
│   └── src/main/resources/
│       ├── application.yml   # 应用配置
│       └── sql/init.sql      # 数据库初始化
└── frontend/             # Vue 3 前端
    ├── src/
    │   ├── api/          # API 接口
    │   ├── router/       # 路由配置
    │   ├── stores/       # 状态管理
    │   └── views/        # 页面组件
    └── README.md         # 前端文档
```

## 构建与运行命令

### 后端

```bash
cd user-management-system/backend

# 构建
mvn clean package -DskipTests

# 运行
mvn spring-boot:run

# 测试
mvn test
mvn test -Dtest=UserServiceTest
```

### 前端

```bash
cd user-management-system/frontend

# 安装依赖
npm install

# 开发环境运行
npm run dev

# 生产构建
npm run build

# 预览
npm run preview
```

## 架构

**后端分层结构：**
- `controller/` - 处理 HTTP 请求的 REST 控制器（Auth、User、Role、Log）
- `service/` - 业务逻辑，实现类在 `impl/` 目录下
- `mapper/` - MyBatis Plus 数据访问层
- `entity/` - JPA/MyBatis Plus 实体类
- `dto/` - 请求/响应数据传输对象
- `security/` - JWT 过滤器、UserDetailsService 实现
- `config/` - 安全配置、AOP 日志记录和 MyBatis Plus 配置
- `utils/` - JWT 工具类

**前端技术栈：**
- Vue 3 + Composition API (`<script setup>`)
- Vue Router 4（路由管理）
- Pinia（状态管理）
- Element Plus（UI 组件库）
- Axios（HTTP 请求）

**关键模式：**
- JWT 认证（jjwt 0.11.x，HS256 签名）
- 无状态会话管理
- BCrypt 密码加密
- AOP 操作日志记录
- MyBatis Plus 逻辑删除（软删除）
- 请求/响应拦截器（自动添加 Token）

## 数据库表结构

| 表名 | 说明 |
|------|------|
| `sys_user` | 用户表（username, phone, password, role_id, deleted） |
| `sys_role` | 角色表（role_name, role_code, description） |
| `sys_menu` | RBAC 菜单表（parent_id, menu_name, path, component, permission, type） |
| `sys_log` | 操作日志表（user_id, operation_type, method, params, ip, time, status） |

## API 接口

| 接口 | 方法 | 说明 | 认证 |
|------|------|------|------|
| `/api/auth/login` | POST | 登录 | 否 |
| `/api/auth/register` | POST | 注册 | 否 |
| `/api/auth/info` | GET | 用户信息 | 是 |
| `/api/users` | GET | 用户列表（分页） | 是 |
| `/api/users` | POST | 创建用户 | 是 |
| `/api/users` | PUT | 更新用户 | 是 |
| `/api/users/{id}` | DELETE | 删除用户 | 是 |
| `/api/roles` | GET/POST | 角色列表/创建 | 是 |
| `/api/roles/{id}` | PUT/DELETE | 更新/删除角色 | 是 |
| `/api/menus` | GET/POST | 菜单列表/创建 | 是 |
| `/api/menus/{id}` | PUT/DELETE | 更新/删除菜单 | 是 |
| `/api/logs` | GET | 日志列表（分页） | 是 |

## 配置

**后端配置**（`backend/src/main/resources/application.yml`）：
- 服务器端口：8080
- 数据库：MySQL，localhost:3306/user_management
- JWT 密钥：`userManagementSecretKeyThatIsLongEnoughForHS256Algorithm`
- JWT 过期时间：7200 秒（2 小时）
- Eureka：已禁用

**前端配置**（`frontend/src/api/auth.js`）：
- API 地址：`http://localhost:8080/api`
- 开发服务器端口：5173

## 主要依赖

**后端：**
- Spring Boot 2.7.0（Web, Security, Validation, AOP）
- Spring Cloud Netflix Eureka Client
- Spring Cloud OpenFeign
- MyBatis Plus 3.5.2
- MySQL Connector 8.x
- jjwt 0.11.5
- Lombok

**前端：**
- Vue 3.x
- Vue Router 4.x
- Pinia 2.x
- Element Plus 2.x
- Axios 1.x
- Vite 8.x

## 默认账号

执行 `backend/src/main/resources/sql/init.sql` 后：
- 用户名：`admin`
- 密码：`admin123`
- 角色：`ADMIN`

## 注意事项

1. **Java 版本**：后端需要 JDK 17+
2. **数据库**：需要 MySQL 5.7+ 并创建 `user_management` 数据库
3. **跨域**：开发环境下前端 Vite 代理 API 请求，生产环境需后端配置 CORS
4. **Token**：存储在 localStorage，生产环境建议使用 httpOnly Cookie
