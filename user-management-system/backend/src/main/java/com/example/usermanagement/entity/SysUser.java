package com.example.usermanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户表实体类
 *
 * 对应数据库中的 sys_user 表，存储系统用户信息。
 * 使用 MyBatis Plus 注解实现与数据库表的映射。
 *
 * 设计说明：
 * - 采用逻辑删除（deleted 字段），而非物理删除，保证数据安全性
 * - 密码使用 BCrypt 加密存储，不可明文保存
 * - 关联 role_id 用于 RBAC 权限控制
 *
 * @see SysRole 角色表
 */
@Data
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID - 主键自增
     * 使用 MyBatis Plus 的 IdType.AUTO 让数据库自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名 - 登录账号
     * 唯一约束，同一用户名不能重复注册
     */
    @TableField("username")
    private String username;

    /**
     * 手机号 - 用于登录或找回密码
     * 可选字段，长度11位
     */
    @TableField("phone")
    private String phone;

    /**
     * 密码（加密存储）
     * 使用 BCrypt 算法加密存储，永不明文显示
     */
    @TableField("password")
    private String password;

    /**
     * 角色ID - 外键关联 sys_role 表
     * - 1: 管理员(ADMIN)
     * - 2: 普通用户(USER)
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 创建时间 - 记录用户注册时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间 - 记录用户信息最后修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 删除标志（0-未删除，1-已删除）
     * 逻辑删除标记，删除用户时只修改此字段，不真正删除数据
     * MyBatis Plus 会自动过滤已删除的记录
     */
    @TableField("deleted")
    private Integer deleted;
}
