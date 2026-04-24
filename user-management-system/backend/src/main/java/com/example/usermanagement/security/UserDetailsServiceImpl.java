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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, username)
                        .eq(SysUser::getDeleted, 0)
        );
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        SecurityUser securityUser = new SecurityUser(sysUser);
        // 根据角色设置权限
        securityUser.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        return securityUser;
    }
}
