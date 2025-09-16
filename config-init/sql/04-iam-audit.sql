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
