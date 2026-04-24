package com.example.usermanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usermanagement.common.PageResult;
import com.example.usermanagement.dto.CreateUserDto;
import com.example.usermanagement.dto.UpdateUserDto;
import com.example.usermanagement.entity.SysUser;

public interface UserService {
    PageResult<SysUser> listUsers(Page<SysUser> page, String username, String phone);
    SysUser getUserById(Long id);
    SysUser createUser(CreateUserDto dto);
    SysUser updateUser(UpdateUserDto dto);
    void deleteUser(Long id);
}
