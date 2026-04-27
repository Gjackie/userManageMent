package com.example.usermanagement.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT（JSON Web Token）工具类
 *
 * JWT 是一种开放标准（RFC 7519），用于在各方之间安全地传输信息。
 * 本项目使用 JWT 实现无状态身份认证。
 *
 * JWT 结构（由三部分组成，用点号分隔）：
 * - Header（头部）：包含令牌类型和签名算法
 * - Payload（载荷）：包含声明（claims），如用户信息
 * - Signature（签名）：用于验证消息的完整性
 *
 * 示例 JWT：xxxxx.yyyyy.zzzzz
 *
 * 本项目 JWT 配置：
 * - 签名算法：HS256（HMAC using SHA-256）
 * - 过期时间：7200 秒（2 小时）
 * - 存储的用户信息：userId、username
 *
 * 安全说明：
 * - JWT 存储在客户端（localStorage），每次请求携带在 Header 中
 * - 密钥必须足够长（至少 256 位）以支持 HS256 算法
 * - Token 过期后需要重新登录获取新 Token
 *
 * @see <a href="https://jwt.io/">JWT官网</a>
 */
@Component
public class JwtUtil {

    /**
     * JWT 签名密钥
     * 从配置文件读取，必须足够长以支持 HS256 算法
     * 配置文件路径：application.yml -> jwt.secret
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Token 过期时间（秒）
     * 从配置文件读取，默认 7200 秒（2小时）
     * 配置文件路径：application.yml -> jwt.expire
     */
    @Value("${jwt.expire}")
    private Long expire;

    // ==================== 核心方法 ====================

    /**
     * 生成 JWT Token
     *
     * 将用户信息（userId、username）编码到 Token 中，
     * Token 包含发布时间和过期时间。
     *
     * @param userId 用户ID
     * @param username 用户名
     * @return 签名的 JWT Token 字符串
     *
     * @see #getClaimsFromToken(String)
     */
    public String generateToken(Long userId, String username) {
        // 当前时间
        Date now = new Date();
        // 计算过期时间：当前时间 + 过期时长
        Date expiration = new Date(now.getTime() + expire * 1000);

        // 构建 JWT Token
        // - setSubject：设置主题（用户标识）
        // - claim：添加自定义claims（用户ID）
        // - setIssuedAt：设置签发时间
        // - setExpiration：设置过期时间
        // - signWith：设置签名算法和密钥
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从 Token 中解析 Claims（声明）
     *
     * Claims 包含 JWT 中存储的所有用户信息。
     * 如果 Token 无效（签名不匹配、已过期等），返回 null。
     *
     * @param token JWT Token 字符串
     * @return Claims 对象，如果解析失败返回 null
     */
    public Claims getClaimsFromToken(String token) {
        try {
            // 解析 Token 并获取 Claims
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // 解析失败返回 null，不抛出异常以便后续处理
            return null;
        }
    }

    // ==================== 验证方法 ====================

    /**
     * 验证 Token 是否过期
     *
     * @param token JWT Token 字符串
     * @return true-已过期，false-未过期
     */
    public Boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            // Token 无效，视为已过期
            return true;
        }
        final Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 验证 Token 是否有效
     *
     * @param token JWT Token 字符串
     * @return true-有效，false-无效（过期或格式错误）
     */
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    // ==================== 信息获取方法 ====================

    /**
     * 从 Token 中获取用户名
     *
     * @param token JWT Token 字符串
     * @return 用户名，如果解析失败返回 null
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    /**
     * 从 Token 中获取用户ID
     *
     * @param token JWT Token 字符串
     * @return 用户ID，如果解析失败返回 null
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.get("userId", Long.class) : null;
    }
}
