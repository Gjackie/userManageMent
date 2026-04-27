package com.example.usermanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.usermanagement.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色表 Mapper 接口
 *
 * 继承 MyBatis Plus 的 BaseMapper，
 * 获得基本的 CRUD 操作能力。
 *
 * 内置方法：
 * - selectById(id)：根据 ID 查询
 * - selectOne(wrapper)：条件查询
 * - selectList(wrapper)：列表查询
 * - insert(entity)：插入
 * - updateById(entity)：更新
 * - deleteById(id)：删除
 *
 * @param <T> 实体类型，本接口固定为 SysRole
 * @see SysRole 角色实体
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
}
