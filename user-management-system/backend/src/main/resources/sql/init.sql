-- 创建数据库
CREATE DATABASE IF NOT EXISTS user_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE user_management;

-- 用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    `role_id` BIGINT DEFAULT NULL COMMENT '角色ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色标识',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '角色描述',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 菜单/权限表
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父级ID',
    `menu_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
    `path` VARCHAR(200) DEFAULT NULL COMMENT '菜单路径',
    `component` VARCHAR(200) DEFAULT NULL COMMENT '组件路径',
    `permission` VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
    `type` TINYINT NOT NULL DEFAULT 1 COMMENT '类型（0-目录，1-菜单，2-按钮）',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `icon` VARCHAR(50) DEFAULT NULL COMMENT '图标',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志（0-未删除，1-已删除）',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单/权限表';

-- 操作日志表
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '操作用户ID',
    `username` VARCHAR(50) DEFAULT NULL COMMENT '操作用户名',
    `operation_type` VARCHAR(100) DEFAULT NULL COMMENT '操作类型',
    `operation_content` TEXT COMMENT '操作内容',
    `method` VARCHAR(10) DEFAULT NULL COMMENT '请求方法',
    `params` TEXT COMMENT '请求参数',
    `ip` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `time` BIGINT DEFAULT NULL COMMENT '消耗时间(毫秒)',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态（0-成功，1-失败）',
    `error_msg` TEXT COMMENT '错误消息',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ==================== 初始数据 ====================

-- 插入默认角色（管理员、普通用户）
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `description`) VALUES
(1, '系统管理员', 'ADMIN', '拥有所有权限'),
(2, '普通用户', 'USER', '基础权限');

-- 插入默认管理员用户（密码: admin123，BCrypt加密后）
INSERT INTO `sys_user` (`username`, `phone`, `password`, `role_id`) VALUES
('admin', '13800138000', '$2b$10$SsynMm/TUcHekZtKJOd5je00Jg7u8HjQ.f4zV7iLOS46zABqFbKzS', 1);

-- 插入默认菜单
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `permission`, `type`, `sort`, `icon`) VALUES
(1, 0, '系统管理', '/system', 'Layout', NULL, 0, 1, 'setting'),
(2, 1, '用户管理', '/system/user', 'system/user', 'system:user:list', 1, 1, 'user'),
(3, 1, '角色管理', '/system/role', 'system/role', 'system:role:list', 1, 2, 'peoples'),
(4, 1, '菜单管理', '/system/menu', 'system/menu', 'system:menu:list', 1, 3, 'tree-table'),
(5, 1, '日志管理', '/system/log', 'system/log', 'system:log:list', 1, 4, 'log');
