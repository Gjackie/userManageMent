package com.example.usermanagement.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装类
 *
 * 用于统一返回分页查询的结果，包含：
 * - total：总记录数
 * - records：当前页的数据列表
 *
 * 设计说明：
 * - 实现 Serializable 接口，支持分布式场景
 * - 使用泛型 T，支持任意类型的数据分页
 *
 * 与 MyBatis Plus 的 IPage 配合使用：
 * - IPage 是 MyBatis Plus 提供的分页对象
 * - PageResult.of(page) 自动从 IPage 中提取数据构建响应
 *
 * 使用示例：
 * <pre>
 * // 后端代码
 * public PageResult<User> listUsers(Page<User> page) {
 *     IPage<User> result = userMapper.selectPage(page, wrapper);
 *     return PageResult.of(result);
 * }
 *
 * // 前端接收到的响应格式
 * {
 *     "code": 200,
 *     "msg": "操作成功",
 *     "data": {
 *         "total": 100,        // 总记录数
 *         "records": [...]     // 当前页数据列表
 *     }
 * }
 * </pre>
 *
 * @param <T> records 列表中元素的类型
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     * 表示符合条件的总数据条数，不是当前页的数据条数
     * 用于前端计算总页数：totalPages = Math.ceil(total / pageSize)
     */
    private long total;

    /**
     * 当前页数据列表
     * 类型为 List<T>，可以存放任意类型的数据
     */
    private List<T> records;

    /**
     * 默认构造函数
     */
    public PageResult() {}

    /**
     * 构造函数
     *
     * @param records 当前页数据列表
     * @param total 总记录数
     */
    public PageResult(List<T> records, long total) {
        this.records = records;
        this.total = total;
    }

    /**
     * 从 MyBatis Plus 的 IPage 对象构建分页结果
     *
     * 这是最常用的创建方式，配合 MyBatis Plus 分页插件使用
     *
     * @param page MyBatis Plus 的分页对象
     * @param <T> 数据类型
     * @return PageResult 分页结果对象
     *
     * @see IPage
     */
    public static <T> PageResult<T> of(IPage<T> page) {
        return new PageResult<>(page.getRecords(), page.getTotal());
    }
}
