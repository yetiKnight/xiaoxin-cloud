-- ===========================================
-- iam_audit 数据库脚本（审计日志）
-- ===========================================
-- 使用iam_audit数据库
USE `iam_audit`;

-- 操作日志表
CREATE TABLE IF NOT EXISTS `sys_oper_log` (
    `id` bigint NOT NULL COMMENT '日志主键',
    `title` varchar(50) DEFAULT '' COMMENT '模块标题',
    `business_type` int DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
    `method` varchar(100) DEFAULT '' COMMENT '方法名称',
    `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
    `operator_type` int DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
    `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
    `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
    `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
    `oper_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
    `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
    `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
    `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
    `status` int DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
    `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
    `oper_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `cost_time` bigint DEFAULT 0 COMMENT '消耗时间',
    PRIMARY KEY (`id`),
    KEY `idx_oper_time` (`oper_time`),
    KEY `idx_oper_name` (`oper_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志记录';

-- 登录日志表
CREATE TABLE IF NOT EXISTS `sys_logininfor` (
    `id` bigint NOT NULL COMMENT '访问ID',
    `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
    `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
    `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
    `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
    `os` varchar(50) DEFAULT '' COMMENT '操作系统',
    `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
    `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
    `login_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
    PRIMARY KEY (`id`),
    KEY `idx_login_time` (`login_time`),
    KEY `idx_user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统访问记录';

-- ===========================================
-- 审计日志增强表结构
-- ===========================================

-- 审计日志表（通用审计日志，区别于操作日志）
CREATE TABLE IF NOT EXISTS `sys_audit_log` (
    `id` bigint NOT NULL COMMENT '审计日志ID',
    `trace_id` varchar(64) DEFAULT '' COMMENT '链路追踪ID',
    `user_id` bigint DEFAULT NULL COMMENT '用户ID',
    `username` varchar(50) DEFAULT '' COMMENT '用户名',
    `real_name` varchar(50) DEFAULT '' COMMENT '真实姓名',
    `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
    `resource_type` varchar(50) DEFAULT '' COMMENT '资源类型',
    `resource_id` varchar(100) DEFAULT '' COMMENT '资源ID',
    `action` varchar(50) DEFAULT '' COMMENT '操作动作',
    `action_desc` varchar(200) DEFAULT '' COMMENT '操作描述',
    `old_value` text COMMENT '变更前数据',
    `new_value` text COMMENT '变更后数据',
    `client_ip` varchar(128) DEFAULT '' COMMENT '客户端IP',
    `user_agent` varchar(500) DEFAULT '' COMMENT '用户代理',
    `request_uri` varchar(255) DEFAULT '' COMMENT '请求URI',
    `request_method` varchar(10) DEFAULT '' COMMENT '请求方法',
    `request_params` text COMMENT '请求参数',
    `response_code` varchar(10) DEFAULT '' COMMENT '响应状态码',
    `response_msg` varchar(500) DEFAULT '' COMMENT '响应消息',
    `cost_time` bigint DEFAULT 0 COMMENT '耗时（毫秒）',
    `risk_level` char(1) DEFAULT '1' COMMENT '风险等级（1低 2中 3高 4严重）',
    `event_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '事件时间',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_username` (`username`),
    KEY `idx_resource` (`resource_type`, `resource_id`),
    KEY `idx_action` (`action`),
    KEY `idx_event_time` (`event_time`),
    KEY `idx_trace_id` (`trace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审计日志表';

-- 权限变更日志表
CREATE TABLE IF NOT EXISTS `sys_permission_change_log` (
    `id` bigint NOT NULL COMMENT '日志ID',
    `change_type` varchar(20) NOT NULL COMMENT '变更类型（GRANT授权 REVOKE撤销 MODIFY修改）',
    `target_type` varchar(20) NOT NULL COMMENT '目标类型（USER用户 ROLE角色）',
    `target_id` bigint NOT NULL COMMENT '目标ID',
    `target_name` varchar(100) DEFAULT '' COMMENT '目标名称',
    `permission_type` varchar(20) DEFAULT '' COMMENT '权限类型（MENU菜单 API接口 DATA数据 FIELD字段）',
    `permission_id` bigint DEFAULT NULL COMMENT '权限ID',
    `permission_name` varchar(100) DEFAULT '' COMMENT '权限名称',
    `old_value` text COMMENT '变更前值',
    `new_value` text COMMENT '变更后值',
    `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
    `operator_name` varchar(50) DEFAULT '' COMMENT '操作人姓名',
    `client_ip` varchar(128) DEFAULT '' COMMENT '操作IP',
    `reason` varchar(500) DEFAULT '' COMMENT '变更原因',
    `approval_id` varchar(100) DEFAULT '' COMMENT '审批单号',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_target` (`target_type`, `target_id`),
    KEY `idx_permission` (`permission_type`, `permission_id`),
    KEY `idx_operator` (`operator_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限变更日志表';

-- 数据访问日志表
CREATE TABLE IF NOT EXISTS `sys_data_access_log` (
    `id` bigint NOT NULL COMMENT '访问日志ID',
    `user_id` bigint DEFAULT NULL COMMENT '用户ID',
    `username` varchar(50) DEFAULT '' COMMENT '用户名',
    `resource_type` varchar(50) DEFAULT '' COMMENT '资源类型',
    `resource_id` varchar(100) DEFAULT '' COMMENT '资源ID',
    `access_type` varchar(20) DEFAULT '' COMMENT '访问类型（SELECT查询 INSERT新增 UPDATE修改 DELETE删除）',
    `sql_statement` text COMMENT 'SQL语句',
    `affected_rows` int DEFAULT 0 COMMENT '影响行数',
    `sensitive_fields` varchar(500) DEFAULT '' COMMENT '敏感字段列表',
    `client_ip` varchar(128) DEFAULT '' COMMENT '客户端IP',
    `access_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
    `cost_time` bigint DEFAULT 0 COMMENT '耗时（毫秒）',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_resource` (`resource_type`, `resource_id`),
    KEY `idx_access_type` (`access_type`),
    KEY `idx_access_time` (`access_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据访问日志表';