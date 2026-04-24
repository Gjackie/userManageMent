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

@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private LogService logService;

    @Around("execution(* com.example.usermanagement.controller.*.*(..))")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        SysLog log = new SysLog();
        log.setCreateTime(LocalDateTime.now());
        if (request != null) {
            log.setMethod(request.getMethod());
            log.setIp(request.getRemoteAddr());
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof SecurityUser) {
            SecurityUser user = (SecurityUser) auth.getPrincipal();
            log.setUserId(user.getId());
            log.setUsername(user.getUsername());
        }

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        log.setOperationType(className + "." + methodName);

        Object result = null;
        try {
            result = joinPoint.proceed();
            log.setStatus(0);
            log.setOperationContent("执行成功");
        } catch (Throwable e) {
            log.setStatus(1);
            log.setOperationContent("执行失败: " + e.getMessage());
            log.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            log.setTime(endTime - startTime);
            logService.saveLog(log);
        }
        return result;
    }
}
