-- ===========================================
-- iam_system 数据库脚本（配置和通知）
-- ===========================================
-- 使用iam_system数据库
USE `iam_system`;

-- 参数配置表
CREATE TABLE IF NOT EXISTS `sys_config` (
    `id` bigint NOT NULL COMMENT '参数主键',
    `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
    `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
    `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
    `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='参数配置表';

-- 字典类型表
CREATE TABLE IF NOT EXISTS `sys_dict_type` (
    `id` bigint NOT NULL COMMENT '字典主键',
    `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
    `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
    `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典类型表';

-- 字典数据表
CREATE TABLE IF NOT EXISTS `sys_dict_data` (
    `id` bigint NOT NULL COMMENT '字典编码',
    `dict_sort` int DEFAULT 0 COMMENT '字典排序',
    `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
    `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
    `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
    `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
    `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
    `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
    `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典数据表';

-- 通知公告表
CREATE TABLE IF NOT EXISTS `sys_notice` (
    `id` bigint NOT NULL COMMENT '公告ID',
    `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
    `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
    `notice_content` longblob COMMENT '公告内容',
    `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知公告表';

-- 消息通知表
CREATE TABLE IF NOT EXISTS `sys_message` (
    `id` bigint NOT NULL COMMENT '消息ID',
    `title` varchar(100) NOT NULL COMMENT '消息标题',
    `content` text COMMENT '消息内容',
    `type` varchar(20) NOT NULL COMMENT '消息类型（email,sms,wechat,internal）',
    `receiver` varchar(100) NOT NULL COMMENT '接收者',
    `status` char(1) DEFAULT '0' COMMENT '发送状态（0待发送 1已发送 2发送失败）',
    `send_time` datetime DEFAULT NULL COMMENT '发送时间',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `idx_receiver` (`receiver`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息通知表';

-- 插入初始数据
-- 插入默认配置
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `remark`) VALUES
(1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),
(2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '初始化密码 123456'),
(3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '深色主题theme-dark，浅色主题theme-light'),
(4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '是否开启验证码功能（true开启，false关闭）'),
(5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '是否开启注册用户功能（true开启，false关闭）'),
(6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');

-- 插入默认字典类型
INSERT INTO `sys_dict_type` (`id`, `dict_name`, `dict_type`, `status`, `create_by`, `remark`) VALUES
(1, '用户性别', 'sys_user_sex', '0', 'admin', '用户性别列表'),
(2, '菜单状态', 'sys_show_hide', '0', 'admin', '菜单状态列表'),
(3, '系统开关', 'sys_normal_disable', '0', 'admin', '系统开关列表'),
(4, '任务状态', 'sys_job_status', '0', 'admin', '任务状态列表'),
(5, '任务分组', 'sys_job_group', '0', 'admin', '任务分组列表'),
(6, '系统是否', 'sys_yes_no', '0', 'admin', '系统是否列表'),
(7, '通知类型', 'sys_notice_type', '0', 'admin', '通知类型列表'),
(8, '通知状态', 'sys_notice_status', '0', 'admin', '通知状态列表'),
(9, '操作类型', 'sys_oper_type', '0', 'admin', '操作类型列表'),
(10, '系统状态', 'sys_common_status', '0', 'admin', '登录状态列表');

-- 插入默认字典数据
INSERT INTO `sys_dict_data` (`id`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `remark`) VALUES
(1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '性别男'),
(2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '性别女'),
(3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '性别未知'),
(4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '显示菜单'),
(5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '隐藏菜单'),
(6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '正常状态'),
(7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '停用状态'),
(8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '正常状态'),
(9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '停用状态'),
(10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '默认分组'),
(11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '系统分组'),
(12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '系统默认是'),
(13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '系统默认否'),
(14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '通知'),
(15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '公告'),
(16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '正常状态'),
(17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '关闭状态'),
(18, 1, '新增', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '新增操作'),
(19, 2, '修改', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '修改操作'),
(20, 3, '删除', '2', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '删除操作'),
(21, 4, '授权', '3', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '授权操作'),
(22, 5, '导出', '4', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '导出操作'),
(23, 6, '导入', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '导入操作'),
(24, 7, '强退', '6', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '强退操作'),
(25, 8, '生成代码', '7', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '生成操作'),
(26, 9, '清空数据', '8', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '清空操作'),
(27, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '正常状态'),
(28, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '停用状态');
