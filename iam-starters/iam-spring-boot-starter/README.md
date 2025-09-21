# IAM Spring Boot Starter

IAM平台基础Spring Boot Starter，提供IAM平台的核心功能支持。

## 功能特性

### 🎯 核心特性

- ✅ **统一异常处理**: 全局异常处理器，统一异常响应格式
- ✅ **统一响应封装**: 自动包装响应结果为统一格式
- ✅ **操作日志**: 自动记录操作日志，支持自定义配置
- ✅ **性能监控**: 自动监控方法执行时间，识别慢查询
- ✅ **异步任务**: 配置异步任务执行器，支持并发处理
- ✅ **配置管理**: 灵活的配置属性管理

### 📦 包含依赖

- IAM Common (公共模块)
- Spring Boot Starter
- Spring Boot Configuration Processor
- Spring Boot Actuator
- Spring Boot AOP
- Spring Boot Validation
- Lombok
- Jackson
- Hutool

## 使用方式

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.xiaoxin</groupId>
    <artifactId>iam-spring-boot-starter</artifactId>
</dependency>
```

### 2. 配置属性

```yaml
iam:
  # 基础配置
  enabled: true
  application-name: iam-platform
  version: 1.0.0
  enable-unified-response: true
  enable-global-exception-handler: true
  enable-audit-log: true
  
  # 核心功能配置
  core:
    enable-operation-log: true
    enable-performance-monitor: true
    enable-request-trace: true
    
    # 线程池配置
    thread-pool:
      core-size: 8
      max-size: 20
      queue-capacity: 1000
      keep-alive-time: 60s
      thread-name-prefix: iam-task-
      wait-for-tasks-to-complete-on-shutdown: true
      await-termination-seconds: 60s
    
    # 任务配置
    task:
      enable-async: true
      enable-scheduling: true
```

## 功能详解

### 1. 统一异常处理

自动处理以下异常类型：
- `BusinessException`: 业务异常
- `MethodArgumentNotValidException`: 参数校验异常
- `BindException`: 参数绑定异常
- `ConstraintViolationException`: 约束违反异常
- `IllegalArgumentException`: 非法参数异常
- `RuntimeException`: 运行时异常
- `Exception`: 通用异常

```java
@RestController
public class UserController {
    
    @PostMapping("/users")
    public User createUser(@Valid @RequestBody UserCreateDTO dto) {
        // 如果校验失败，会自动被全局异常处理器捕获
        return userService.createUser(dto);
    }
}
```

### 2. 统一响应封装

自动将返回结果包装为统一的Response格式：

```java
@RestController
public class UserController {
    
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        // 返回的User对象会自动被包装为Result<User>
        return userService.getUser(id);
    }
}
```

响应格式：
```json
{
    "code": 0,
    "message": "success",
    "data": {
        "id": 1,
        "username": "admin"
    },
    "timestamp": "2024-01-01T00:00:00Z"
}
```

### 3. 操作日志

使用`@OperationLog`注解自动记录操作日志：

```java
@Service
public class UserService {
    
    @OperationLog(module = "用户管理", operation = "创建用户")
    public User createUser(UserCreateDTO dto) {
        // 会自动记录操作日志
        return userRepository.save(dto.toEntity());
    }
}
```

### 4. 性能监控

自动监控Service和Controller层方法的执行时间：

- 执行时间超过1秒的方法会记录为慢查询警告
- 方法执行异常会被记录
- 支持调试级别的性能日志

### 5. 异步任务

启用异步任务支持：

```java
@Service
public class NotificationService {
    
    @Async
    public CompletableFuture<Void> sendEmailAsync(String email, String content) {
        // 异步发送邮件
        emailService.send(email, content);
        return CompletableFuture.completedFuture(null);
    }
}
```

## 配置属性

### 基础配置 (`iam.*`)

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `enabled` | Boolean | true | 是否启用IAM平台功能 |
| `application-name` | String | iam-platform | 应用名称 |
| `version` | String | 1.0.0 | 版本信息 |
| `enable-unified-response` | Boolean | true | 是否启用统一响应格式 |
| `enable-global-exception-handler` | Boolean | true | 是否启用全局异常处理 |
| `enable-audit-log` | Boolean | true | 是否启用审计日志 |

### 核心配置 (`iam.core.*`)

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `enable-operation-log` | Boolean | true | 是否启用操作日志 |
| `enable-performance-monitor` | Boolean | true | 是否启用性能监控 |
| `enable-request-trace` | Boolean | true | 是否启用请求追踪 |

### 线程池配置 (`iam.core.thread-pool.*`)

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `core-size` | Integer | 8 | 核心线程数 |
| `max-size` | Integer | 20 | 最大线程数 |
| `queue-capacity` | Integer | 1000 | 队列容量 |
| `keep-alive-time` | Duration | 60s | 线程存活时间 |
| `thread-name-prefix` | String | iam-task- | 线程名称前缀 |
| `wait-for-tasks-to-complete-on-shutdown` | Boolean | true | 是否等待任务完成 |
| `await-termination-seconds` | Duration | 60s | 等待终止时间 |

### 任务配置 (`iam.core.task.*`)

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `enable-async` | Boolean | true | 是否启用异步任务 |
| `enable-scheduling` | Boolean | true | 是否启用定时任务 |

## 自动配置

本Starter会自动配置以下Bean：

1. `GlobalExceptionHandler` - 全局异常处理器
2. `IamResponseBodyAdvice` - 统一响应体处理器
3. `OperationLogAspect` - 操作日志切面
4. `PerformanceMonitorAspect` - 性能监控切面
5. `TaskExecutor` - 异步任务执行器

## 扩展自定义

### 1. 自定义异常处理

```java
@Component
public class CustomExceptionHandler {
    
    @EventListener
    public void handleBusinessException(BusinessException e) {
        // 自定义异常处理逻辑
    }
}
```

### 2. 自定义响应格式

```java
@Configuration
public class ResponseConfig {
    
    @Bean
    @Primary
    public IamResponseBodyAdvice customResponseBodyAdvice() {
        return new CustomResponseBodyAdvice();
    }
}
```

### 3. 自定义操作日志

```java
@Component
public class CustomOperationLogHandler {
    
    @EventListener
    public void handleOperationLog(OperationLogEvent event) {
        // 自定义操作日志处理逻辑
    }
}
```

## 注意事项

1. **性能监控**: 默认监控Service和Controller层，如需监控其他层级可自定义切点
2. **异步任务**: 确保异步方法不在同一个类中调用，否则AOP不会生效
3. **操作日志**: 需要配合`@OperationLog`注解使用
4. **线程池**: 根据实际业务场景调整线程池参数

## 版本历史

### 1.0.0-SNAPSHOT
- 初始版本
- 提供基础功能支持
- 统一异常处理和响应封装
- 操作日志和性能监控
- 异步任务支持
