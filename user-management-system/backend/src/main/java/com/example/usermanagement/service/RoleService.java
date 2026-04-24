package com.example.usermanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usermanagement.entity.SysRole;

import java.util.List;

public interface RoleService {
    List<SysRole> listRoles();
    SysRole getRoleById(Long id);
    SysRole createRole(SysRole role);
    SysRole updateRole(SysRole role);
    void deleteRole(Long id);
}
