# IAM认证服务

## 概述

IAM认证服务是基于Spring Boot和Spring Security构建的用户认证服务，提供用户登录、登出、令牌刷新等核心认证功能。

## 功能特性

- ✅ 用户名/密码登录
- ✅ JWT令牌生成和验证
- ✅ 密码加密存储
- ✅ 用户状态验证
- ✅ 令牌刷新机制
- ✅ 登录日志记录
- ✅ 接口参数验证
- ✅ 统一异常处理

## 技术栈

- **Spring Boot 3.2.9**: 基础框架
- **Spring Security 6.1.5**: 安全框架
- **JWT**: 令牌管理
- **BCrypt**: 密码加密
- **Swagger/OpenAPI 3**: API文档
- **JUnit 5**: 单元测试

## 快速开始

### 1. 数据库准备

#### 创建数据库
```sql
-- 创建核心业务数据库
CREATE DATABASE iam_core CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建审计日志数据库
CREATE DATABASE iam_audit CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 导入表结构
```bash
# 导入核心业务表结构
mysql -u root -p iam_core < ../../config-init/sql/01-iam-core.sql

# 导入审计日志表结构
mysql -u root -p iam_audit < ../../config-init/sql/04-iam-audit.sql
```

#### 导入测试数据
```bash
# 导入测试数据
mysql -u root -p iam_core < src/main/resources/sql/init-data.sql
```

### 2. 配置数据库连接

修改 `src/main/resources/application-dev.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/iam_core?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: your_username
    password: your_password
```

### 3. 启动服务

```bash
# 启动认证服务
cd services/iam-auth-service
mvn spring-boot:run
```

服务将在 `http://localhost:8081` 启动。

### 4. 访问API文档

启动服务后，可以通过以下地址访问API文档：

- Swagger UI: `http://localhost:8081/doc.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`

## API接口

### 用户登录

**接口地址**: `POST /api/v1/auth/login`

**请求参数**:
```json
{
  "username": "admin",
  "password": "123456",
  "captcha": "1234",
  "captchaKey": "uuid-key",
  "rememberMe": false
}
```

**响应结果**:
```json
{
  "code": 200,
  "message": "操作成功",
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 7200,
    "userInfo": {
      "id": 1,
      "username": "admin",
      "nickname": "管理员",
      "email": "admin@xiaoxin.com",
      "phone": "15888888888",
      "sex": "0",
      "avatar": "",
      "roles": ["admin"],
      "permissions": [],
      "lastLoginTime": "2024-01-01T12:00:00",
      "lastLoginIp": "127.0.0.1"
    }
  }
}
```

### 用户登出

**接口地址**: `POST /api/v1/auth/logout`

**请求头**:
```
Authorization: Bearer <access_token>
```

**响应结果**:
```json
{
  "code": 200,
  "message": "操作成功",
  "success": true,
  "data": null
}
```

### 刷新令牌

**接口地址**: `POST /api/v1/auth/refresh`

**请求参数**:
```
refreshToken: <refresh_token>
```

**响应结果**:
```json
{
  "code": 200,
  "message": "操作成功",
  "success": true,
  "data": "eyJhbGciOiJIUzUxMiJ9..."
}
```

### 验证令牌

**接口地址**: `GET /api/v1/auth/validate`

**请求头**:
```
Authorization: Bearer <access_token>
```

**响应结果**:
```json
{
  "code": 200,
  "message": "操作成功",
  "success": true,
  "data": true
}
```

## 测试账号

| 用户名 | 密码 | 角色 | 状态 | 说明 |
|--------|------|------|------|------|
| admin | 123456 | 超级管理员 | 正常 | 默认管理员账号，拥有所有权限 |

**注意**: 测试账号的密码已使用BCrypt加密存储，实际密码为 `123456`。

## 配置说明

### JWT配置

```yaml
iam:
  security:
    jwt:
      enabled: true
      secret: xiaoxin-iam-jwt-secret-key-2024
      access-token-expiration: 7200000  # 2小时
      refresh-token-expiration: 604800000  # 7天
      issuer: xiaoxin-iam
      audience: xiaoxin-iam-client
```

### 密码策略

```yaml
iam:
  security:
    password:
      min-length: 6
      max-length: 20
      require-uppercase: false
      require-lowercase: false
      require-digits: true
      require-special-chars: false
```

## 开发指南

### 1. 项目结构

```
services/iam-auth-service/
├── src/main/java/com/xiaoxin/iam/auth/
│   ├── controller/          # 控制器层
│   ├── service/            # 服务层
│   └── AuthServiceApplication.java
├── src/main/resources/
│   ├── application.yml     # 主配置文件
│   └── application-dev.yml # 开发环境配置
└── src/test/               # 测试代码
```

### 2. 添加新的认证方式

1. 在 `AuthService` 接口中添加新方法
2. 在 `AuthServiceImpl` 中实现具体逻辑
3. 在 `AuthController` 中添加新的接口
4. 编写相应的测试用例

### 3. 自定义用户数据源

当前使用模拟数据，实际项目中需要：

1. 实现 `UserService` 接口
2. 连接数据库查询用户信息
3. 实现用户权限和角色查询

## 测试

### 运行单元测试

```bash
mvn test
```

### 运行集成测试

```bash
mvn verify
```

### 测试覆盖率

```bash
mvn jacoco:report
```

## 部署

### Docker部署

```bash
# 构建镜像
docker build -t iam-auth-service:1.0.0 .

# 运行容器
docker run -d -p 8081:8081 iam-auth-service:1.0.0
```

### 生产环境配置

1. 修改JWT密钥
2. 配置数据库连接
3. 配置Redis缓存
4. 配置日志级别
5. 配置监控告警

## 常见问题

### 1. 登录失败

- 检查用户名和密码是否正确
- 检查用户状态是否正常
- 查看服务日志获取详细错误信息

### 2. 令牌验证失败

- 检查令牌是否过期
- 检查令牌格式是否正确
- 检查JWT密钥配置

### 3. 接口调用失败

- 检查请求头格式
- 检查参数验证规则
- 查看API文档确认接口规范

## 更新日志

### v1.0.0 (2024-01-01)

- ✅ 实现基础用户登录功能
- ✅ 集成JWT令牌管理
- ✅ 添加密码加密验证
- ✅ 完善异常处理机制
- ✅ 添加API文档
- ✅ 编写单元测试和集成测试

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交代码
4. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证。
