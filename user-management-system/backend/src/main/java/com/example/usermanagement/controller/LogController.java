package com.example.usermanagement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usermanagement.common.PageResult;
import com.example.usermanagement.common.Result;
import com.example.usermanagement.entity.SysLog;
import com.example.usermanagement.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 日志管理控制器
 *
 * 提供日志相关的 RESTful API 接口。
 *
 * 接口列表：
 * - GET /api/logs/list - 获取日志列表（分页）
 *
 * @see LogService 日志服务
 */
@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 获取日志列表（分页）
     *
     * @param pageNum 页码（默认1）
     * @param pageSize 每页大小（默认10）
     * @param username 用户名（可选，用于筛选）
     * @param operationType 操作类型（可选，用于筛选）
     */
    @GetMapping("/list")
    public Result<PageResult<SysLog>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String operationType) {
        Page<SysLog> page = new Page<>(pageNum, pageSize);
        PageResult<SysLog> result = logService.listLogs(page, username, operationType);
        return Result.success(result);
    }
}
