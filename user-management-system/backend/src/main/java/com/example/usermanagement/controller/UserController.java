package com.example.usermanagement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usermanagement.common.PageResult;
import com.example.usermanagement.common.Result;
import com.example.usermanagement.dto.CreateUserDto;
import com.example.usermanagement.dto.UpdateUserDto;
import com.example.usermanagement.entity.SysUser;
import com.example.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public Result<PageResult<SysUser>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String phone) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        PageResult<SysUser> result = userService.listUsers(page, username, phone);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        SysUser user = userService.getUserById(id);
        return Result.success(user);
    }

    @PostMapping
    public Result<SysUser> create(@RequestBody CreateUserDto dto) {
        SysUser user = userService.createUser(dto);
        return Result.success("创建成功", user);
    }

    @PutMapping
    public Result<SysUser> update(@RequestBody UpdateUserDto dto) {
        SysUser user = userService.updateUser(dto);
        return Result.success("更新成功", user);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("删除成功");
    }
}
