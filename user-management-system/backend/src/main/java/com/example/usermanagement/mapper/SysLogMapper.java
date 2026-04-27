package com.example.usermanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.usermanagement.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志表 Mapper 接口
 *
 * 继承 MyBatis Plus 的 BaseMapper，
 * 获得基本的 CRUD 操作能力。
 *
 * @param <T> 实体类型，本接口固定为 SysLog
 * @see SysLog 操作日志实体
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {
}
