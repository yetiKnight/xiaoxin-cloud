# IAM Common 异常处理模块

## 概述

本模块提供了完整的异常处理机制，包括业务异常、认证异常、权限异常、数据异常等多种异常类型，以及全局异常处理器和异常工具类。

## 异常类型

### 1. BusinessException - 业务异常
基础业务异常类，所有业务异常都应该继承此类。

```java
// 基本用法
throw new BusinessException("业务操作失败");

// 带错误码
throw new BusinessException(1001, "用户不存在");

// 带详情
throw new BusinessException("用户操作失败", "用户ID: 123");

// 静态方法创建
throw BusinessException.of("业务异常");
throw BusinessException.of(1001, "用户不存在");
```

### 2. AuthException - 认证异常
处理认证相关的异常。

```java
// Token相关异常
throw AuthException.tokenInvalid();
throw AuthException.tokenExpired();
throw AuthException.tokenMissing();

// 登录相关异常
throw AuthException.loginFailed();
throw AuthException.loginFailed("用户名或密码错误");

// 用户相关异常
throw AuthException.userNotFound();
throw AuthException.userDisabled();
throw AuthException.userLocked();
throw AuthException.passwordError();
```

### 3. PermissionException - 权限异常
处理权限相关的异常。

```java
// 权限不足
throw PermissionException.denied();
throw PermissionException.denied("没有访问该资源的权限");

// 角色权限异常
throw PermissionException.roleNotFound();
throw PermissionException.permissionNotFound();

// 访问拒绝
throw PermissionException.accessDenied();
```

### 4. DataException - 数据异常
处理数据相关的异常。

```java
// 数据库操作异常
throw DataException.databaseError();
throw DataException.databaseError("连接超时");

// 数据不存在
throw DataException.dataNotFound();
throw DataException.dataNotFound("用户ID: 123");

// 数据已存在
throw DataException.dataAlreadyExists();
throw DataException.userAlreadyExists();
```

### 5. ValidationException - 参数验证异常
处理参数验证相关的异常。

```java
// 基本参数错误
throw ValidationException.paramError();
throw ValidationException.paramError("参数格式错误");

// 必填参数缺失
throw ValidationException.requiredParamMissing("userId");

// 参数格式错误
throw ValidationException.paramFormatError("email", "邮箱格式");

// 参数值超出范围
throw ValidationException.paramOutOfRange("age", "0-150");
```

### 6. SystemException - 系统异常
处理系统相关的异常。

```java
// 系统内部错误
throw SystemException.internalError();
throw SystemException.internalError("服务不可用");

// 第三方服务异常
throw SystemException.thirdPartyServiceError();
throw SystemException.smsSendFailed();
throw SystemException.emailSendFailed();

// 文件操作异常
throw SystemException.fileUploadFailed();
throw SystemException.fileNotFound();
```

## 异常工具类

### ExceptionUtils
提供异常处理的工具方法。

```java
// 获取异常堆栈信息
String stackTrace = ExceptionUtils.getStackTrace(throwable);

// 获取异常根原因
Throwable rootCause = ExceptionUtils.getRootCause(throwable);

// 获取异常链
List<Throwable> chain = ExceptionUtils.getExceptionChain(throwable);

// 检查异常类型
boolean isBusiness = ExceptionUtils.isBusinessException(throwable);

// 获取异常消息
String message = ExceptionUtils.getMessage(throwable);
String shortMessage = ExceptionUtils.getShortMessage(throwable);

// 记录异常日志
ExceptionUtils.logException(throwable);
ExceptionUtils.logException(throwable, "操作失败");
ExceptionUtils.logException(throwable, "操作失败", "userId", 123, "action", "login");

// 包装异常
BusinessException businessException = ExceptionUtils.wrapAsBusinessException(throwable);

// 安全执行
String result = ExceptionUtils.safeExecute(() -> {
    // 可能抛出异常的代码
    return "result";
}, "defaultValue");

ExceptionUtils.safeExecute(() -> {
    // 可能抛出异常的代码
}, "操作失败");
```

## 全局异常处理器

