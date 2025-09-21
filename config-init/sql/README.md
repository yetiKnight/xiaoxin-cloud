# IAM平台数据库初始化指南

## 概述

本目录包含了IAM平台的数据库初始化脚本，已按数据库分离为独立的SQL文件，适用于各种MySQL数据库环境（如阿里云RDS、腾讯云CDB、自建MySQL等）。

## 数据库结构

IAM平台使用4个独立的数据库：

- **iam_core**: 用户、角色、权限、组织管理
- **iam_system**: 系统配置、字典、通知公告
- **iam_auth**: OAuth2认证相关
- **iam_audit**: 操作日志、登录日志

## 执行步骤

### 第一步：创建数据库

根据您的MySQL环境，选择以下方式之一创建数据库：

#### 方式一：通过数据库管理工具（推荐）
使用MySQL Workbench、Navicat、phpMyAdmin等工具：
1. 连接到您的MySQL服务器
2. 创建以下4个数据库：
   - `iam_core`
   - `iam_system` 
   - `iam_auth`
   - `iam_audit`

#### 方式二：通过命令行
```sql
CREATE DATABASE iam_core CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE iam_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE iam_auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE iam_audit CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 方式三：通过云服务商控制台
- **阿里云RDS**：在RDS控制台创建数据库
- **腾讯云CDB**：在CDB控制台创建数据库
- **华为云RDS**：在RDS控制台创建数据库
- **AWS RDS**：在RDS控制台创建数据库

**数据库创建参数：**
- 字符集：`utf8mb4`
- 排序规则：`utf8mb4_unicode_ci`

### 第二步：创建数据库账号

根据您的环境，创建具有适当权限的数据库账号：

#### 建议的账号策略
- **管理员账号**：用于数据库创建和初始化（高权限）
- **应用账号**：用于应用连接（普通权限）

#### 通过命令行创建账号
```sql
-- 创建管理员账号
CREATE USER 'iam_admin'@'%' IDENTIFIED BY 'your_admin_password';
GRANT ALL PRIVILEGES ON iam_core.* TO 'iam_admin'@'%';
GRANT ALL PRIVILEGES ON iam_system.* TO 'iam_admin'@'%';
GRANT ALL PRIVILEGES ON iam_auth.* TO 'iam_admin'@'%';
GRANT ALL PRIVILEGES ON iam_audit.* TO 'iam_admin'@'%';

-- 创建应用账号
CREATE USER 'iam_app'@'%' IDENTIFIED BY 'your_app_password';
GRANT SELECT, INSERT, UPDATE, DELETE ON iam_core.* TO 'iam_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON iam_system.* TO 'iam_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON iam_auth.* TO 'iam_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON iam_audit.* TO 'iam_app'@'%';

-- 刷新权限
FLUSH PRIVILEGES;
```

#### 通过云服务商控制台
- **阿里云RDS**：在RDS控制台创建数据库账号
- **腾讯云CDB**：在CDB控制台创建数据库账号
- **华为云RDS**：在RDS控制台创建数据库账号
- **AWS RDS**：在RDS控制台创建数据库账号

### 第三步：执行SQL脚本

按以下顺序执行SQL脚本：

#### 方式一：通过命令行执行
```bash
# 1. 执行 iam_core 数据库脚本
mysql -h <HOST> -P <PORT> -u <USERNAME> -p iam_core < 01-iam-core.sql

# 2. 执行 iam_system 数据库脚本
mysql -h <HOST> -P <PORT> -u <USERNAME> -p iam_system < 02-iam-system.sql

# 3. 执行 iam_auth 数据库脚本
mysql -h <HOST> -P <PORT> -u <USERNAME> -p iam_auth < 03-iam-auth.sql

# 4. 执行 iam_audit 数据库脚本
mysql -h <HOST> -P <PORT> -u <USERNAME> -p iam_audit < 04-iam-audit.sql

