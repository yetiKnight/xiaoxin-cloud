# IAM认证服务配置说明

## 配置文件结构

### 1. 本地配置文件
- **`application.yml`**: 基础配置，包含服务端口、应用名称、管理端点等
- **`application-dev.yml`**: 开发环境特定配置，包含Nacos连接、Sentinel配置、日志配置等

### 2. Nacos配置中心
- **`iam-common.yml`**: 公共配置，包含数据源、Redis、MyBatis-Plus、Seata等
- **`iam-auth-service-dev.yml`**: 认证服务特定配置，包含数据源、OAuth2、第三方登录等

## 配置分层原则

### 本地配置优先级 > Nacos配置
1. **本地配置**：服务发现、Sentinel、日志级别等运行时配置
2. **Nacos配置**：数据源、业务配置、第三方集成等业务相关配置

## 主要配置项说明

### 服务注册发现
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: ${NACOS_SERVER_ADDR:localhost:8848}
        namespace: ${NACOS_NAMESPACE:dev}
        group: IAM_GROUP
        cluster-name: ${spring.profiles.active}
        metadata:
          version: ${project.version:1.0.0}
          zone: ${app.zone:default}
```

### Sentinel限流熔断
```yaml
spring:
  cloud:
    sentinel:
      transport:
        port: 8718
        dashboard: ${SENTINEL_DASHBOARD:localhost:8181}
      datasource:
        flow:
          nacos:
            server-addr: ${NACOS_SERVER_ADDR:localhost:8848}
            namespace: ${NACOS_NAMESPACE:dev}
            dataId: iam-auth-service-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow
```

### 日志配置
- **本地配置**：详细的调试日志级别
- **Nacos配置**：基础的生产日志级别

## 环境变量

### 必需的环境变量
- `NACOS_SERVER_ADDR`: Nacos服务器地址
- `NACOS_NAMESPACE`: Nacos命名空间
- `SENTINEL_DASHBOARD`: Sentinel控制台地址

### 可选的环境变量
- `REDIS_HOST`: Redis主机地址
- `REDIS_PORT`: Redis端口
- `REDIS_PASSWORD`: Redis密码
- `JWT_SECRET`: JWT密钥
- `OAUTH2_ISSUER`: OAuth2颁发者

## 配置刷新

### 支持动态刷新的配置
- 数据源配置
- Redis配置
- 日志级别
- 业务配置

### 不支持动态刷新的配置
- 服务注册发现配置
- Sentinel配置
- 管理端点配置

## 最佳实践

1. **配置分离**：将环境相关配置放在本地，业务配置放在Nacos
2. **配置优先级**：明确本地配置和Nacos配置的优先级
3. **配置验证**：使用`import-check.enabled: true`验证配置导入
4. **日志管理**：本地配置详细日志，Nacos配置生产日志
5. **环境隔离**：通过namespace和group实现环境隔离

## 故障排查

### 常见问题
1. **配置不生效**：检查配置优先级和刷新机制
2. **服务注册失败**：检查Nacos连接配置
3. **Sentinel规则不生效**：检查数据源配置
4. **日志级别不生效**：检查本地配置优先级

### 调试建议
1. 启用详细日志：`logging.level.com.xiaoxin.iam.auth: debug`
2. 检查配置导入：查看启动日志中的配置导入信息
3. 验证Nacos连接：检查服务注册和配置拉取状态
