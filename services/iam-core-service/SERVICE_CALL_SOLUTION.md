# 服务间调用403问题解决方案

## 问题描述
认证服务调用核心服务时出现403错误：
```
feign.FeignException$Forbidden: [403] during [GET] to [http://iam-core-service/api/v1/users/username/admin]
```

## 问题根因
核心服务引入了Spring Security依赖，但没有配置安全策略，导致所有请求都被默认的安全策略拦截。

## 解决方案

### 1. 创建核心服务安全配置
**文件**: `src/main/java/com/xiaoxin/iam/core/config/CoreSecurityConfig.java`

**功能**:
- 允许内部API路径匿名访问（服务间调用）
- 允许健康检查和文档接口访问
- 其他请求需要认证

**允许匿名访问的路径**:
- `/api/v1/internal/**` - 内部API路径
- `/actuator/**` - 健康检查接口
- `/doc.html`, `/swagger-ui/**`, `/v3/api-docs/**` - API文档接口

### 2. 创建内部API控制器
**文件**: `src/main/java/com/xiaoxin/iam/core/controller/InternalUserController.java`

**功能**:
- 提供专门的服务间调用接口
- 路径前缀为 `/api/v1/internal/users`
- 不包含权限验证注解

**主要接口**:
- `GET /api/v1/internal/users/username/{username}` - 根据用户名查询用户
- `POST /api/v1/internal/users/{userId}/login-info` - 更新用户登录信息
- `GET /api/v1/internal/users/{userId}` - 根据ID查询用户
- 其他用户相关查询接口

### 3. 更新认证服务Feign客户端
**文件**: `services/iam-auth-service/src/main/java/com/xiaoxin/iam/auth/client/CoreServiceClient.java`

**修改**:
- 将路径从 `/api/v1` 改为 `/api/v1/internal`
- 调用内部API接口，避免权限验证

### 4. 创建核心服务常量类
**文件**: `src/main/java/com/xiaoxin/iam/core/constant/CoreConstants.java`

**功能**:
- 统一管理路径常量
- 避免硬编码
- 便于维护和扩展

## 架构设计

### 服务间调用流程
```
认证服务 -> Feign客户端 -> 核心服务内部API -> 业务逻辑
```

### 安全策略分层
1. **网关层**: 统一入口，路由转发
2. **服务层**: 内部API允许服务间调用
3. **业务层**: 外部API需要认证授权

### 路径设计
- **外部API**: `/api/v1/users/**` - 需要认证
- **内部API**: `/api/v1/internal/users/**` - 服务间调用
- **系统API**: `/actuator/**` - 健康检查

## 测试验证

### 1. 启动服务
```bash
# 启动核心服务
cd services/iam-core-service
mvn spring-boot:run

# 启动认证服务
cd services/iam-auth-service
mvn spring-boot:run
```

### 2. 测试服务间调用
```bash
# 测试登录接口
curl -X POST http://localhost:8081/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "123456"
  }'
```

### 3. 验证内部API
```bash
# 直接调用内部API（应该成功）
curl -X GET http://localhost:8082/api/v1/internal/users/username/admin

# 调用外部API（应该返回403）
curl -X GET http://localhost:8082/api/v1/users/username/admin
```

## 优势

### 1. 安全性
- 内部API和外部API分离
- 服务间调用无需认证，提高性能
- 外部API仍需要完整的认证授权

### 2. 可维护性
- 路径常量统一管理
- 配置清晰，便于理解
- 符合微服务架构规范

### 3. 可扩展性
- 可以轻松添加新的内部API
- 支持服务间认证机制（如需要）
- 便于监控和日志记录

## 注意事项

1. **内部API安全**: 虽然内部API允许匿名访问，但应该通过网络安全策略保护
2. **监控告警**: 建议对内部API调用进行监控和告警
3. **文档维护**: 内部API也需要文档，便于其他服务调用
4. **版本管理**: 内部API的版本变更需要通知所有调用方

## 后续优化

1. **服务间认证**: 可以添加服务间认证机制，提高安全性
2. **API网关**: 通过API网关统一管理服务间调用
3. **监控告警**: 添加服务间调用的监控和告警
4. **文档生成**: 自动生成内部API文档
