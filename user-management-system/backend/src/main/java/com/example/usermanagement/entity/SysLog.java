package com.example.usermanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志表实体类
 */
@Data
@TableName("sys_log")
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 操作用户名
     */
    @TableField("username")
    private String username;

    /**
     * 操作类型
     */
    @TableField("operation_type")
    private String operationType;

    /**
     * 操作内容
     */
    @TableField("operation_content")
    private String operationContent;

    /**
     * 请求方法
     */
    @TableField("method")
    private String method;

    /**
     * 请求参数
     */
    @TableField("params")
    private String params;

    /**
     * IP地址
     */
    @TableField("ip")
    private String ip;

    /**
     * 操作时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 消耗时间(毫秒)
     */
    @TableField("time")
    private Long time;

    /**
     * 状态(0-成功,1-失败)
     */
    @TableField("status")
    private Integer status;

    /**
     * 错误消息
     */
    @TableField("error_msg")
    private String errorMsg;
}