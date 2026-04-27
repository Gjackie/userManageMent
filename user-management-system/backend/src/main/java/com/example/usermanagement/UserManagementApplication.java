package com.example.usermanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 用户管理系统启动类
 *
 * Spring Boot 应用入口，负责启动整个应用程序。
 *
 * 主要注解说明：
 * - @SpringBootApplication：标注这是一个 Spring Boot 应用，包含以下组合：
 *   - @Configuration：标记此类为配置类
 *   - @EnableAutoConfiguration：启用自动配置
 *   - @ComponentScan：扫描组件（如 Controller、Service 等）
 *
 * - @MapperScan：指定 MyBatis Mapper 接口的扫描路径，
 *   MyBatis Plus 会自动为这些接口创建实现类
 *
 * - @EnableFeignClients：启用 Spring Cloud OpenFeign 功能，
 *   用于服务间调用（本项目目前未实际使用）
 *
 * - @EnableAspectJAutoProxy：启用 AspectJ 自动代理，
 *   用于 AOP 切面编程（如操作日志记录）
 */
@SpringBootApplication
@MapperScan("com.example.usermanagement.mapper")
@EnableFeignClients
@EnableAspectJAutoProxy
public class UserManagementApplication {

    /**
     * Spring Boot 应用启动入口
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(UserManagementApplication.class, args);
    }

}
