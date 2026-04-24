package com.example.usermanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usermanagement.common.PageResult;
import com.example.usermanagement.entity.SysLog;
import com.example.usermanagement.mapper.SysLogMapper;
import com.example.usermanagement.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public PageResult<SysLog> listLogs(Page<SysLog> page, String username, String operationType) {
        LambdaQueryWrapper<SysLog> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(username)) {
            wrapper.like(SysLog::getUsername, username);
        }
        if (StringUtils.hasText(operationType)) {
            wrapper.eq(SysLog::getOperationType, operationType);
        }
        wrapper.orderByDesc(SysLog::getCreateTime);
        Page<SysLog> result = sysLogMapper.selectPage(page, wrapper);
        return PageResult.of(result);
    }

    @Override
    public void saveLog(SysLog log) {
        sysLogMapper.insert(log);
    }
}
