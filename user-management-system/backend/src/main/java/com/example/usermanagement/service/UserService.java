package com.example.usermanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usermanagement.common.PageResult;
import com.example.usermanagement.dto.CreateUserDto;
import com.example.usermanagement.dto.UpdateUserDto;
import com.example.usermanagement.entity.SysUser;

/**
 * 用户服务接口
 *
 * 定义用户管理的业务逻辑方法。
 * 采用接口+实现类的设计模式，便于：
 * 1. 面向接口编程，降低耦合
 * 2. 单元测试时可以使用 mock
 * 3. 后续可以容易地替换实现类
 *
 * 服务层职责：
 * 1. 处理业务逻辑
 * 2. 参数校验
 * 3. 调用 Mapper 层操作数据库
 * 4. 事务管理（在实现类中使用 @Transactional）
 *
 * @see com.example.usermanagement.service.impl.UserServiceImpl 实现类
 */
public interface UserService {

    /**
     * 分页查询用户列表
     *
     * @param page 分页对象（包含页码和每页大小）
     * @param username 用户名（模糊查询条件，可为 null）
     * @param phone 手机号（模糊查询条件，可为 null）
     * @return 分页结果，包含总记录数和当前页数据
     */
    PageResult<SysUser> listUsers(Page<SysUser> page, String username, String phone);

    /**
     * 根据ID获取用户详情
     *
     * @param id 用户ID
     * @return 用户对象
     * @throws RuntimeException 如果用户不存在
     */
    SysUser getUserById(Long id);

    /**
     * 创建新用户
     *
     * @param dto 创建用户的数据传输对象
     * @return 创建成功的用户对象（不含密码）
     * @throws RuntimeException 如果用户名已存在
     */
    SysUser createUser(CreateUserDto dto);

    /**
     * 更新用户信息
     *
     * @param dto 更新用户的数据传输对象
     * @return 更新后的用户对象（不含密码）
     * @throws RuntimeException 如果用户不存在
     */
    SysUser updateUser(UpdateUserDto dto);

    /**
     * 删除用户（逻辑删除）
     *
     * @param id 用户ID
     * @throws RuntimeException 如果用户不存在
     */
    void deleteUser(Long id);
}
