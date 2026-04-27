package com.example.usermanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.usermanagement.entity.SysRole;
import com.example.usermanagement.mapper.SysRoleMapper;
import com.example.usermanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色服务实现类
 *
 * 实现 RoleService 接口，处理角色相关的业务逻辑。
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysRole> listRoles() {
        return sysRoleMapper.selectList(
                new LambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getDeleted, 0)
                        .orderByAsc(SysRole::getId)
        );
    }

    @Override
    public SysRole getRoleById(Long id) {
        SysRole role = sysRoleMapper.selectOne(
                new LambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getId, id)
                        .eq(SysRole::getDeleted, 0)
        );
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        return role;
    }

    @Override
    public SysRole createRole(SysRole role) {
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        role.setDeleted(0);
        sysRoleMapper.insert(role);
        return role;
    }

    @Override
    public SysRole updateRole(SysRole role) {
        SysRole existing = getRoleById(role.getId());
        existing.setRoleName(role.getRoleName());
        existing.setRoleCode(role.getRoleCode());
        existing.setDescription(role.getDescription());
        existing.setUpdateTime(LocalDateTime.now());
        sysRoleMapper.updateById(existing);
        return existing;
    }

    @Override
    public void deleteRole(Long id) {
        SysRole role = getRoleById(id);
        role.setDeleted(1);
        role.setUpdateTime(LocalDateTime.now());
        sysRoleMapper.updateById(role);
    }
}