```

#### 方式二：通过数据库管理工具执行
1. 使用MySQL Workbench、Navicat、phpMyAdmin等工具
2. 连接到对应的数据库
3. 打开并执行对应的SQL脚本文件

#### 方式三：通过Docker执行（如果使用Docker部署MySQL）
```bash
# 将SQL文件复制到容器中执行
docker cp 01-iam-core.sql <container_name>:/tmp/
docker exec -i <container_name> mysql -u root -p iam_core < /tmp/01-iam-core.sql
```

## 脚本说明

### 01-iam-core.sql
**基础表结构：**
- 用户表 (`sys_user`)
- 角色表 (`sys_role`)
- 菜单表 (`sys_menu`)
- 部门表 (`sys_dept`)
- 岗位表 (`sys_post`)
- 各种关联表

**权限管理增强：**
- 权限表 (`sys_permission`) - 独立权限管理
- 角色权限关联表 (`sys_role_permission`)
- 用户部门关联表 (`sys_user_dept`) - 支持多部门
- 数据权限表 (`sys_data_permission`) - 细粒度数据权限
- 字段权限表 (`sys_field_permission`) - 字段级权限控制

**初始数据：** 管理员用户、默认角色、菜单、部门、岗位、基础权限

### 02-iam-system.sql
**基础表结构：**
- 参数配置表 (`sys_config`)
- 字典类型表 (`sys_dict_type`)
- 字典数据表 (`sys_dict_data`)
- 通知公告表 (`sys_notice`)
- 消息通知表 (`sys_message`)

**系统管理增强：**
- 通知模板表 (`sys_notification_template`)
- 定时任务表 (`sys_job`)
- 定时任务执行日志表 (`sys_job_log`)

**初始数据：** 系统配置、字典数据、通知模板

### 03-iam-auth.sql
**基础表结构：**
- OAuth2客户端表 (`oauth2_registered_client`)
- OAuth2授权表 (`oauth2_authorization`)

**第三方登录增强：**
- OAuth2授权同意表 (`oauth2_authorization_consent`)
- 第三方登录配置表 (`sys_third_party_config`)
- 用户第三方账号绑定表 (`sys_user_third_party`)

**初始数据：** 默认OAuth2客户端、第三方登录配置

### 04-iam-audit.sql
**基础表结构：**
- 操作日志表 (`sys_oper_log`)
- 登录日志表 (`sys_logininfor`)

**审计日志增强：**
- 审计日志表 (`sys_audit_log`) - 通用审计日志
- 权限变更日志表 (`sys_permission_change_log`)
- 数据访问日志表 (`sys_data_access_log`)


## 默认账号信息

### 管理员账号
- 用户名：`admin`
- 密码：`123456` (已加密存储)
- 邮箱：`admin@xiaoxin.com`
- 手机：`15888888888`

### OAuth2客户端
- 客户端ID：`iam-client`
- 客户端密钥：`iam-secret`
- 重定向URI：`http://localhost:3000/callback`

## 注意事项

1. **权限要求**：执行脚本的账号需要有对应数据库的CREATE、INSERT权限
2. **字符集**：所有数据库和表都使用utf8mb4字符集
3. **执行顺序**：建议按编号顺序执行脚本
4. **备份**：执行前建议备份现有数据
5. **网络**：确保应用服务器能够访问MySQL实例
6. **MySQL版本**：建议使用MySQL 5.7+或MySQL 8.0+

## 故障排除

### 常见问题

1. **权限不足**
   ```
   ERROR 1142 (42000): CREATE command denied to user
   ```
   解决：使用具有CREATE权限的账号

2. **数据库不存在**
   ```
   ERROR 1049 (42000): Unknown database 'iam_core'
   ```
   解决：先创建对应的数据库

3. **字符集问题**
   ```
   ERROR 1267 (HY000): Illegal mix of collations
   ```
   解决：确保数据库和表都使用utf8mb4字符集

### 验证安装

执行以下SQL验证安装是否成功：

```sql
-- 检查iam_core数据库
USE iam_core;
SHOW TABLES;
SELECT COUNT(*) FROM sys_user;
SELECT COUNT(*) FROM sys_permission;

-- 检查iam_system数据库  
USE iam_system;
SHOW TABLES;
SELECT COUNT(*) FROM sys_config;
SELECT COUNT(*) FROM sys_notification_template;

-- 检查iam_auth数据库
USE iam_auth;
SHOW TABLES;
SELECT COUNT(*) FROM oauth2_registered_client;
SELECT COUNT(*) FROM sys_third_party_config;

-- 检查iam_audit数据库
USE iam_audit;
SHOW TABLES;
SELECT COUNT(*) FROM sys_oper_log;
SELECT COUNT(*) FROM sys_audit_log;

-- 验证增强功能表数量
SELECT 
    'iam_core' as database_name,
    (SELECT COUNT(*) FROM iam_core.sys_permission) as permission_count,
    (SELECT COUNT(*) FROM iam_core.sys_data_permission) as data_permission_count
UNION ALL
SELECT 
    'iam_auth' as database_name,
    (SELECT COUNT(*) FROM iam_auth.sys_third_party_config) as third_party_config_count,
    0 as data_permission_count
UNION ALL
SELECT 
    'iam_system' as database_name,
    (SELECT COUNT(*) FROM iam_system.sys_notification_template) as notification_template_count,
    0 as data_permission_count;
```

## 联系支持

如有问题，请联系开发团队或查看项目文档。
