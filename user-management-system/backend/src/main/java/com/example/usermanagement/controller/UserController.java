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

/**
 * 用户管理控制器
 *
 * 提供用户相关的 RESTful API 接口。
 *
 * 接口列表：
 * - GET    /api/users/list     - 分页获取用户列表
 * - GET    /api/users/{id}    - 获取用户详情
 * - POST   /api/users         - 创建用户
 * - PUT    /api/users         - 更新用户
 * - DELETE /api/users/{id}    - 删除用户
 *
 * 认证说明：
 * - 所有接口都需要登录后访问
 * - Token 放在 Authorization Header 中：Bearer xxx
 *
 * @see com.example.usermanagement.service.UserService 用户服务
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * 用户服务
     */
    @Autowired
    private UserService userService;

    /**
     * 分页获取用户列表
     *
     * @param pageNum 页码（默认1）
     * @param pageSize 每页大小（默认10）
     * @param username 用户名（可选，用于筛选）
     * @param phone 手机号（可选，用于筛选）
     * @return 分页后的用户列表
     *
     * GET /api/users/list?pageNum=1&pageSize=10&username=admin
     */
    @GetMapping("/list")
    public Result<PageResult<SysUser>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String phone) {
        // 创建分页对象
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        // 调用服务获取数据
        PageResult<SysUser> result = userService.listUsers(page, username, phone);
        return Result.success(result);
    }

    /**
     * 获取用户详情
     *
     * @param id 用户ID
     * @return 用户信息
     *
     * GET /api/users/1
     */
    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        SysUser user = userService.getUserById(id);
        return Result.success(user);
    }

    /**
     * 创建用户
     *
     * @param dto 创建用户的数据
     * @return 创建的用户信息
     *
     * POST /api/users
     * Body: {"username": "test", "phone": "13800138000", "password": "xxx", "roleId": 2}
     */
    @PostMapping
    public Result<SysUser> create(@RequestBody CreateUserDto dto) {
        SysUser user = userService.createUser(dto);
        return Result.success("创建成功", user);
    }

    /**
     * 更新用户
     *
     * @param dto 更新用户的数据
     * @return 更新后的用户信息
     *
     * PUT /api/users
     * Body: {"id": 1, "username": "newName", "phone": "13900139000"}
     */
    @PutMapping
    public Result<SysUser> update(@RequestBody UpdateUserDto dto) {
        SysUser user = userService.updateUser(dto);
        return Result.success("更新成功", user);
    }

    /**
     * 删除用户（逻辑删除）
     *
     * @param id 用户ID
     * @return 操作结果
     *
     * DELETE /api/users/1
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("删除成功");
    }
}
