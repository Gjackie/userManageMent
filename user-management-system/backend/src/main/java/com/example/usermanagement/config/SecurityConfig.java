package com.example.usermanagement.config;

import com.example.usermanagement.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security 安全配置类
 *
 * 核心功能：
 * 1. 配置用户认证方式（数据库认证 + JWT）
 * 2. 配置密码加密策略（BCrypt）
 * 3. 配置请求授权规则（哪些接口需要登录）
 * 4. 配置跨域（CORS）
 * 5. 配置无状态会话（JWT 无需 session）
 *
 * 安全策略说明：
 * - CSRF 禁用：前后端分离项目，JWT 认证不需要 CSRF 防护
 * - 无状态会话：使用 JWT，不在服务端存储 session
 * - 过滤器顺序：JWT 过滤器在用户名密码过滤器之前
 *
 * @see JwtAuthenticationFilter JWT认证过滤器
 * @see BCryptPasswordEncoder 密码加密器
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * JWT 认证过滤器
     * 注入后添加到过滤器链中
     */
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 用户详情服务
     * 用于加载用户信息和权限
     */
    @Autowired
    private UserDetailsService userDetailsService;

    // ==================== Bean 配置 ====================

    /**
     * 密码加密器
     *
     * 使用 BCrypt 算法进行密码加密和验证。
     * BCrypt 的特点：
     * - 每次加密结果不同（因为内部生成随机盐）
     * - 加密是单向的，无法逆向解密
     * - 验证时只需提供明文密码和加密后的密码即可验证
     *
     * @return PasswordEncoder 实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证管理器
     *
     * 用于处理认证请求。
     * 在登录接口中，AuthenticationManager.authenticate() 用于验证用户凭据。
     *
     * @return AuthenticationManager 实例
     * @throws Exception 如果创建失败
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // ==================== 配置方法 ====================

    /**
     * 配置认证管理器
     *
     * 设置用户详情服务和密码加密器。
     * 这告诉 Security 如何获取用户信息和验证密码。
     *
     * @param auth 认证管理器构建器
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置用户详情服务和密码加密方式
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * 配置 HTTP 安全策略
     *
     * @param http HTTP 安全配置构建器
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()                                      // 禁用 CSRF（前后端分离使用 JWT 不需要）
            .cors()                                                // 启用 CORS
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 无状态会话（JWT 不需要 session）
            .and()
            .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()          // 认证接口允许所有人访问
                .antMatchers("/api/users/**").authenticated()     // 用户接口需要登录
                .antMatchers("/api/roles/**").authenticated()     // 角色接口需要登录
                .antMatchers("/api/logs/**").authenticated()      // 日志接口需要登录
                .anyRequest().authenticated()                      // 其他接口都需要登录
            .and()
            // 将 JWT 过滤器添加到用户名密码过滤器之前
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 配置 CORS（跨域资源共享）
     *
     * @return CORS 配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 允许的来源（开发环境允许 localhost）
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:*", "http://127.0.0.1:*"));

        // 允许的 HTTP 方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 允许的请求头（* 表示全部）
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // 是否允许携带凭证（cookies）
        configuration.setAllowCredentials(true);

        // 预检请求的缓存时间（秒）
        configuration.setMaxAge(3600L);

        // 创建 CORS 配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
