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
 *
 * 对应数据库中的 sys_log 表，用于记录用户在系统中的所有操作。
 * 通过 AOP（Aspect-Oriented Programming）切面自动记录。
 *
 * 日志记录内容：
 * - 谁（userId, username）：操作用户的身份
 * - 何时（createTime）：操作发生的时间
 * - 何地（ip）：操作者的 IP 地址
 * - 做了什么（operationType, operationContent）：操作的类型和内容
 * - 结果如何（status, errorMsg, time）：操作结果和耗时
 *
 * 使用场景：
 * - 安全审计：追踪用户行为，排查安全问题
 * - 故障排查：当系统出现异常时，通过日志还原操作流程
 * - 性能监控：通过 time 字段分析接口响应时间
 * - 合规要求：满足等级保护等安全合规要求
 *
 * @see com.example.usermanagement.config.OperationLogAspect AOP切面
 */
@Data
@TableName("sys_log")
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID - 主键自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作用户ID - 关联 sys_user 表的 id
     * 记录是谁执行了这次操作
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 操作用户名 - 用户名的冗余存储
     * 便于直接查看日志时识别用户，无需关联查询
     */
    @TableField("username")
    private String username;

    /**
     * 操作类型 - 操作的分类标识
     * 格式：类名.方法名，如：UserController.create
     * 用于分类统计和分析操作趋势
     */
    @TableField("operation_type")
    private String operationType;

    /**
     * 操作内容 - 操作的详细描述
     * 例如："创建用户"、"删除角色"、"更新菜单"
     */
    @TableField("operation_content")
    private String operationContent;

    /**
     * 请求方法 - HTTP 请求方法
     * 例如：GET、POST、PUT、DELETE
     */
    @TableField("method")
    private String method;

    /**
     * 请求参数 - HTTP 请求的参数
     * 记录请求的 query 参数或 body 内容
     * 注意：敏感信息（如密码）应在记录前脱敏
     */
    @TableField("params")
    private String params;

    /**
     * IP地址 - 操作者的 IP 地址
     * 用于：
     * - 安全审计：追踪操作来源
     * - 异常排查：定位问题终端
     * - 统计报表：分析用户地域分布
     */
    @TableField("ip")
    private String ip;

    /**
     * 操作时间 - 记录操作发生的时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 消耗时间 - 操作执行耗时
     * 单位：毫秒（ms）
     * 用于性能监控和优化分析
     */
    @TableField("time")
    private Long time;

    /**
     * 状态 - 操作执行结果
     * - 0：成功
     * - 1：失败
     */
    @TableField("status")
    private Integer status;

    /**
     * 错误消息 - 失败时的错误详情
     * 当 status = 1 时，记录具体的异常信息
     */
    @TableField("error_msg")
    private String errorMsg;
}
