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
INSERT INTO `oauth2_registered_client` (`id`, `client_id`, `client_secret`, `client_name`, `client_authentication_methods`, `authorization_grant_types`, `redirect_uris`, `scopes`) VALUES
('iam-client', 'iam-client', '{noop}iam-secret', 'IAM平台客户端', 'client_secret_basic,client_secret_post', 'authorization_code,refresh_token,client_credentials', 'http://localhost:3000/callback', 'read,write');
