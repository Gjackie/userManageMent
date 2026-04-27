package com.example.usermanagement.config;

import com.example.usermanagement.entity.SysLog;
import com.example.usermanagement.security.SecurityUser;
import com.example.usermanagement.service.LogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 操作日志切面类
 *
 * 作用：
 * 通过 AOP（Aspect-Oriented Programming）自动记录 Controller 中所有操作的日志，
 * 无需在每个 Controller 方法中手动编写日志记录代码。
 *
 * AOP 术语说明：
 * - Aspect（切面）：一个关注点的模块化，如日志记录
 * - Join Point（连接点）：程序执行的某个点，如方法调用
 * - Pointcut（切入点）：一组连接点的集合，如所有 Controller 方法
 * - Advice（通知/增强）：切入点的行为，如记录日志
 *
 * 本类使用 @Around 通知：
 * - 在目标方法执行前和执行后都能执行代码
 * - 可以控制是否继续执行目标方法
 * - 可以获取目标方法的返回值和异常信息
 *
 * 切入点表达式：
 * execution(* com.example.usermanagement.controller.*.*(..))
 * - execution：执行时触发
 * - *：返回任意类型
 * - com.example.usermanagement.controller.*：controller包下的任意类
 * - .*：任意方法
 * - (..)：任意参数
 *
 * @see SysLog 操作日志实体
 * @see LogService 日志服务
 */
@Aspect
@Component
public class OperationLogAspect {

    /**
     * 日志服务
     * 用于保存操作日志到数据库
     */
    @Autowired
    private LogService logService;

    /**
     * 记录操作日志
     *
     * 使用 @Around 环绕通知，在目标方法执行前后记录日志。
     *
     * @param joinPoint 切入点对象，可以获取目标方法的信息
     * @return 目标方法的返回值
     * @throws Throwable 如果目标方法执行抛出异常
     */
    @Around("execution(* com.example.usermanagement.controller.*.*(..))")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 获取 HTTP 请求对象（用于获取 IP、请求方法等）
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        // 创建日志对象
        SysLog log = new SysLog();
        log.setCreateTime(LocalDateTime.now());

        // 从请求中提取信息
        if (request != null) {
            log.setMethod(request.getMethod());                    // HTTP 方法（GET/POST/PUT/DELETE）
            log.setIp(request.getRemoteAddr());                    // 客户端 IP 地址
            log.setParams(getParamsFromRequest(request));           // 请求参数
        }

        // 从安全上下文中获取当前登录用户信息
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof SecurityUser) {
            SecurityUser user = (SecurityUser) auth.getPrincipal();
            log.setUserId(user.getId());
            log.setUsername(user.getUsername());
        }

        // 获取目标方法信息
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        log.setOperationType(className + "." + methodName);

        Object result = null;
        try {
            // 执行目标方法
            result = joinPoint.proceed();

            // 方法执行成功
            log.setStatus(0);
            log.setOperationContent("执行成功");

            return result;

        } catch (Throwable e) {
            // 方法执行失败，记录错误信息
            log.setStatus(1);
            log.setOperationContent("执行失败: " + e.getMessage());
            log.setErrorMsg(e.getMessage());

            // 重新抛出异常，让上层处理
            throw e;

        } finally {
            // 最终执行：记录消耗时间并保存日志
            long endTime = System.currentTimeMillis();
            log.setTime(endTime - startTime);
            logService.saveLog(log);
        }
    }

    /**
     * 从请求中提取参数
     *
     * 将请求参数格式化为字符串，便于日志记录。
     *
     * @param request HTTP 请求对象
     * @return 参数字符串
     */
    private String getParamsFromRequest(HttpServletRequest request) {
        // 实际项目中可以在这里处理参数序列化
        // 注意：敏感信息（如密码）应该在此处脱敏
        return request.getQueryString();
    }
}
