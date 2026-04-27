package com.example.usermanagement.controller;

import com.example.usermanagement.common.Result;
import com.example.usermanagement.dto.LoginDto;
import com.example.usermanagement.dto.RegisterDto;
import com.example.usermanagement.dto.WechatLoginDto;
import com.example.usermanagement.entity.SysUser;
import com.example.usermanagement.mapper.SysUserMapper;
import com.example.usermanagement.security.SecurityUser;
import com.example.usermanagement.service.WechatLoginService;
import com.example.usermanagement.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 认证控制器
 *
 * 提供用户认证相关的接口：
 * - 登录：用户名密码登录
 * - 注册：用户自主注册
 * - 用户信息：获取当前登录用户信息
 * - 微信登录：微信扫码登录
 *
 * 认证机制：
 * - 使用 JWT（JSON Web Token）实现无状态认证
 * - 登录成功后返回 Token，客户端后续请求携带此 Token
 * - Token 放在 Authorization Header 中：Bearer xxx
 *
 * @see com.example.usermanagement.utils.JwtUtil JWT工具类
 * @see com.example.usermanagement.security.JwtAuthenticationFilter JWT过滤器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * 认证管理器
     * 用于验证用户名密码
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * JWT 工具类
     * 用于生成和验证 Token
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户 Mapper
     */
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 密码加密器
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 微信登录服务
     */
    @Autowired
    private WechatLoginService wechatLoginService;

    /**
     * 用户登录
     *
     * 流程：
     * 1. 验证用户名密码
     * 2. 生成 JWT Token
     * 3. 返回 Token 和用户信息
     *
     * @param loginDto 登录信息（用户名、密码）
     * @return Token 和用户信息
     *
     * POST /api/auth/login
     * Body: {"username": "admin", "password": "admin123"}
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDto loginDto) {
        // 使用 Spring Security 的 AuthenticationManager 验证用户名密码
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );

        // 获取认证后的用户信息
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        // 生成 JWT Token
        String token = jwtUtil.generateToken(securityUser.getId(), securityUser.getUsername());

        // 构建返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", securityUser.getId());
        data.put("username", securityUser.getUsername());

        return Result.success("登录成功", data);
    }

    /**
     * 获取当前登录用户信息
     *
     * @param authentication Spring Security 自动注入的认证信息
     * @return 用户详细信息
     *
     * GET /api/auth/info
     * Header: Authorization: Bearer xxx
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo(Authentication authentication) {
        // 从认证信息中获取用户
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        // 从数据库查询完整用户信息
        SysUser user = sysUserMapper.selectById(securityUser.getId());

        // 构建返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("phone", user.getPhone());
        data.put("roleId", user.getRoleId());

        return Result.success(data);
    }

    /**
     * 用户注册
     *
     * 流程：
     * 1. 验证两次密码是否一致
     * 2. 检查用户名是否已存在
     * 3. 创建用户记录
     * 4. 生成 JWT Token
     * 5. 返回 Token 和用户信息
     *
     * @param registerDto 注册信息
     * @return Token 和用户信息
     *
     * POST /api/auth/register
     * Body: {"username": "test", "phone": "13800138000", "password": "xxx", "confirmPassword": "xxx"}
     */
    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody RegisterDto registerDto) {
        // 验证两次密码是否一致
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            return Result.error("两次密码输入不一致");
        }

        // 检查用户名唯一性
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysUser> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, registerDto.getUsername())
               .eq(SysUser::getDeleted, 0);
        Long count = sysUserMapper.selectCount(wrapper);
        if (count > 0) {
            return Result.error("用户名已存在");
        }

        // 创建用户
        SysUser user = new SysUser();
        user.setUsername(registerDto.getUsername());
        user.setPhone(registerDto.getPhone());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoleId(2L);  // 默认普通用户
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setDeleted(0);
        sysUserMapper.insert(user);

        // 生成 Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        // 构建返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("phone", user.getPhone());

        return Result.success("注册成功", data);
    }

    /**
     * 获取微信登录二维码链接
     *
     * @return 微信二维码 URL 和场景值
     *
     * GET /api/auth/wechat/qrcode
     */
    @GetMapping("/wechat/qrcode")
    public Result<Map<String, String>> getWechatQrCode() {
        Map<String, String> qrCodeData = wechatLoginService.getQrCodeUrl();
        return Result.success(qrCodeData);
    }

    /**
     * 微信登录回调
     *
     * 微信扫码后会回调此接口，携带授权码。
     * 根据授权码换取用户信息。
     *
     * @param code 微信授权码
     * @param state 场景值（用于防止 CSRF 攻击）
     * @return 登录结果
     *
     * GET /api/auth/wechat/callback?code=xxx&state=xxx
     */
    @GetMapping("/wechat/callback")
    public Result<Map<String, Object>> wechatCallback(@RequestParam String code, @RequestParam String state) {
        Map<String, Object> result = wechatLoginService.callback(code);
        if ((Boolean) result.get("success")) {
            return Result.success("微信登录成功", result);
        }
        return Result.error((String) result.get("message"));
    }
}
