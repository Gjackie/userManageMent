package com.example.usermanagement.security;

import com.example.usermanagement.entity.SysUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 安全用户类 - 实现 Spring Security 的 UserDetails 接口
 *
 * 作用：
 * 将业务系统的 SysUser 用户实体类与 Spring Security 的用户认证体系对接。
 * 作为 Spring Security 上下文中的用户信息载体。
 *
 * 设计原理：
 * - UserDetails 是 Spring Security 的核心接口之一
 * - 代表认证后的用户详情，包含用户名、密码、权限等信息
 * - 在用户登录成功后，Spring Security 会将 UserDetails 存入安全上下文
 * - 后续的请求可以通过 SecurityContextHolder 获取当前用户信息
 *
 * 与 SysUser 的关系：
 * - SecurityUser 是 SysUser 的安全封装类
 * - 构造函数将 SysUser 的数据复制到 SecurityUser
 * - 在认证流程中使用，认证成功后存储在安全上下文中
 *
 * @see UserDetailsServiceImpl 用户认证服务
 * @see JwtAuthenticationFilter JWT认证过滤器
 */
@Data
public class SecurityUser implements UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID - 与 SysUser.id 对应
     */
    private Long id;

    /**
     * 用户名 - 登录账号
     */
    private String username;

    /**
     * 密码 - 已加密的密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 用户权限集合
     * Spring Security 根据此字段判断用户可以访问哪些资源
     * 通常格式：ROLE_USER、ROLE_ADMIN、user:add 等
     */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * 构造函数 - 从 SysUser 创建 SecurityUser
     *
     * 在用户登录认证时调用，将数据库中的用户信息转换为安全用户对象
     *
     * @param sysUser 业务系统的用户实体
     */
    public SecurityUser(SysUser sysUser) {
        this.id = sysUser.getId();
        this.username = sysUser.getUsername();
        this.password = sysUser.getPassword();
        this.phone = sysUser.getPhone();
        this.roleId = sysUser.getRoleId();
    }

    // ==================== UserDetails 接口实现 ====================

    /**
     * 获取用户的权限列表
     *
     * @return 权限集合，如 [ROLE_USER, user:add, user:edit]
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 获取加密后的密码
     *
     * @return 密码字符串（BCrypt 加密后的哈希值）
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 获取用户名
     *
     * @return 用户名字符串
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 账户是否未过期
     *
     * @return true-未过期，false-已过期
     * 注：本项目不实现账户过期功能，固定返回 true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否未锁定
     *
     * @return true-未锁定，false-已锁定
     * 注：本项目不实现账户锁定功能，固定返回 true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 凭证（密码）是否未过期
     *
     * @return true-未过期，false-已过期
     * 注：本项目不实现凭证过期功能，固定返回 true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 用户是否启用
     *
     * @return true-启用，false-禁用
     * 注：本项目不实现用户禁用功能，固定返回 true
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
