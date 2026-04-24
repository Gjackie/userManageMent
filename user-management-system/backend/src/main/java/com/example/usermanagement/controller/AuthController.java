package com.example.usermanagement.controller;

import com.example.usermanagement.common.Result;
import com.example.usermanagement.dto.LoginDto;
import com.example.usermanagement.entity.SysUser;
import com.example.usermanagement.mapper.SysUserMapper;
import com.example.usermanagement.security.SecurityUser;
import com.example.usermanagement.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SysUserMapper sysUserMapper;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        String token = jwtUtil.generateToken(securityUser.getId(), securityUser.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", securityUser.getId());
        data.put("username", securityUser.getUsername());
        return Result.success("登录成功", data);
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        SysUser user = sysUserMapper.selectById(securityUser.getId());
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("phone", user.getPhone());
        data.put("roleId", user.getRoleId());
        return Result.success(data);
    }
}
