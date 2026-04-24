package com.example.usermanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usermanagement.common.PageResult;
import com.example.usermanagement.entity.SysLog;

public interface LogService {
    PageResult<SysLog> listLogs(Page<SysLog> page, String username, String operationType);
    void saveLog(SysLog log);
}
