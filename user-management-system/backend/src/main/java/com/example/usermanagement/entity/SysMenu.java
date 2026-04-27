package com.example.usermanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜单/权限表实体类
 *
 * 对应数据库中的 sys_menu 表，存储系统的菜单和权限信息。
 * 采用树形结构设计，支持多级菜单，用于：
 * - 前端动态菜单渲染
 * - 按钮级别权限控制
 * - API 访问权限控制
 *
 * 菜单类型说明（type 字段）：
 * - type = 0：目录 - 菜单分组，不对应具体页面
 * - type = 1：菜单 - 对应具体的页面或功能
 * - type = 2：按钮 - 对应页面上的具体操作（如：添加、编辑、删除）
 *
 * 树形结构说明：
 * - parent_id = 0：表示顶级菜单（根节点）
 * - parent_id > 0：表示子菜单，指向父级菜单的 ID
 *
 * permission 字段（权限标识）格式示例：
 * - "user:add" - 添加用户
 * - "user:edit" - 编辑用户
 * - "user:delete" - 删除用户
 * - "user:list" - 查看用户列表
 *
 * @see SysRole 角色表
 */
@Data
@TableName("sys_menu")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID - 主键自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父级菜单ID
     * - 0：表示顶级菜单（根节点）
     * - > 0：指向父级菜单的 ID
     *
     * 通过 parent_id 构建树形结构，支持无限层级嵌套
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 菜单名称 - 显示在界面上的名称
     * 例如："用户管理"、"角色管理"、"添加用户"
     */
    @TableField("menu_name")
    private String menuName;

    /**
     * 菜单路径 - URL 路径
     * 例如："/users"、"/roles/list"
     * 用于前端路由匹配和后端权限验证
     */
    @TableField("path")
    private String path;

    /**
     * 组件路径 - 前端 Vue 组件的路径
     * 例如："User.vue"、"Role/List.vue"
     * 用于动态加载前端页面组件
     */
    @TableField("component")
    private String component;

    /**
     * 权限标识 - 程序化使用的权限码
     * 格式：资源:操作，如 "user:add"
     * 用于后端 API 权限拦截和前端按钮级别控制
     */
    @TableField("permission")
    private String permission;

    /**
     * 菜单类型
     * - 0：目录（用于菜单分组，不对应具体页面）
     * - 1：菜单（对应具体的功能页面）
     * - 2：按钮（对应具体操作，如添加、编辑、删除）
     */
    @TableField("type")
    private Integer type;

    /**
     * 排序号 - 用于菜单的显示顺序
     * 数字越小排序越靠前，相同数字按创建时间排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 图标 - 菜单显示的图标
     * 使用 Element Plus 图标库的名称，如："el-icon-user"
     */
    @TableField("icon")
    private String icon;

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
     * 逻辑删除，保护系统菜单数据
     */
    @TableField("deleted")
    private Integer deleted;
}
