package com.example.usermanagement.security;

import com.example.usermanagement.entity.SysUser;
import com.example.usermanagement.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Spring Security 用户认证服务实现
 *
 * 核心职责：
 * 根据用户名加载用户详情，用于 Spring Security 的认证流程。
 *
 * 认证流程说明：
 * 1. 用户提交用户名和密码
 * 2. Spring Security 调用 loadUserByUsername(username)
 * 3. 本方法根据用户名查询数据库，获取用户信息
 * 4. 将用户信息封装为 UserDetails（SecurityUser）返回
 * 5. Spring Security 比对密码是否匹配
 * 6. 认证成功后，将 UserDetails 存入 SecurityContext
 *
 * 密码加密配合：
 * - 本方法只负责加载用户，不负责密码验证
 * - 密码验证由 Spring Security 的 DaoAuthenticationProvider 自动完成
 * - 验证时使用 SecurityConfig 中配置的 PasswordEncoder
 *
 * @see SecurityUser 用户详情封装类
 * @see SecurityConfig 安全配置类
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * 用户 Mapper 接口
     * 用于查询数据库中的用户信息
     */
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 根据用户名加载用户详情
     *
     * 这是 Spring Security 认证流程的核心方法。
     * 当用户登录时，Spring Security 会自动调用此方法。
     *
     * @param username 用户名（登录时输入的用户名）
     * @return UserDetails 用户详情对象
     * @throws UsernameNotFoundException 当用户不存在时抛出
     *
     * 实现逻辑：
     * 1. 使用 MyBatis Plus 的 LambdaQueryWrapper 构建查询条件
     * 2. 查询条件：用户名匹配且未删除
     * 3. 如果找不到用户，抛出 UsernameNotFoundException
     * 4. 如果找到用户，创建 SecurityUser 对象并返回
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 使用 Lambda 语法构建查询条件
        SysUser sysUser = sysUserMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, username)      // 用户名精确匹配
                        .eq(SysUser::getDeleted, 0)              // 只查询未删除的用户
        );

        // 用户不存在，抛出异常
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 创建安全用户对象
        SecurityUser securityUser = new SecurityUser(sysUser);

        // 设置用户权限
        // 目前简单设置为 ROLE_USER，实际项目中可以从角色表关联查询权限
        // TODO: 后续可以扩展为根据角色查询对应的菜单权限
        securityUser.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        return securityUser;
    }
}
