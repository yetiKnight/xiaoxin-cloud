# OAuth2 Client Credentials 服务间认证架构方案

## 方案概述

采用OAuth2的`client_credentials`授权模式解决服务间调用认证问题，实现安全、标准、易扩展的服务间通信机制。

## 架构设计

### 1. 认证流程

```
服务A调用服务B
    ↓
Feign拦截器检查是否为内部API
    ↓
获取/刷新OAuth2访问令牌
    ↓
向认证服务请求client_credentials令牌
    ↓
认证服务验证客户端凭据
    ↓
返回访问令牌
    ↓
Feign添加Authorization头
    ↓
服务B验证OAuth2令牌
    ↓
调用认证服务introspect接口验证
    ↓
验证通过，执行业务逻辑
```

### 2. 服务角色

| 服务 | 角色 | 功能 |
|---|---|---|
| iam-auth-service | 授权服务器 | 颁发和验证OAuth2令牌 |
| iam-core-service | 资源服务器 | 验证令牌，提供受保护资源 |
| iam-audit-service | 客户端+资源服务器 | 既调用其他服务，也提供服务 |
| iam-system-service | 客户端+资源服务器 | 既调用其他服务，也提供服务 |

## 核心组件

### 1. OAuth2客户端凭据服务

**文件**: `services/iam-auth-service/src/main/java/com/xiaoxin/iam/auth/service/OAuth2ClientCredentialsService.java`

**功能**:
- 验证客户端凭据
- 生成访问令牌
- 令牌内省验证
- 令牌撤销

**关键方法**:
```java
// 获取访问令牌
public Result<OAuth2AccessTokenResponse> getAccessToken(String clientId, String clientSecret, String scope)

// 验证令牌
public Result<OAuth2Authorization> validateToken(String token)

// 撤销令牌
public Result<Void> revokeToken(String token)
```

### 2. OAuth2令牌控制器

**文件**: `services/iam-auth-service/src/main/java/com/xiaoxin/iam/auth/controller/OAuth2TokenController.java`

**接口**:
- `POST /oauth2/token` - 获取访问令牌
- `POST /oauth2/introspect` - 验证令牌
- `POST /oauth2/revoke` - 撤销令牌

### 3. Feign客户端认证拦截器

**文件**: `iam-common/src/main/java/com/xiaoxin/iam/common/feign/OAuth2ClientCredentialsInterceptor.java`

**功能**:
- 自动为内部API调用添加OAuth2令牌
- 令牌缓存和自动刷新
- 异常处理和重试机制

**特性**:
- 智能缓存：令牌提前30秒刷新
- 自动重试：网络异常时自动重试
- 性能优化：避免重复获取令牌

### 4. 资源服务器安全配置

**文件**: `services/iam-core-service/src/main/java/com/xiaoxin/iam/core/config/CoreSecurityConfig.java`

**配置**:
```java
.oauth2ResourceServer(oauth2 -> oauth2
    .opaqueToken(opaque -> opaque
        .introspectionUri("http://iam-auth-service/oauth2/introspect")
        .introspectionClientCredentials("iam-core-service", "core-service-secret")
    )
)
```

## 数据库设计

### OAuth2客户端表

**表名**: `oauth2_registered_client`

**服务客户端配置**:
```sql
-- 核心服务客户端
INSERT INTO oauth2_registered_client VALUES (
    'iam-core-service',
    'iam-core-service', 
    '{bcrypt}$2a$10$...',
    '核心服务客户端',
    'client_secret_basic,client_secret_post',
    'client_credentials',
    '',
    'internal:read,internal:write,user:read,user:write'
);
```

## 权限模型

### 作用域设计

| 作用域 | 说明 | 适用服务 |
|---|---|---|
| internal:read | 内部读取权限 | 所有服务 |
| internal:write | 内部写入权限 | 所有服务 |
| user:read | 用户数据读取 | 核心服务 |
| user:write | 用户数据写入 | 核心服务 |
| audit:read | 审计数据读取 | 审计服务 |
| audit:write | 审计数据写入 | 审计服务 |
| system:read | 系统数据读取 | 系统服务 |
| system:write | 系统数据写入 | 系统服务 |

