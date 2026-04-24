package com.example.usermanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usermanagement.common.PageResult;
import com.example.usermanagement.dto.CreateUserDto;
import com.example.usermanagement.dto.UpdateUserDto;
import com.example.usermanagement.entity.SysUser;
import com.example.usermanagement.mapper.SysUserMapper;
import com.example.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public PageResult<SysUser> listUsers(Page<SysUser> page, String username, String phone) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getDeleted, 0);
        if (StringUtils.hasText(username)) {
            wrapper.like(SysUser::getUsername, username);
        }
        if (StringUtils.hasText(phone)) {
            wrapper.like(SysUser::getPhone, phone);
        }
        wrapper.orderByDesc(SysUser::getCreateTime);
        Page<SysUser> result = sysUserMapper.selectPage(page, wrapper);
        return PageResult.of(result);
    }

    @Override
    public SysUser getUserById(Long id) {
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getId, id)
                        .eq(SysUser::getDeleted, 0)
        );
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }

    @Override
    public SysUser createUser(CreateUserDto dto) {
        // 检查用户名是否已存在
        Long count = sysUserMapper.selectCount(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, dto.getUsername())
                        .eq(SysUser::getDeleted, 0)
        );
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoleId(dto.getRoleId());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setDeleted(0);
        sysUserMapper.insert(user);
        user.setPassword(null);
        return user;
    }

    @Override
    public SysUser updateUser(UpdateUserDto dto) {
        SysUser user = getUserById(dto.getId());
        if (StringUtils.hasText(dto.getUsername())) {
            user.setUsername(dto.getUsername());
        }
        if (StringUtils.hasText(dto.getPhone())) {
            user.setPhone(dto.getPhone());
        }
        if (dto.getRoleId() != null) {
            user.setRoleId(dto.getRoleId());
        }
        user.setUpdateTime(LocalDateTime.now());
        sysUserMapper.updateById(user);
        user.setPassword(null);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        SysUser user = getUserById(id);
        user.setDeleted(1);
        user.setUpdateTime(LocalDateTime.now());
        sysUserMapper.updateById(user);
    }
}
