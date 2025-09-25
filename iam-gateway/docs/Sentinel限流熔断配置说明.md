# IAM网关Sentinel限流熔断配置说明

## 概述

IAM网关基于Sentinel实现了完善的限流、熔断、降级功能，支持动态规则配置和实时监控。

## 核心组件

### 1. 配置类
- `SentinelConfig`: 主要配置类，初始化API定义、限流规则和异常处理
- `SentinelCircuitBreakerConfig`: 熔断降级规则配置
- `SentinelNacosConfig`: Nacos数据源配置，支持规则动态更新
- `RateLimitConfig`: 限流参数配置
- `SentinelCircuitBreakerConfig`: 内置熔断参数配置（使用@ConfigurationProperties）

### 2. 管理工具
- `SentinelRuleManager`: 规则管理工具类，支持动态添加/删除规则
- `SentinelController`: REST接口，提供规则管理功能

## 限流配置

### 1. 限流规则
```yaml
# 基础限流配置
rate-limit:
  enabled: true
  default-replenish-rate: 10    # 默认令牌补充速率
  default-burst-capacity: 20    # 默认突发容量
```

### 2. API分组限流
- **认证服务**: 30 QPS，突发 60
- **核心服务**: 60 QPS，突发 120  
- **系统服务**: 40 QPS，突发 80
- **审计服务**: 20 QPS，突发 40
- **前端应用**: 100 QPS，突发 200

### 3. 限流维度
- **IP限流**: 基于客户端IP地址
- **用户限流**: 基于用户ID（需要认证）
- **API限流**: 基于API路径

## 熔断配置

### 1. Sentinel熔断参数
```yaml
sentinel:
  degrade:
    enabled: true
    failure-rate-threshold: 50.0      # 失败率阈值50%
    slow-call-rate-threshold: 50.0    # 慢调用率阈值50%
    slow-call-duration-threshold: 2000 # 慢调用时间阈值2秒
    minimum-number-of-calls: 10       # 最小调用次数
  sliding-window-size: 100          # 滑动窗口大小
```

### 2. 熔断策略
- **错误率熔断**: 当错误率超过50%时触发熔断
- **慢调用熔断**: 当慢调用比例超过50%时触发熔断
- **熔断时长**: 10秒后进入半开状态

## 动态配置

### 1. Nacos集成
规则配置存储在Nacos配置中心，支持实时更新：
- `iam-gateway-flow-rules`: 网关限流规则
- `iam-gateway-sentinel-flow-rules`: 普通限流规则
- `iam-gateway-sentinel-degrade-rules`: 熔断降级规则

### 2. 规则格式
```json
// 限流规则示例
[
  {
    "resource": "auth-api",
    "grade": 1,
    "count": 30,
    "burst": 60,
    "intervalSec": 1
  }
]

// 熔断规则示例
[
  {
    "resource": "iam-auth-service",
    "grade": 0,
    "count": 0.5,
    "timeWindow": 10,
    "minRequestAmount": 10,
    "statIntervalMs": 100000
  }
]
```

## 管理接口

### 1. 规则查询
```bash
# 查询网关限流规则
GET /actuator/sentinel/gateway-flow-rules

# 查询熔断降级规则
GET /actuator/sentinel/degrade-rules
```

### 2. 规则管理
```bash
# 添加网关限流规则
POST /actuator/sentinel/gateway-flow-rules
{
  "resource": "test-api",
  "count": 10,
  "burst": 20
}

# 删除网关限流规则
DELETE /actuator/sentinel/gateway-flow-rules/test-api

# 添加熔断规则
POST /actuator/sentinel/degrade-rules
{
  "resource": "test-service",
  "strategy": 0,
  "threshold": 50.0,
  "minRequestAmount": 10,
  "timeWindow": 10
}

# 清空所有规则
DELETE /actuator/sentinel/rules
```

## 监控指标

### 1. Sentinel控制台
访问Sentinel Dashboard查看实时监控数据：
- 地址: http://localhost:8080
- 用户名/密码: sentinel/sentinel

### 2. 监控指标
- **QPS**: 每秒请求量
- **响应时间**: 平均响应时间
- **异常比例**: 异常请求比例
- **限流次数**: 被限流的请求数量
- **熔断状态**: 熔断器当前状态

## 降级处理

### 1. 限流降级
当触发限流时，返回统一的错误响应：
```json
{
  "code": 429,
  "message": "请求过于频繁，请稍后再试",
  "data": null,
  "timestamp": 1234567890,
  "path": "/api/v1/auth/login"
}
```

### 2. 熔断降级
通过FallbackController提供降级服务：
- `/fallback/auth`: 认证服务降级
- `/fallback/core`: 核心服务降级
- `/fallback/system`: 系统服务降级
- `/fallback/audit`: 审计服务降级

## 最佳实践

### 1. 规则配置建议
- 根据业务重要性设置不同的限流阈值
- 慢调用阈值应该基于P99响应时间设置
- 熔断窗口大小应该覆盖足够的样本数量

### 2. 监控告警
- 配置限流告警，及时发现流量异常
- 配置熔断告警，快速响应服务故障
- 定期检查规则有效性

### 3. 测试验证
- 使用压测工具验证限流效果
- 模拟服务异常验证熔断功能
- 定期进行故障演练

## 故障排查

### 1. 常见问题
- **限流不生效**: 检查规则配置和资源名称
- **熔断误触发**: 调整阈值参数和统计窗口
- **规则不更新**: 检查Nacos连接和配置格式

### 2. 日志分析
关键日志位置：
```
logs/iam-gateway.log
```

搜索关键字：
- `Sentinel`: Sentinel相关日志
- `BlockException`: 限流熔断异常
- `DegradeRule`: 熔断规则相关
