-- ===========================================
-- iam_auth 数据库脚本（OAuth2认证）
-- ===========================================
-- 使用iam_auth数据库
USE `iam_auth`;

-- OAuth2客户端表
CREATE TABLE IF NOT EXISTS `oauth2_registered_client` (
    `id` varchar(100) NOT NULL COMMENT '客户端ID',
    `client_id` varchar(100) NOT NULL COMMENT '客户端标识',
    `client_id_issued_at` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '客户端ID发布时间',
    `client_secret` varchar(200) DEFAULT NULL COMMENT '客户端密钥',
    `client_secret_expires_at` datetime DEFAULT NULL COMMENT '客户端密钥过期时间',
    `client_name` varchar(200) NOT NULL COMMENT '客户端名称',
    `client_authentication_methods` varchar(1000) DEFAULT NULL COMMENT '客户端认证方法',
    `authorization_grant_types` varchar(1000) DEFAULT NULL COMMENT '授权类型',
    `redirect_uris` varchar(1000) DEFAULT NULL COMMENT '重定向URI',
    `scopes` varchar(1000) DEFAULT NULL COMMENT '作用域',
    `client_settings` varchar(2000) DEFAULT NULL COMMENT '客户端设置',
    `token_settings` varchar(2000) DEFAULT NULL COMMENT '令牌设置',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_client_id` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OAuth2客户端表';

-- OAuth2授权表
CREATE TABLE IF NOT EXISTS `oauth2_authorization` (
    `id` varchar(100) NOT NULL COMMENT '授权ID',
    `registered_client_id` varchar(100) NOT NULL COMMENT '注册客户端ID',
    `principal_name` varchar(200) NOT NULL COMMENT '主体名称',
    `authorization_grant_type` varchar(100) NOT NULL COMMENT '授权类型',
    `authorized_scopes` varchar(1000) DEFAULT NULL COMMENT '授权作用域',
    `attributes` text COMMENT '属性',
    `state` varchar(500) DEFAULT NULL COMMENT '状态',
    `authorization_code_value` text COMMENT '授权码值',
    `authorization_code_issued_at` datetime DEFAULT NULL COMMENT '授权码发布时间',
    `authorization_code_expires_at` datetime DEFAULT NULL COMMENT '授权码过期时间',
    `authorization_code_metadata` text COMMENT '授权码元数据',
    `access_token_value` text COMMENT '访问令牌值',
    `access_token_issued_at` datetime DEFAULT NULL COMMENT '访问令牌发布时间',
    `access_token_expires_at` datetime DEFAULT NULL COMMENT '访问令牌过期时间',
    `access_token_metadata` text COMMENT '访问令牌元数据',
    `access_token_type` varchar(100) DEFAULT NULL COMMENT '访问令牌类型',
    `access_token_scopes` varchar(1000) DEFAULT NULL COMMENT '访问令牌作用域',
    `oidc_id_token_value` text COMMENT 'OIDC ID令牌值',
    `oidc_id_token_issued_at` datetime DEFAULT NULL COMMENT 'OIDC ID令牌发布时间',
    `oidc_id_token_expires_at` datetime DEFAULT NULL COMMENT 'OIDC ID令牌过期时间',
    `oidc_id_token_metadata` text COMMENT 'OIDC ID令牌元数据',
    `refresh_token_value` text COMMENT '刷新令牌值',
    `refresh_token_issued_at` datetime DEFAULT NULL COMMENT '刷新令牌发布时间',
    `refresh_token_expires_at` datetime DEFAULT NULL COMMENT '刷新令牌过期时间',
    `refresh_token_metadata` text COMMENT '刷新令牌元数据',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OAuth2授权表';

-- 插入默认OAuth2客户端
INSERT IGNORE INTO `oauth2_registered_client` (`id`, `client_id`, `client_secret`, `client_name`, `client_authentication_methods`, `authorization_grant_types`, `redirect_uris`, `scopes`) VALUES
('iam-client', 'iam-client', '{noop}iam-secret', 'IAM平台客户端', 'client_secret_basic,client_secret_post', 'authorization_code,refresh_token,client_credentials', 'http://localhost:3000/callback', 'read,write');

-- 插入服务间调用客户端
INSERT IGNORE INTO `oauth2_registered_client` (`id`, `client_id`, `client_secret`, `client_name`, `client_authentication_methods`, `authorization_grant_types`, `redirect_uris`, `scopes`, `client_settings`, `token_settings`) VALUES
('iam-auth-service', 'iam-auth-service', '{bcrypt}$2a$10$BQMpX5VZ9oZH.1mZ1B9D9.Z2oZH1mZ1B9D9Z2oZH1mZ1B9D9Z2oZ', '认证服务客户端', 'client_secret_basic,client_secret_post', 'client_credentials', '', 'internal:read,internal:write,auth:read,auth:write', '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}', '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.refresh-token-time-to-live":["java.time.Duration",7200.000000000],"settings.token.reuse-refresh-tokens":true}'),
('iam-core-service', 'iam-core-service', '{bcrypt}$2a$10$YQJmX4VY7nXE.1kX8Y7A7.X9nXE1kX8Y7A7X9nXE1kX8Y7A7X9nX', '核心服务客户端', 'client_secret_basic,client_secret_post', 'client_credentials', '', 'internal:read,internal:write,user:read,user:write', '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}', '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.refresh-token-time-to-live":["java.time.Duration",7200.000000000],"settings.token.reuse-refresh-tokens":true}'),
('iam-audit-service', 'iam-audit-service', '{bcrypt}$2a$10$ZRKnY5WZ8oYF.1lY9Z8B8.Y0oYF1lY9Z8B8Y0oYF1lY9Z8B8Y0oY', '审计服务客户端', 'client_secret_basic,client_secret_post', 'client_credentials', '', 'internal:read,internal:write,audit:read,audit:write', '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}', '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.refresh-token-time-to-live":["java.time.Duration",7200.000000000],"settings.token.reuse-refresh-tokens":true}'),
('iam-system-service', 'iam-system-service', '{bcrypt}$2a$10$ARLoZ6XA9pZG.1mZ0A9C9.Z1pZG1mZ0A9C9Z1pZG1mZ0A9C9Z1pZ', '系统服务客户端', 'client_secret_basic,client_secret_post', 'client_credentials', '', 'internal:read,internal:write,system:read,system:write', '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}', '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.refresh-token-time-to-live":["java.time.Duration",7200.000000000],"settings.token.reuse-refresh-tokens":true}');

-- ===========================================
-- 第三方登录增强表结构
-- ===========================================

-- OAuth2授权同意表（用户授权记录）
CREATE TABLE IF NOT EXISTS `oauth2_authorization_consent` (
    `registered_client_id` varchar(100) NOT NULL COMMENT '注册客户端ID',
    `principal_name` varchar(200) NOT NULL COMMENT '主体名称',
    `authorities` varchar(1000) NOT NULL COMMENT '授权权限',
    PRIMARY KEY (`registered_client_id`, `principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OAuth2授权同意表';

-- 第三方登录配置表
CREATE TABLE IF NOT EXISTS `sys_third_party_config` (
    `id` bigint NOT NULL COMMENT '配置ID',
    `provider` varchar(50) NOT NULL COMMENT '第三方提供商（wechat微信 dingtalk钉钉 github等）',
    `app_id` varchar(100) NOT NULL COMMENT '应用ID',
    `app_secret` varchar(200) NOT NULL COMMENT '应用密钥',
    `redirect_uri` varchar(200) DEFAULT '' COMMENT '回调地址',
    `scope` varchar(200) DEFAULT '' COMMENT '授权范围',
    `enabled` char(1) DEFAULT '1' COMMENT '是否启用（0停用 1启用）',
    `sort_order` int DEFAULT 0 COMMENT '排序',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_provider` (`provider`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='第三方登录配置表';

-- 用户第三方账号绑定表
CREATE TABLE IF NOT EXISTS `sys_user_third_party` (
    `id` bigint NOT NULL COMMENT '绑定ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `provider` varchar(50) NOT NULL COMMENT '第三方提供商',
    `third_party_id` varchar(100) NOT NULL COMMENT '第三方用户ID',
    `third_party_username` varchar(100) DEFAULT '' COMMENT '第三方用户名',
    `third_party_avatar` varchar(200) DEFAULT '' COMMENT '第三方头像',
    `access_token` text COMMENT '访问令牌',
    `refresh_token` text COMMENT '刷新令牌',
    `expires_at` datetime DEFAULT NULL COMMENT '令牌过期时间',
    `bind_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
    `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
    `status` char(1) DEFAULT '1' COMMENT '绑定状态（0解绑 1已绑定）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_provider` (`user_id`, `provider`),
    UNIQUE KEY `uk_provider_third_id` (`provider`, `third_party_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户第三方账号绑定表';

-- ===========================================
-- 第三方登录初始化数据
-- ===========================================

-- 插入第三方登录配置示例
INSERT IGNORE INTO `sys_third_party_config` (`id`, `provider`, `app_id`, `app_secret`, `redirect_uri`, `scope`, `enabled`, `sort_order`, `create_by`, `remark`) VALUES
(1, 'wechat', 'your_wechat_app_id', 'your_wechat_app_secret', 'http://localhost:8080/auth/callback/wechat', 'snsapi_userinfo', '0', 1, 'admin', '微信登录配置'),
(2, 'dingtalk', 'your_dingtalk_app_id', 'your_dingtalk_app_secret', 'http://localhost:8080/auth/callback/dingtalk', 'openid', '0', 2, 'admin', '钉钉登录配置'),
(3, 'github', 'your_github_client_id', 'your_github_client_secret', 'http://localhost:8080/auth/callback/github', 'user:email', '0', 3, 'admin', 'GitHub登录配置');