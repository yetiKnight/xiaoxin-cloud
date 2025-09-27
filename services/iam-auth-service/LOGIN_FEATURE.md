# 登录功能实现说明

## 功能概述

认证服务已完善登录功能，支持用户名密码登录，并与iam-core服务集成，实现完整的用户认证流程。

## 架构设计

### 服务调用流程

```
客户端请求
    ↓
iam-auth-service (认证服务)
    ↓ Feign调用
iam-core-service (核心服务)
    ↓ 查询用户信息
数据库 (iam_core)
    ↓ 返回用户信息
iam-auth-service
    ↓ 生成JWT令牌
客户端响应
```

### 事件驱动架构

```
认证服务登录成功
    ↓ 发布事件
LoginEvent (登录事件)
    ↓ 事件监听
iam-audit-service (审计服务)
    ↓ 记录日志
数据库 (iam_audit)
```

## 核心组件

### 1. 登录请求DTO
- **LoginRequest**: 登录请求参数
  - `username`: 用户名（必填，2-20字符）
  - `password`: 密码（必填，6-20字符）
  - `captcha`: 验证码（可选）
  - `captchaKey`: 验证码Key（可选）
  - `rememberMe`: 记住我（可选，默认false）

### 2. 登录响应DTO
- **LoginResponse**: 登录响应数据
  - `accessToken`: 访问令牌
  - `refreshToken`: 刷新令牌
  - `tokenType`: 令牌类型（Bearer）
  - `expiresIn`: 过期时间（秒）
  - `userInfo`: 用户信息

### 3. 用户信息
- **UserInfo**: 用户详细信息
  - `id`: 用户ID
  - `username`: 用户名
  - `nickname`: 昵称
  - `email`: 邮箱
  - `phone`: 手机号
  - `sex`: 性别
  - `avatar`: 头像
  - `roles`: 角色列表
  - `permissions`: 权限列表
  - `lastLoginTime`: 最后登录时间
  - `lastLoginIp`: 最后登录IP

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
      "permissions": ["user:read", "user:create"],
      "lastLoginTime": "2024-01-01T12:00:00",
      "lastLoginIp": "127.0.0.1"
    }
  }
}
```

## 登录流程

### 1. 用户验证
1. 根据用户名查询用户信息（调用iam-core服务）
2. 检查用户状态（正常/停用/删除）
3. 验证密码（BCrypt加密验证）

### 2. 令牌生成
1. 获取用户角色信息
2. 生成JWT访问令牌
3. 生成JWT刷新令牌

### 3. 信息更新
1. 更新用户登录IP
2. 更新用户登录时间
3. 发布登录成功事件

### 4. 审计日志
1. 审计服务监听登录事件
2. 记录登录日志到数据库
3. 支持登录成功/失败记录

## 安全特性

### 1. 密码安全
- 使用BCrypt算法加密存储
- 密码强度验证
- 防止密码暴力破解

### 2. 令牌安全
- JWT令牌包含用户信息
- 令牌过期时间控制
- 支持令牌刷新机制

### 3. 状态验证
- 用户状态检查（正常/停用）
- 删除状态验证
- 防止已删除用户登录

### 4. 审计追踪
- 登录成功/失败记录
- IP地址追踪
- 用户代理记录
- 地理位置信息（待实现）

## 错误处理

### 常见错误码
- `USER_NOT_FOUND`: 用户不存在
- `USER_DISABLED`: 用户已被停用
- `PASSWORD_ERROR`: 密码错误
- `PARAM_ERROR`: 参数错误
- `SYSTEM_ERROR`: 系统异常

### 异常处理
- 统一异常处理机制
- 详细错误信息返回
- 日志记录和监控

## 配置说明

### JWT配置
```yaml
iam:
  security:
    jwt:
      access-token-expiration: 7200000  # 访问令牌过期时间（毫秒）
      refresh-token-expiration: 604800000  # 刷新令牌过期时间（毫秒）
```

### 服务配置
```yaml
spring:
  cloud:
    openfeign:
      client:
        config:
          iam-core-service:
            connect-timeout: 5000
            read-timeout: 10000
```

## 测试说明

### 单元测试
- 控制器层测试
- 服务层测试
- 参数验证测试

### 集成测试
- 服务间调用测试
- 数据库集成测试
- 事件发布测试

### 测试用例
```java
@Test
public void testLoginSuccess() {
    // 测试正常登录流程
}

@Test
public void testLoginValidation() {
    // 测试参数验证
}

@Test
public void testLoginFailure() {
    // 测试登录失败场景
}
```

## 部署说明

### 依赖服务
1. **iam-core-service**: 核心业务服务（必须）
2. **iam-audit-service**: 审计服务（可选）
3. **Nacos**: 服务注册与配置中心
4. **MySQL**: 数据库服务

### 启动顺序
1. 启动Nacos服务
2. 启动iam-core-service
3. 启动iam-audit-service
4. 启动iam-auth-service

### 健康检查
- 服务健康检查: `/actuator/health`
- 服务指标监控: `/actuator/metrics`
- API文档: `/doc.html`

## 扩展功能

### 待实现功能
1. 验证码支持
2. 地理位置解析
3. 设备指纹识别
4. 登录频率限制
5. 单点登录支持

### 性能优化
1. 用户信息缓存
2. 令牌缓存机制
3. 数据库连接池优化
4. 异步事件处理

## 监控告警

### 关键指标
- 登录成功率
- 登录响应时间
- 错误率统计
- 用户活跃度

### 告警规则
- 登录失败率过高
- 响应时间超时
- 服务不可用
- 异常错误频发
