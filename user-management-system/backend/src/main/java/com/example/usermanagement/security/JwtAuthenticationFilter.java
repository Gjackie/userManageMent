package com.example.usermanagement.security;

import com.example.usermanagement.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 认证过滤器 - 拦截所有 HTTP 请求
 *
 * 作用：
 * 在每次请求到达目标 Controller 之前，尝试从请求 Header 中提取 JWT Token，
 * 验证 Token 的有效性，如果有效则将用户信息存入 Spring Security 上下文。
 *
 * 工作原理：
 * - 继承自 OncePerRequestFilter，保证每个请求只执行一次
 * - 是 Spring Security 过滤器链中的一环
 * - 在 UsernamePasswordAuthenticationFilter 之前执行
 *
 * 请求流程：
 * 1. 客户端发起请求，携带 Authorization Header
 * 2. 本过滤器从 Header 中提取 Bearer Token
 * 3. 验证 Token 是否有效（未过期、格式正确）
 * 4. 从 Token 中解析用户名
 * 5. 加载用户详情（UserDetails）
 * 6. 创建 Authentication 认证凭证，存入 SecurityContext
 * 7. 继续执行后续过滤器
 *
 * @see JwtUtil JWT工具类
 * @see SecurityConfig 安全配置类
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * JWT 工具类
     * 用于 Token 的生成、验证和解析
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户详情服务
     * 用于根据用户名加载完整的用户信息
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 过滤器核心方法 - 处理每个请求
     *
     * @param request HTTP 请求对象
     * @param response HTTP 响应对象
     * @param filterChain 过滤器链，继续传递请求
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 从请求中提取 JWT Token
        String token = getTokenFromRequest(request);

        // 2. 验证 Token 是否存在且有效
        if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
            // 3. 从 Token 中获取用户名
            String username = jwtUtil.getUsernameFromToken(token);

            // 4. 加载用户详情
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 5. 创建认证凭证
            // 参数说明：
            // - userDetails: 用户信息
            // - credentials: 凭证（这里为 null，因为 Token 已经是凭证）
            // - authorities: 用户权限
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            // 6. 设置请求详情（包含请求 IP、Session 等信息）
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 7. 将认证信息存入 SecurityContext
            // SecurityContext 是线程安全的，存储在 ThreadLocal 中
            // 后续的代码可以通过 SecurityContextHolder 获取当前用户信息
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 8. 继续执行过滤器链
        // 即使没有 Token 或 Token 无效，也要继续（可能是匿名访问的接口）
        filterChain.doFilter(request, response);
    }

    /**
     * 从 HTTP 请求中提取 JWT Token
     *
     * Token 的典型格式：Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
     * - "Bearer " 是前缀标识，表示 Token 类型
     * - 后面的字符串才是实际的 JWT Token
     *
     * @param request HTTP 请求对象
     * @return Token 字符串，如果不存在返回 null
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        // 从 Header 中获取 Authorization 字段
        String bearerToken = request.getHeader("Authorization");

        // 检查是否存在且以 "Bearer " 开头
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // 去掉 "Bearer " 前缀，返回实际的 Token
            return bearerToken.substring(7);
        }

        return null;
    }
}
