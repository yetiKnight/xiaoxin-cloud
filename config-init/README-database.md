# IAM平台数据库配置说明

## 概述

本文档说明IAM平台的数据库配置架构、连接配置、性能优化和监控配置。

## 数据库架构

### 数据库分布

| 数据库名 | 服务 | 端口 | 用途 | Redis库 |
|---------|------|------|------|---------|
| `-` | iam-gateway | 8080 | 网关服务 | 1 |
| `iam_core` | iam-core-service | 8082 | 核心业务数据 | 2 |
| `iam_auth` | iam-auth-service | 8081 | 认证授权数据 | 3 |
| `iam_system` | iam-system-service | 8084 | 系统配置数据 | 4 |
| `iam_audit` | iam-audit-service | 8083 | 审计日志数据 | 5 |

### 数据存储策略

- **MySQL**: 核心业务数据、配置数据、结构化日志
- **Redis**: 缓存、会话、限流计数
- **MongoDB**: 非结构化审计数据、文档存储
- **Elasticsearch**: 日志搜索、数据分析

## 配置文件结构

```
config-init/config/
├── iam-common.yml                    # 公共配置
├── iam-auth-service-dev.yml         # 认证服务配置
├── iam-core-service-dev.yml         # 核心服务配置
├── iam-system-service-dev.yml       # 系统服务配置
├── iam-audit-service-dev.yml        # 审计服务配置
└── iam-gateway-dev.yml              # 网关配置
```

## 数据库连接配置

### 基础连接参数

```yaml
# 数据库配置
db:
  driver-class-name: com.mysql.cj.jdbc.Driver
  url: jdbc:mysql://localhost:3306/iam_core?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
  username: root
  password: 123456
```

### 连接池配置

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      # 连接池核心参数
      minimum-idle: 10             # 最小空闲连接数
      maximum-pool-size: 100       # 最大连接数
      auto-commit: true            # 自动提交
      idle-timeout: 300000         # 空闲超时时间(ms)
      max-lifetime: 1800000         # 连接最大生命周期(ms)
      connection-timeout: 30000    # 连接超时时间(ms)
      connection-test-query: SELECT 1  # 连接测试查询
      pool-name: IamPlatformHikariCP   # 连接池名称
```

## Redis配置

### 各服务Redis数据库分配

- **Gateway**: database 0 (限流、路由缓存)
- **Auth**: database 1 (JWT Token、验证码)
- **Core**: database 2 (用户会话、权限缓存)
- **Audit**: database 3 (审计缓存、统计数据)
- **System**: database 4 (字典缓存、配置缓存)

### Redis连接配置

```yaml
spring:
  data:
    redis:
      host: ${redis.host:localhost}
      port: ${redis.port:6379}
      password: ${redis.password:}
      database: ${redis.database.core:2}
      timeout: 10000ms
      lettuce:
        pool:
          max-active: 8
          max-wait: -1ms
          max-idle: 8
          min-idle: 0
```

## 配置部署

### 1. Nacos配置导入

```bash
# 执行配置导入脚本
cd config-init
./nacos-config-import.sh
```

### 2. 环境变量配置

```bash
# 数据库配置
export DB_HOST=localhost
export DB_PORT=3306
export DB_USERNAME=root
export DB_PASSWORD=your_password

# Redis配置
export REDIS_HOST=localhost
export REDIS_PORT=6379
export REDIS_PASSWORD=your_password

# Nacos配置
export NACOS_SERVER_ADDR=localhost:8848
export NACOS_NAMESPACE=dev
```

### 3. 启动顺序

1. **基础服务**: MySQL、Redis、Nacos
2. **中间件**: RocketMQ、Elasticsearch、MongoDB
3. **IAM服务**: 按依赖顺序启动各微服务

## 数据库监控

### HikariCP监控

- **监控端点**: http://localhost:8082/actuator/metrics/hikaricp.connections
- **健康检查**: http://localhost:8082/actuator/health
- **Prometheus指标**: http://localhost:8082/actuator/prometheus

### 监控指标

#### 连接池指标
- 活跃连接数 (hikaricp.connections.active)
- 空闲连接数 (hikaricp.connections.idle)
- 等待连接数 (hikaricp.connections.pending)
- 连接获取次数 (hikaricp.connections.acquisition)

#### SQL执行指标
- SQL执行次数
- 平均执行时间
- 慢SQL统计
- 错误SQL统计

#### 事务指标
- 事务提交次数
- 事务回滚次数
- 事务执行时间

### 告警配置

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "health,info,metrics,prometheus"
  endpoint:
    health:
      show-details: always
  prometheus:
    metrics:
      export:
        enabled: true
```

