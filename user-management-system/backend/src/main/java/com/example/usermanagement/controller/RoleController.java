package com.example.usermanagement.controller;

import com.example.usermanagement.common.Result;
import com.example.usermanagement.entity.SysRole;
import com.example.usermanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public Result<List<SysRole>> list() {
        List<SysRole> roles = roleService.listRoles();
        return Result.success(roles);
    }

    @GetMapping("/{id}")
    public Result<SysRole> getById(@PathVariable Long id) {
        SysRole role = roleService.getRoleById(id);
        return Result.success(role);
    }

    @PostMapping
    public Result<SysRole> create(@RequestBody SysRole role) {
        SysRole created = roleService.createRole(role);
        return Result.success("创建成功", created);
    }

    @PutMapping
    public Result<SysRole> update(@RequestBody SysRole role) {
        SysRole updated = roleService.updateRole(role);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success("删除成功");
    }
}
