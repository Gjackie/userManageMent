package com.example.usermanagement.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置类
 *
 * MyBatis Plus 是 MyBatis 的增强工具，简化 CRUD 操作。
 * 本配置主要启用分页插件，支持分页查询。
 *
 * 核心功能：
 * 1. 分页插件：自动拦截查询请求，实现分页
 * 2. 逻辑删除：自动过滤已删除的记录
 *
 * 分页原理：
 * - MyBatis Plus 拦截执行的 SQL
 * - 在 SQL 末尾自动添加 LIMIT 和 OFFSET
 * - 同时查询总记录数
 *
 * 使用示例：
 * <pre>
 * // Controller
 * public PageResult<User> list(Page<User> page) {
 *     IPage<User> result = userMapper.selectPage(page, wrapper);
 *     return PageResult.of(result);
 * }
 * </pre>
 *
 * @see <a href="https://baomidou.com/pages/24112f/">MyBatis Plus 文档</a>
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 配置 MyBatis Plus 插件拦截器
     *
     * @return MybatisPlusInterceptor 实例
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 创建插件拦截器
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        /**
         * 添加分页内部拦截器
         *
         * DbType.MYSQL：指定数据库类型为 MySQL
         * - MyBatis Plus 根据数据库类型生成不同的分页 SQL
         * - MySQL 使用 LIMIT offset, count 语法
         *
         * 分页插件会自动检测并处理以下情况：
         * - 单表查询：直接添加分页
         * - 多表查询：需要手动处理或使用分页插件的 join 优化
         */
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        return interceptor;
    }
}