### GlobalExceptionHandler
自动处理各种类型的异常，并返回统一的响应格式。

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.failed(e.getCode(), e.getMessage());
    }
    
    // 其他异常处理方法...
}
```

## 异常处理配置

### ExceptionConfig
配置异常处理的各种参数。

```yaml
iam:
  exception:
    enabled: true                    # 是否启用异常处理
    log-stack-trace: true           # 是否记录异常堆栈信息
    log-detail: true                # 是否记录异常详情
    log-level: WARN                 # 异常日志级别
    enable-monitoring: true         # 是否启用异常监控
    monitoring-threshold: 100       # 异常监控阈值
    enable-alert: false             # 是否启用异常告警
    alert-threshold: 1000           # 异常告警阈值
    enable-statistics: true         # 是否启用异常统计
    statistics-window: 60           # 异常统计时间窗口（分钟）
    enable-cache: true              # 是否启用异常缓存
    cache-expire-time: 300          # 异常缓存过期时间（秒）
    enable-retry: false             # 是否启用异常重试
    retry-count: 3                  # 异常重试次数
    retry-interval: 1000            # 异常重试间隔（毫秒）
    enable-fallback: false          # 是否启用异常降级
    fallback-strategy: default      # 异常降级策略
    enable-circuit-breaker: false   # 是否启用异常熔断
    circuit-breaker-failure-threshold: 10  # 熔断器失败阈值
    circuit-breaker-recovery-time: 60      # 熔断器恢复时间（秒）
    enable-rate-limit: false        # 是否启用异常限流
    rate-limit-threshold: 100       # 限流阈值（每秒）
    rate-limit-window: 60           # 限流时间窗口（秒）
```

## 使用示例

### 1. 在Service层使用异常

```java
@Service
public class UserService {
    
    public User getUserById(Long userId) {
        if (userId == null) {
            throw ValidationException.requiredParamMissing("userId");
        }
        
        User user = userRepository.findById(userId);
        if (user == null) {
            throw DataException.userNotFound();
        }
        
        if (!user.isEnabled()) {
            throw AuthException.userDisabled();
        }
        
        return user;
    }
    
    public void createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw DataException.userAlreadyExists();
        }
        
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw DataException.databaseError("用户创建失败", e);
        }
    }
}
```

### 2. 在Controller层使用异常

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return Result.success(user);
        } catch (BusinessException e) {
            // 异常会被GlobalExceptionHandler自动处理
            throw e;
        }
    }
    
    @PostMapping
    public Result<Void> createUser(@RequestBody @Valid User user) {
        userService.createUser(user);
        return Result.success();
    }
}
```

### 3. 异常监控和统计

```java
@Component
public class ExceptionMonitor {
    
    @Autowired
    private ExceptionHandlerAdvice exceptionHandlerAdvice;
    
    public void monitorExceptions() {
        // 获取异常统计信息
        Map<String, AtomicLong> statistics = exceptionHandlerAdvice.getExceptionStatistics();
        
        // 获取异常监控信息
        Map<String, AtomicLong> monitoring = exceptionHandlerAdvice.getExceptionMonitoring();
        
        // 获取异常总数
        long totalCount = exceptionHandlerAdvice.getTotalExceptionCount();
        
        log.info("异常统计: {}", statistics);
        log.info("异常监控: {}", monitoring);
        log.info("异常总数: {}", totalCount);
    }
}
```

## 最佳实践

### 1. 异常分类
- 使用不同的异常类型表示不同的错误场景
- 业务异常使用BusinessException及其子类
- 系统异常使用SystemException
- 参数验证异常使用ValidationException

### 2. 异常信息
- 提供清晰的错误消息
- 包含必要的上下文信息
- 避免暴露敏感信息

### 3. 异常处理
- 在适当的层级处理异常
- 使用全局异常处理器统一处理
- 记录必要的异常日志

### 4. 异常监控
- 启用异常监控和统计
- 设置合理的告警阈值
- 定期分析异常趋势

### 5. 异常测试
- 编写异常场景的单元测试
- 测试异常处理逻辑
- 验证异常响应格式

## 注意事项

1. **异常性能**: 异常处理会有一定的性能开销，避免在正常流程中频繁抛出异常
2. **异常安全**: 确保异常不会导致数据不一致或资源泄漏
3. **异常日志**: 合理控制异常日志的详细程度，避免日志过多
4. **异常监控**: 定期检查异常监控数据，及时发现和处理问题
5. **异常文档**: 及时更新异常处理文档，方便团队使用
