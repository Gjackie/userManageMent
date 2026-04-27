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

/**
 * 用户服务实现类
 *
 * 实现 UserService 接口，处理用户相关的业务逻辑。
 *
 * @Transactional 说明：
 * - 默认情况下，unchecked 异常（RuntimeException）会触发事务回滚
 * - checked 异常不会触发回滚，需要通过 rollbackFor 指定
 * - 本类中的方法如果抛出 RuntimeException 会自动回滚
 *
 * @see UserService 接口
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * 用户 Mapper
     * 用于数据库操作
     */
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 密码加密器
     * 用于密码加密存储
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 分页查询用户列表
     *
     * 查询条件：
     * - 只查询未删除的用户（deleted = 0）
     * - 用户名模糊匹配（如果提供了 username）
     * - 手机号模糊匹配（如果提供了 phone）
     * - 按创建时间倒序排序
     *
     * @param page 分页对象
     * @param username 用户名（可选）
     * @param phone 手机号（可选）
     * @return 分页结果
     */
    @Override
    public PageResult<SysUser> listUsers(Page<SysUser> page, String username, String phone) {
        // 构建查询条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getDeleted, 0);  // 只查询未删除用户

        // 用户名模糊查询
        if (StringUtils.hasText(username)) {
            wrapper.like(SysUser::getUsername, username);
        }

        // 手机号模糊查询
        if (StringUtils.hasText(phone)) {
            wrapper.like(SysUser::getPhone, phone);
        }

        // 按创建时间倒序
        wrapper.orderByDesc(SysUser::getCreateTime);

        // 执行分页查询
        Page<SysUser> result = sysUserMapper.selectPage(page, wrapper);

        // 转换为 PageResult
        return PageResult.of(result);
    }

    /**
     * 根据ID获取用户详情
     *
     * @param id 用户ID
     * @return 用户对象
     * @throws RuntimeException 如果用户不存在
     */
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

    /**
     * 创建新用户
     *
     * @param dto 创建用户的数据
     * @return 创建的用户（不含密码）
     * @throws RuntimeException 如果用户名已存在
     */
    @Override
    public SysUser createUser(CreateUserDto dto) {
        // 检查用户名唯一性
        Long count = sysUserMapper.selectCount(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, dto.getUsername())
                        .eq(SysUser::getDeleted, 0)
        );
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建用户对象
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));  // BCrypt加密
        user.setRoleId(dto.getRoleId() != null ? dto.getRoleId() : 2L);  // 默认普通用户
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setDeleted(0);

        // 插入数据库
        sysUserMapper.insert(user);

        // 返回前清空密码
        user.setPassword(null);
        return user;
    }

    /**
     * 更新用户信息
     *
     * 只更新有值的字段，null 字段保持不变。
     *
     * @param dto 更新用户的数据
     * @return 更新后的用户（不含密码）
     * @throws RuntimeException 如果用户不存在
     */
    @Override
    public SysUser updateUser(UpdateUserDto dto) {
        // 先获取原用户
        SysUser user = getUserById(dto.getId());

        // 只更新有值的字段
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

        // 更新数据库
        sysUserMapper.updateById(user);

        // 返回前清空密码
        user.setPassword(null);
        return user;
    }

    /**
     * 删除用户（逻辑删除）
     *
     * 将 deleted 字段设为 1，而不是真正从数据库删除。
     *
     * @param id 用户ID
     * @throws RuntimeException 如果用户不存在
     */
    @Override
    public void deleteUser(Long id) {
        SysUser user = getUserById(id);
        user.setDeleted(1);           // 标记为已删除
        user.setUpdateTime(LocalDateTime.now());
        sysUserMapper.updateById(user);
    }
}
