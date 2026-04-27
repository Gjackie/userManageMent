package com.example.usermanagement.config;

import com.example.usermanagement.common.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * 作用：
 * 统一捕获并处理应用中抛出的异常，转换为统一的 JSON 响应格式。
 *
 * 工作原理：
 * - @RestControllerAdvice：标注为 REST 控制器的切面类
 * - @ExceptionHandler：标注处理特定异常的方法
 * - 异常处理顺序：从上到下匹配，先匹配到的先执行
 *
 * 处理的异常类型：
 * 1. RuntimeException：运行时异常
 * 2. MethodArgumentNotValidException：参数校验异常
 * 3. AccessDeniedException：权限不足异常
 * 4. Exception：通用异常（兜底处理）
 *
 * 统一响应格式：
 * {
 *     "code": 状态码,
 *     "msg": "错误消息",
 *     "data": null
 * }
 *
 * @see Result 统一响应结果类
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理运行时异常
     *
     * RuntimeException 是最常见的异常类型，
     * 本项目中的业务异常（如"用户不存在"、"用户名已存在"）都是 RuntimeException。
     *
     * @param e 运行时异常
     * @return 错误响应
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        // 直接返回异常消息
        return Result.error(e.getMessage());
    }

    /**
     * 处理参数校验异常
     *
     * 当 Controller 方法参数使用了 @Valid 或 @Validated 注解，
     * 但参数不满足校验规则时，Spring 会抛出此异常。
     *
     * 常见校验注解：
     * - @NotBlank：不能为空字符串
     * - @NotNull：不能为 null
     * - @Size：字符串长度范围
     * - @Email：邮箱格式
     * - @Pattern：正则表达式匹配
     *
     * @param e 参数校验异常
     * @return 错误响应，包含字段名和错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        // 提取所有字段校验错误
        // 格式：fieldName: errorMessage; fieldName2: errorMessage2
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("参数校验失败");
        return Result.error(400, msg);
    }

    /**
     * 处理权限不足异常
     *
     * 当用户已登录，但尝试访问没有权限的资源时抛出。
     * 例如：普通用户尝试访问管理员专属接口。
     *
     * @param e 权限异常
     * @return 403 Forbidden 响应
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e) {
        return Result.error(403, "没有权限访问");
    }

    /**
     * 处理通用异常（兜底）
     *
     * 当以上异常都匹配不到时，由本方法处理。
     * 这是最后一道防线，防止异常信息泄露给用户。
     *
     * @param e 任意异常
     * @return 500 服务器错误响应
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        // 生产环境应记录详细日志，只返回简短消息
        return Result.error("系统异常: " + e.getMessage());
    }
}