## 性能优化建议

### 1. 连接池优化

- **连接数设置**: maximum-pool-size = CPU核数 × 2
- **空闲连接**: minimum-idle = 10
- **连接验证**: 启用connection-test-query
- **连接回收**: 设置idle-timeout为5分钟

### 2. SQL优化

- **慢SQL监控**: 通过MyBatis-Plus配置
- **批量操作**: 使用批处理减少网络开销
- **索引优化**: 定期分析慢SQL并优化索引
- **查询优化**: 避免N+1查询，使用JOIN优化

### 3. 事务优化

- **事务范围**: 最小化事务范围，避免长事务
- **隔离级别**: 根据业务需求选择合适隔离级别
- **读写分离**: 读操作使用只读数据源
- **分布式事务**: 谨慎使用，优先考虑最终一致性

### 4. 缓存策略

- **多级缓存**: 本地缓存 + Redis缓存
- **缓存预热**: 系统启动时预加载热点数据
- **缓存更新**: 使用事件驱动更新缓存
- **缓存穿透**: 使用布隆过滤器防止缓存穿透

## 故障排查

### 常见问题

#### 1. 连接池耗尽
- **现象**: 获取连接超时
- **原因**: 并发量过大或连接泄漏
- **解决**: 调整max-active，检查连接是否正确释放

#### 2. 慢SQL问题
- **现象**: 响应时间长
- **原因**: SQL执行效率低
- **解决**: 分析执行计划，优化SQL和索引

#### 3. 数据库死锁
- **现象**: 事务回滚
- **原因**: 并发事务访问顺序不一致
- **解决**: 统一锁获取顺序，缩短事务时间

#### 4. 内存泄漏
- **现象**: 应用内存持续增长
- **原因**: 连接未释放或大结果集
- **解决**: 检查连接使用，分页查询大数据

### 监控命令

```bash
# 查看连接数
SHOW PROCESSLIST;

# 查看事务状态
SELECT * FROM information_schema.INNODB_TRX;

# 查看锁等待
SELECT * FROM information_schema.INNODB_LOCKS;

# 查看慢查询
SELECT * FROM mysql.slow_log ORDER BY start_time DESC LIMIT 10;
```

## 备份与恢复

### 备份策略

- **全量备份**: 每天凌晨2点执行
- **增量备份**: 每小时执行一次
- **日志备份**: 实时备份binlog
- **备份保留**: 全量30天，增量7天

### 备份脚本

```bash
#!/bin/bash
# 数据库备份脚本
BACKUP_DIR="/data/backup/mysql"
DATE=$(date +%Y%m%d_%H%M%S)

# 备份所有IAM数据库
mysqldump -h localhost -u root -p --single-transaction \
  --routines --triggers --all-databases \
  > "$BACKUP_DIR/iam_full_$DATE.sql"
```

### 恢复流程

1. **停止服务**: 停止所有IAM服务
2. **恢复数据**: 执行备份文件恢复
3. **验证数据**: 检查数据完整性
4. **启动服务**: 按顺序启动服务

## 安全配置

### 1. 数据库安全

- **用户权限**: 最小权限原则
- **网络访问**: 限制访问来源IP
- **SSL连接**: 启用SSL加密传输
- **密码策略**: 强密码策略

### 2. 连接安全

- **连接加密**: 使用SSL连接
- **密码加密**: 配置文件密码加密
- **访问控制**: IP白名单限制
- **审计日志**: 记录所有数据库操作

这个配置说明涵盖了IAM平台数据库配置的各个方面，为开发和运维人员提供了完整的参考指南。
