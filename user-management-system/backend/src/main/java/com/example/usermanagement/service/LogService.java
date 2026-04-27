package com.example.usermanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usermanagement.common.PageResult;
import com.example.usermanagement.entity.SysLog;

/**
 * 日志服务接口
 *
 * 定义操作日志的业务逻辑方法。
 *
 * @see com.example.usermanagement.service.impl.LogServiceImpl
 */
public interface LogService {

    /**
     * 分页查询日志列表
     *
     * @param page 分页对象
     * @param username 用户名（可选，用于筛选）
     * @param operationType 操作类型（可选，用于筛选）
     * @return 分页结果
     */
    PageResult<SysLog> listLogs(Page<SysLog> page, String username, String operationType);

    /**
     * 保存日志
     *
     * @param log 日志对象
     */
    void saveLog(SysLog log);
}
