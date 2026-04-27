package com.example.usermanagement.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * CORS（跨域资源共享）配置类
 *
 * 问题背景：
 * 浏览器出于安全考虑，采用同源策略（Same-Origin Policy）。
 * 当前端页面（http://localhost:5173）向后端（http://localhost:8080）发起请求时，
 * 由于协议、域名、端口不同，属于跨域请求。
 *
 * 解决方案：
 * CORS 是一种 W3C 标准，允许服务器声明哪些来源可以访问其资源。
 * 服务器通过在响应头中添加特定的 CORS 字段来授权跨域访问。
 *
 * 本配置的作用：
 * 1. 允许前端开发环境（localhost:5173）访问后端 API
 * 2. 允许携带认证信息（cookies）
 * 3. 缓存预检请求结果，减少OPTIONS请求
 *
 * 常见 HTTP 方法与跨域：
 * - GET、POST：简单请求，直接发送
 * - PUT、DELETE：复杂请求，先发预检（OPTIONS）请求询问服务器是否允许
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS">CORS 详解</a>
 */
@Configuration
public class CorsConfig {

    /**
     * 创建 CORS 过滤器
     *
     * @return CorsFilter 过滤器实例
     */
    @Bean
    public CorsFilter corsFilter() {
        // 创建 CORS 配置对象
        CorsConfiguration config = new CorsConfiguration();

        // ==================== 核心配置 ====================

        /**
         * 允许的来源（Origin）
         * 使用 patterns 格式，支持通配符 *
         * 开发环境：
         * - http://localhost:* （Vite 默认端口 5173）
         * - http://127.0.0.1:*
         *
         * 生产环境应改为实际的域名，如：
         * - https://your-domain.com
         */
        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:*", "http://127.0.0.1:*"));

        /**
         * 允许的 HTTP 方法
         * - GET：获取资源
         * - POST：创建资源
         * - PUT：更新资源
         * - DELETE：删除资源
         * - OPTIONS：预检请求（查看服务器支持的请求方法）
         */
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        /**
         * 允许的请求头
         * - * 表示允许所有请求头
         * - 常用请求头：
         *   - Content-Type：请求体类型
         *   - Authorization：认证信息（JWT Token）
         *   - X-Requested-With：Ajax 请求标识
         */
        config.setAllowedHeaders(Arrays.asList("*"));

        /**
         * 是否允许携带凭证
         * - true：允许浏览器携带 cookies、Authorization header 等凭证
         * - 这使得登录状态可以在跨域请求中保持
         */
        config.setAllowCredentials(true);

        /**
         * 预检请求的缓存时间（秒）
         * - 3600L = 1小时
         * - 在此时间内，浏览器不会再次发送 OPTIONS 预检请求
         * - 减少不必要的预检请求，提升性能
         */
        config.setMaxAge(3600L);

        // 创建 CORS 配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有路径应用此 CORS 配置
        source.registerCorsConfiguration("/**", config);

        // 返回 CORS 过滤器
        return new CorsFilter(source);
    }

    /**
     * 注册 CORS 过滤器（优先级最高）
     *
     * @return 过滤器注册Bean
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();

        // 设置我们创建的 CORS 过滤器
        registration.setFilter(corsFilter());

        // 拦截所有请求
        registration.addUrlPatterns("/*");

        // 设置优先级为最高
        // 确保 CORS 过滤器在其他过滤器之前执行
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return registration;
    }
}
