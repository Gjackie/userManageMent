package com.example.usermanagement.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果类
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private long total; // 总记录数
    private List<T> records; // 当前页数据

    public PageResult() {}

    public PageResult(List<T> records, long total) {
        this.records = records;
        this.total = total;
    }

    public static <T> PageResult<T> of(IPage<T> page) {
        return new PageResult<>(page.getRecords(), page.getTotal());
    }
}