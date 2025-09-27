-- 插入测试用户数据
INSERT IGNORE INTO `sys_user` (`id`, `username`, `nickname`, `email`, `phone`, `sex`, `avatar`, `password`, `status`, `del_flag`, `create_by`, `remark`) VALUES
(1, 'admin', '管理员', 'admin@xiaoxin.com', '15888888888', '0', '', '$2a$10$7JB720yubVSOfvVWbQJ5UeJQJ5UeJQJ5UeJQJ5UeJQJ5UeJQJ5UeJQ', '0', '0', 'admin', '管理员');

-- 插入测试角色数据
INSERT IGNORE INTO `sys_role` (`id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `status`, `create_by`, `remark`) VALUES
(1, '超级管理员', 'admin', 1, '1', '0', 'admin', '超级管理员'),
(2, '普通角色', 'common', 2, '2', '0', 'admin', '普通角色');

-- 插入用户角色关联
INSERT IGNORE INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1);

-- 插入测试权限数据
INSERT IGNORE INTO `sys_permission` (`id`, `permission_name`, `permission_code`, `permission_type`, `resource_type`, `resource_path`, `http_method`, `parent_id`, `level`, `sort_order`, `status`, `create_by`, `remark`) VALUES
(1, '系统管理', 'system', '1', 'MODULE', '', '', 0, 1, 1, '0', 'admin', '系统管理模块'),
(2, '用户管理', 'system:user', '1', 'MODULE', '', '', 1, 2, 1, '0', 'admin', '用户管理模块'),
(3, '用户查询', 'system:user:list', '1', 'API', '/system/user/list', 'GET', 2, 3, 1, '0', 'admin', '用户查询权限'),
(4, '用户新增', 'system:user:add', '1', 'API', '/system/user', 'POST', 2, 3, 2, '0', 'admin', '用户新增权限'),
(5, '用户修改', 'system:user:edit', '1', 'API', '/system/user', 'PUT', 2, 3, 3, '0', 'admin', '用户修改权限'),
(6, '用户删除', 'system:user:remove', '1', 'API', '/system/user', 'DELETE', 2, 3, 4, '0', 'admin', '用户删除权限');

-- 插入角色权限关联
INSERT IGNORE INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6);

-- 插入测试菜单数据
INSERT IGNORE INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `remark`) VALUES
(1, '系统管理', 0, 1, 'system', NULL, 'M', '0', '0', '', 'system', 'admin', '系统管理目录'),
(2, '用户管理', 1, 1, 'user', 'system/user/index', 'C', '0', '0', 'system:user:list', 'user', 'admin', '用户管理菜单'),
(3, '角色管理', 1, 2, 'role', 'system/role/index', 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '角色管理菜单'),
(4, '菜单管理', 1, 3, 'menu', 'system/menu/index', 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '菜单管理菜单');

-- 插入角色菜单关联
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4);

-- 插入测试部门数据
INSERT IGNORE INTO `sys_dept` (`id`, `parent_id`, `ancestors`, `dept_name`, `order_num`, `leader`, `phone`, `email`, `status`, `create_by`) VALUES
(100, 0, '0', '小新科技', 0, '小新', '15888888888', 'admin@xiaoxin.com', '0', 'admin'),
(101, 100, '0,100', '深圳总公司', 1, '小新', '15888888888', 'sz@xiaoxin.com', '0', 'admin'),
(102, 100, '0,100', '长沙分公司', 2, '小新', '15888888888', 'cs@xiaoxin.com', '0', 'admin');

-- 插入用户部门关联
INSERT IGNORE INTO `sys_user_dept` (`user_id`, `dept_id`, `is_primary`) VALUES
(1, 100, '1');
