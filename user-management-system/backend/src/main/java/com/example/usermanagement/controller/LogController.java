package com.example.usermanagement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usermanagement.common.PageResult;
import com.example.usermanagement.common.Result;
import com.example.usermanagement.entity.SysLog;
import com.example.usermanagement.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    private LogService logService;

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
