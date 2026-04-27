package com.example.usermanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色表实体类
 *
 * 对应数据库中的 sys_role 表，存储系统角色信息。
 * 用于 RBAC（Role-Based Access Control，基于角色的访问控制）权限管理。
 *
 * RBAC 模型说明：
 * - 用户（User）通过角色（Role）获得权限
 * - 角色（Role）关联多个菜单/权限（Menu）
 * - 一个用户可以有一个或多个角色（当前设计为单角色）
 *
 * 预定义角色：
 * - role_id = 1: 管理员(ADMIN) - 拥有所有权限
 * - role_id = 2: 普通用户(USER) - 仅有基本访问权限
 *
 * @see SysUser 用户表
 * @see SysMenu 菜单/权限表
 */
@Data
@TableName("sys_role")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID - 主键自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称 - 角色的中文显示名称
     * 例如："系统管理员"、"普通用户"
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 角色标识 - 角色的程序化标识，用于权限判断
     * 例如："ADMIN"、"USER"，通常使用大写下划线格式
     */
    @TableField("role_code")
    private String roleCode;

    /**
     * 角色描述 - 角色的详细说明
     * 用于后台管理展示，帮助管理员理解角色用途
     */
    @TableField("description")
    private String description;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 删除标志（0-未删除，1-已删除）
     * 逻辑删除，保护历史数据
     */
    @TableField("deleted")
    private Integer deleted;
}