### 权限控制

```java
// 内部用户API需要用户相关权限
.requestMatchers("/api/v1/internal/users/**")
    .hasAnyAuthority("SCOPE_internal:read", "SCOPE_internal:write", 
                     "SCOPE_user:read", "SCOPE_user:write")
```

## 配置管理

### 1. 认证服务配置

**文件**: `config-init/config/dev/iam-auth-service-dev.yml`

```yaml
oauth2:
  client:
    enabled: false  # 认证服务本身不需要作为客户端
    client-secret: iam-auth-secret-2024
```

### 2. 资源服务配置

**文件**: `config-init/config/dev/iam-core-service-dev.yml`

```yaml
oauth2:
  client:
    enabled: true  # 启用OAuth2客户端
    client-secret: iam-core-secret-2024
    token-uri: http://iam-auth-service/oauth2/token
    scope: internal:read internal:write user:read user:write
```

## 安全特性

### 1. 客户端凭据安全

- **密钥加密**: 使用BCrypt加密存储客户端密钥
- **密钥轮换**: 支持定期更新客户端密钥
- **作用域限制**: 严格限制客户端可访问的资源范围

### 2. 令牌安全

- **短期有效**: 访问令牌有效期1小时
- **透明令牌**: 使用Opaque Token，避免JWT信息泄露
- **实时验证**: 每次请求都进行令牌内省验证
- **快速撤销**: 支持令牌立即撤销

### 3. 通信安全

- **HTTPS传输**: 生产环境强制使用HTTPS
- **请求签名**: 可选的请求签名验证
- **IP白名单**: 可配置允许的客户端IP范围

### 4. 监控和审计

- **访问日志**: 记录所有令牌获取和验证请求
- **异常告警**: 认证失败时发送告警
- **性能监控**: 监控令牌获取和验证的性能

## 使用示例

### 1. 服务间调用

```java
@FeignClient(name = "iam-core-service", path = "/api/v1/internal")
public interface CoreServiceClient {
    @GetMapping("/users/username/{username}")
    Result<UserDTO> getUserByUsername(@PathVariable String username);
}
```

### 2. 手动获取令牌

```bash
curl -X POST http://localhost:8081/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials" \
  -d "client_id=iam-core-service" \
  -d "client_secret=iam-core-secret-2024" \
  -d "scope=internal:read internal:write"
```

### 3. 令牌验证

```bash
curl -X POST http://localhost:8081/oauth2/introspect \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "token=ACCESS_TOKEN_HERE"
```

## 部署和运维

### 1. 环境变量

```bash
# 客户端凭据
export OAUTH2_CLIENT_SECRET=your-service-secret-here

# 令牌端点
export OAUTH2_TOKEN_URI=http://iam-auth-service/oauth2/token

# 作用域
export OAUTH2_CLIENT_SCOPE="internal:read internal:write"
```

### 2. 监控指标

- 令牌获取成功率
- 令牌验证延迟
- 认证失败次数
- 客户端调用频率

### 3. 故障处理

- **令牌获取失败**: 检查客户端凭据和网络连接
- **令牌验证失败**: 检查令牌是否过期或被撤销
- **权限不足**: 检查客户端作用域配置

## 优势总结

1. **标准化**: 采用OAuth2标准，易于理解和维护
2. **安全性**: 多层安全防护，符合企业安全要求
3. **性能**: 令牌缓存和批量验证，减少网络开销
4. **扩展性**: 易于添加新服务和权限控制
5. **监控性**: 完整的日志和监控体系
6. **兼容性**: 与现有Spring Security生态完美集成

这个方案完美解决了您提到的服务间调用认证问题，既保证了安全性，又保持了架构的简洁和高性能。
