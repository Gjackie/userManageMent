package com.example.usermanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.usermanagement.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表 Mapper 接口
 *
 * 继承 MyBatis Plus 的 BaseMapper，获得基本的 CRUD 操作能力。
 * MyBatis Plus 会自动生成 SQL 语句，无需手动编写。
 *
 * 内置方法说明：
 * - selectById(id)：根据 ID 查询
 * - selectOne(wrapper)：条件查询
 * - selectPage(page, wrapper)：分页查询
 * - selectCount(wrapper)：条件统计
 * - insert(entity)：插入数据
 * - updateById(entity)：根据 ID 更新
 * - deleteById(id)：根据 ID 删除（物理删除）
 *
 * 逻辑删除配合：
 * - MyBatis Plus 的逻辑删除通过 wrapper 的 .eq(deleted, 0) 条件实现
 * - 插入、更新、查询时需要手动添加 deleted 条件
 *
 * 使用示例：
 * <pre>
 * // 条件查询
 * LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
 * wrapper.eq(SysUser::getUsername, "admin")
 *        .eq(SysUser::getDeleted, 0);
 * SysUser user = sysUserMapper.selectOne(wrapper);
 *
 * // 分页查询
 * Page<SysUser> page = new Page<>(1, 10);
 * IPage<SysUser> result = sysUserMapper.selectPage(page, wrapper);
 * </pre>
 *
 * @param <T> 实体类型，本接口固定为 SysUser
 * @see SysUser 用户实体
 * @see com.baomidou.mybatisplus.core.mapper.BaseMapper MyBatis Plus BaseMapper
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
