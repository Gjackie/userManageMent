package com.example.usermanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usermanagement.entity.SysRole;

import java.util.List;

/**
 * 角色服务接口
 *
 * 定义角色管理的业务逻辑方法。
 *
 * @see com.example.usermanagement.service.impl.RoleServiceImpl
 */
public interface RoleService {

    /**
     * 获取所有角色列表
     *
     * @return 角色列表
     */
    List<SysRole> listRoles();

    /**
     * 根据ID获取角色详情
     *
     * @param id 角色ID
     * @return 角色对象
     */
    SysRole getRoleById(Long id);

    /**
     * 创建新角色
     *
     * @param role 角色对象
     * @return 创建的角色对象
     */
    SysRole createRole(SysRole role);

    /**
     * 更新角色
     *
     * @param role 角色对象
     * @return 更新后的角色对象
     */
    SysRole updateRole(SysRole role);

    /**
     * 删除角色（逻辑删除）
     *
     * @param id 角色ID
     */
    void deleteRole(Long id);
}
