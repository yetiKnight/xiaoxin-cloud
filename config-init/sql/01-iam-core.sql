-- ===========================================
-- iam_core 数据库脚本（用户、权限、组织）
-- ===========================================
-- 使用iam_core数据库
USE `iam_core`;

-- 用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` bigint NOT NULL COMMENT '用户ID',
    `username` varchar(30) NOT NULL COMMENT '用户账号',
    `nickname` varchar(30) NOT NULL COMMENT '用户昵称',
    `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
    `phone` varchar(11) DEFAULT '' COMMENT '手机号码',
    `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
    `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
    `password` varchar(100) DEFAULT '' COMMENT '密码',
    `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
    `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_phone` (`phone`),
    KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户和角色关联表';

-- 角色表
CREATE TABLE IF NOT EXISTS `sys_role` (
    `id` bigint NOT NULL COMMENT '角色ID',
    `role_name` varchar(30) NOT NULL COMMENT '角色名称',
    `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
    `role_sort` int NOT NULL COMMENT '显示顺序',
    `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    `menu_check_strictly` tinyint(1) DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
    `dept_check_strictly` tinyint(1) DEFAULT 1 COMMENT '部门树选择项是否关联显示',
    `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息表';

-- 菜单权限表
CREATE TABLE IF NOT EXISTS `sys_menu` (
    `id` bigint NOT NULL COMMENT '菜单ID',
    `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
    `parent_id` bigint DEFAULT 0 COMMENT '父菜单ID',
    `order_num` int DEFAULT 0 COMMENT '显示顺序',
    `path` varchar(200) DEFAULT '' COMMENT '路由地址',
    `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
    `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
    `is_frame` int DEFAULT 1 COMMENT '是否为外链（0是 1否）',
    `is_cache` int DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
    `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
    `visible` char(1) DEFAULT 0 COMMENT '菜单状态（0显示 1隐藏）',
    `status` char(1) DEFAULT 0 COMMENT '菜单状态（0正常 1停用）',
    `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
    `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` varchar(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

-- 角色菜单关联表
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `menu_id` bigint NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色和菜单关联表';

-- 部门表
CREATE TABLE IF NOT EXISTS `sys_dept` (
    `id` bigint NOT NULL COMMENT '部门id',
    `parent_id` bigint DEFAULT 0 COMMENT '父部门id',
    `ancestors` varchar(50) DEFAULT '' COMMENT '祖级列表',
    `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
    `order_num` int DEFAULT 0 COMMENT '显示顺序',
    `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
    `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
    `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
    `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- 岗位表
CREATE TABLE IF NOT EXISTS `sys_post` (
    `id` bigint NOT NULL COMMENT '岗位ID',
    `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
    `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
    `post_sort` int NOT NULL COMMENT '显示顺序',
    `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='岗位信息表';

-- 用户岗位关联表
CREATE TABLE IF NOT EXISTS `sys_user_post` (
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `post_id` bigint NOT NULL COMMENT '岗位ID',
    PRIMARY KEY (`user_id`, `post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户与岗位关联表';

-- 插入初始数据
-- 插入默认管理员用户
INSERT IGNORE INTO `sys_user` (`id`, `username`, `nickname`, `email`, `phone`, `sex`, `avatar`, `password`, `status`, `del_flag`, `create_by`, `remark`) VALUES
(1, 'admin', '管理员', 'admin@xiaoxin.com', '15888888888', '0', '', '$2a$10$7JB720yubVSOfvVWbQJ5UeJQJ5UeJQJ5UeJQJ5UeJQJ5UeJQJ5UeJQ', '0', '0', 'admin', '管理员');

-- 插入默认角色
INSERT IGNORE INTO `sys_role` (`id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `status`, `create_by`, `remark`) VALUES
(1, '超级管理员', 'admin', 1, '1', '0', 'admin', '超级管理员'),
(2, '普通角色', 'common', 2, '2', '0', 'admin', '普通角色');

-- 插入默认菜单
INSERT IGNORE INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `remark`) VALUES
(1, '系统管理', 0, 1, 'system', NULL, 'M', '0', '0', '', 'system', 'admin', '系统管理目录'),
(2, '用户管理', 1, 1, 'user', 'system/user/index', 'C', '0', '0', 'system:user:list', 'user', 'admin', '用户管理菜单'),
(3, '角色管理', 1, 2, 'role', 'system/role/index', 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '角色管理菜单'),
(4, '菜单管理', 1, 3, 'menu', 'system/menu/index', 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '菜单管理菜单'),
(5, '部门管理', 1, 4, 'dept', 'system/dept/index', 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '部门管理菜单'),
(6, '岗位管理', 1, 5, 'post', 'system/post/index', 'C', '0', '0', 'system:post:list', 'post', 'admin', '岗位管理菜单'),
(7, '字典管理', 1, 6, 'dict', 'system/dict/index', 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '字典管理菜单'),
(8, '参数设置', 1, 7, 'config', 'system/config/index', 'C', '0', '0', 'system:config:list', 'edit', 'admin', '参数设置菜单'),
(9, '通知公告', 1, 8, 'notice', 'system/notice/index', 'C', '0', '0', 'system:notice:list', 'message', 'admin', '通知公告菜单'),
(10, '日志管理', 1, 9, 'log', '', 'M', '0', '0', '', 'log', 'admin', '日志管理目录'),
(11, '操作日志', 10, 1, 'operlog', 'monitor/operlog/index', 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '操作日志菜单'),
(12, '登录日志', 10, 2, 'logininfor', 'monitor/logininfor/index', 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', '登录日志菜单');

-- 插入角色菜单关联
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11), (1, 12);

-- 插入默认部门
INSERT IGNORE INTO `sys_dept` (`id`, `parent_id`, `ancestors`, `dept_name`, `order_num`, `leader`, `phone`, `email`, `status`, `create_by`) VALUES
(100, 0, '0', '小新科技', 0, '小新', '15888888888', 'admin@xiaoxin.com', '0', 'admin'),
(101, 100, '0,100', '深圳总公司', 1, '小新', '15888888888', 'sz@xiaoxin.com', '0', 'admin'),
(102, 100, '0,100', '长沙分公司', 2, '小新', '15888888888', 'cs@xiaoxin.com', '0', 'admin'),
(103, 101, '0,100,101', '研发部门', 1, '小新', '15888888888', 'rd@xiaoxin.com', '0', 'admin'),
(104, 101, '0,100,101', '市场部门', 2, '小新', '15888888888', 'mk@xiaoxin.com', '0', 'admin'),
(105, 101, '0,100,101', '测试部门', 3, '小新', '15888888888', 'qa@xiaoxin.com', '0', 'admin'),
(106, 101, '0,100,101', '财务部门', 4, '小新', '15888888888', 'fi@xiaoxin.com', '0', 'admin'),
(107, 101, '0,100,101', '运维部门', 5, '小新', '15888888888', 'ops@xiaoxin.com', '0', 'admin'),
(108, 102, '0,100,102', '市场部门', 1, '小新', '15888888888', 'mk@xiaoxin.com', '0', 'admin'),
(109, 102, '0,100,102', '财务部门', 2, '小新', '15888888888', 'fi@xiaoxin.com', '0', 'admin');

-- 插入默认岗位
INSERT IGNORE INTO `sys_post` (`id`, `post_code`, `post_name`, `post_sort`, `status`, `create_by`, `remark`) VALUES
(1, 'ceo', '董事长', 1, '0', 'admin', ''),
(2, 'se', '项目经理', 2, '0', 'admin', ''),
(3, 'hr', '人力资源', 3, '0', 'admin', ''),
(4, 'user', '普通员工', 4, '0', 'admin', '');

-- ===========================================
-- 权限管理增强表结构
-- ===========================================

-- 权限表（独立的权限管理，区别于菜单权限）
CREATE TABLE IF NOT EXISTS `sys_permission` (
    `id` bigint NOT NULL COMMENT '权限ID',
    `permission_name` varchar(50) NOT NULL COMMENT '权限名称',
    `permission_code` varchar(100) NOT NULL COMMENT '权限编码',
    `permission_type` char(1) DEFAULT '1' COMMENT '权限类型（1功能权限 2数据权限 3字段权限）',
    `resource_type` varchar(20) DEFAULT 'API' COMMENT '资源类型（API接口 MENU菜单 BUTTON按钮 DATA数据）',
    `resource_path` varchar(200) DEFAULT '' COMMENT '资源路径',
    `http_method` varchar(10) DEFAULT '' COMMENT 'HTTP方法（GET POST PUT DELETE）',
    `parent_id` bigint DEFAULT 0 COMMENT '父权限ID',
    `level` int DEFAULT 1 COMMENT '权限层级',
    `sort_order` int DEFAULT 0 COMMENT '排序',
    `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_permission_code` (`permission_code`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_resource_type` (`resource_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS `sys_role_permission` (
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `permission_id` bigint NOT NULL COMMENT '权限ID',
    PRIMARY KEY (`role_id`, `permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色和权限关联表';

-- 用户部门关联表（支持用户多部门）
CREATE TABLE IF NOT EXISTS `sys_user_dept` (
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `dept_id` bigint NOT NULL COMMENT '部门ID',
    `is_primary` char(1) DEFAULT '0' COMMENT '是否主部门（0否 1是）',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`user_id`, `dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户和部门关联表';

-- 数据权限表（支持细粒度数据权限控制）
CREATE TABLE IF NOT EXISTS `sys_data_permission` (
    `id` bigint NOT NULL COMMENT '数据权限ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `resource_type` varchar(50) NOT NULL COMMENT '资源类型（表名或业务模块）',
    `permission_type` char(1) DEFAULT '1' COMMENT '权限类型（1全部数据 2本部门数据 3本部门及子部门 4仅本人数据 5自定义数据）',
    `dept_ids` varchar(1000) DEFAULT '' COMMENT '部门ID列表（逗号分隔）',
    `user_ids` varchar(1000) DEFAULT '' COMMENT '用户ID列表（逗号分隔）',
    `custom_condition` text COMMENT '自定义条件（SQL WHERE条件）',
    `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_resource_type` (`resource_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据权限表';

-- 字段权限表（支持字段级权限控制）
CREATE TABLE IF NOT EXISTS `sys_field_permission` (
    `id` bigint NOT NULL COMMENT '字段权限ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `resource_type` varchar(50) NOT NULL COMMENT '资源类型（表名）',
    `field_name` varchar(50) NOT NULL COMMENT '字段名称',
    `permission_type` char(1) DEFAULT '1' COMMENT '权限类型（1可见 2可编辑 3脱敏显示 4不可见）',
    `mask_type` varchar(20) DEFAULT '' COMMENT '脱敏类型（phone手机号 email邮箱 idcard身份证 custom自定义）',
    `mask_rule` varchar(100) DEFAULT '' COMMENT '脱敏规则',
    `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_role_resource` (`role_id`, `resource_type`),
    KEY `idx_field_name` (`field_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字段权限表';

-- ===========================================
-- 权限管理初始化数据
-- ===========================================

-- 插入基础权限
INSERT IGNORE INTO `sys_permission` (`id`, `permission_name`, `permission_code`, `permission_type`, `resource_type`, `resource_path`, `http_method`, `parent_id`, `level`, `sort_order`, `status`, `create_by`, `remark`) VALUES
(1, '系统管理', 'system', '1', 'MODULE', '', '', 0, 1, 1, '0', 'admin', '系统管理模块'),
(2, '用户管理', 'system:user', '1', 'MODULE', '', '', 1, 2, 1, '0', 'admin', '用户管理模块'),
(3, '用户查询', 'system:user:list', '1', 'API', '/system/user/list', 'GET', 2, 3, 1, '0', 'admin', '用户查询权限'),
(4, '用户新增', 'system:user:add', '1', 'API', '/system/user', 'POST', 2, 3, 2, '0', 'admin', '用户新增权限'),
(5, '用户修改', 'system:user:edit', '1', 'API', '/system/user', 'PUT', 2, 3, 3, '0', 'admin', '用户修改权限'),
(6, '用户删除', 'system:user:remove', '1', 'API', '/system/user', 'DELETE', 2, 3, 4, '0', 'admin', '用户删除权限'),
(7, '角色管理', 'system:role', '1', 'MODULE', '', '', 1, 2, 2, '0', 'admin', '角色管理模块'),
(8, '角色查询', 'system:role:list', '1', 'API', '/system/role/list', 'GET', 7, 3, 1, '0', 'admin', '角色查询权限'),
(9, '角色新增', 'system:role:add', '1', 'API', '/system/role', 'POST', 7, 3, 2, '0', 'admin', '角色新增权限'),
(10, '角色修改', 'system:role:edit', '1', 'API', '/system/role', 'PUT', 7, 3, 3, '0', 'admin', '角色修改权限'),
(11, '角色删除', 'system:role:remove', '1', 'API', '/system/role', 'DELETE', 7, 3, 4, '0', 'admin', '角色删除权限');

-- 为超级管理员角色分配权限
INSERT IGNORE INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11);

-- 插入默认数据权限（超级管理员全部数据权限）
INSERT IGNORE INTO `sys_data_permission` (`id`, `role_id`, `resource_type`, `permission_type`, `status`, `create_by`, `remark`) VALUES
(1, 1, 'sys_user', '1', '0', 'admin', '超级管理员-用户数据全部权限'),
(2, 1, 'sys_role', '1', '0', 'admin', '超级管理员-角色数据全部权限'),
(3, 1, 'sys_dept', '1', '0', 'admin', '超级管理员-部门数据全部权限');