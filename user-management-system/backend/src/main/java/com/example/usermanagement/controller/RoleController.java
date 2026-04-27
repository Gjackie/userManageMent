package com.example.usermanagement.controller;

import com.example.usermanagement.common.Result;
import com.example.usermanagement.entity.SysRole;
import com.example.usermanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 *
 * 提供角色相关的 RESTful API 接口。
 *
 * 接口列表：
 * - GET    /api/roles/list     - 获取角色列表
 * - GET    /api/roles/{id}    - 获取角色详情
 * - POST   /api/roles         - 创建角色
 * - PUT    /api/roles         - 更新角色
 * - DELETE /api/roles/{id}    - 删除角色
 *
 * @see RoleService 角色服务
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 获取角色列表
     */
    @GetMapping("/list")
    public Result<List<SysRole>> list() {
        List<SysRole> roles = roleService.listRoles();
        return Result.success(roles);
    }

    /**
     * 获取角色详情
     */
    @GetMapping("/{id}")
    public Result<SysRole> getById(@PathVariable Long id) {
        SysRole role = roleService.getRoleById(id);
        return Result.success(role);
    }

    /**
     * 创建角色
     */
    @PostMapping
    public Result<SysRole> create(@RequestBody SysRole role) {
        SysRole created = roleService.createRole(role);
        return Result.success("创建成功", created);
    }

    /**
     * 更新角色
     */
    @PutMapping
    public Result<SysRole> update(@RequestBody SysRole role) {
        SysRole updated = roleService.updateRole(role);
        return Result.success("更新成功", updated);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success("删除成功");
    }
}
