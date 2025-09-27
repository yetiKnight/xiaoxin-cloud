# IAM Starters 模块集合

IAM平台Spring Boot Starter模块集合，提供IAM平台开发所需的各种功能组件。

## 模块概览

### 🏗️ 架构设计

IAM Starters采用分层设计，每个starter专注于特定功能领域：

```
iam-starters/
├── iam-commons/                     # 公共组件库
├── iam-spring-boot-starter/         # 基础功能Starter
├── iam-spring-boot-starter-web/     # Web功能Starter
├── iam-spring-boot-starter-security/# 安全功能Starter
├── iam-spring-boot-starter-data/    # 数据访问Starter
├── iam-spring-boot-starter-cache/   # 缓存功能Starter
├── iam-spring-boot-starter-mq/      # 消息队列Starter
├── iam-spring-boot-starter-audit/   # 审计功能Starter
├── iam-spring-boot-starter-notification/ # 通知功能Starter
└── iam-spring-boot-starter-gateway/ # 网关功能Starter
```

### 📦 模块功能

| 模块 | 功能描述 | 主要特性 |
|------|----------|----------|
| **iam-commons** | 公共组件库 | 基础工具类、常量定义 |
| **iam-spring-boot-starter** | 基础功能 | 统一异常处理、响应封装、操作日志、性能监控 |
| **iam-spring-boot-starter-web** | Web功能 | CORS、API文档、HTTP客户端、请求日志 |
| **iam-spring-boot-starter-security** | 安全功能 | JWT、OAuth2、密码策略、多因子认证 |
| **iam-spring-boot-starter-data** | 数据访问 | MyBatis Plus、多数据源、分页、审计 |
| **iam-spring-boot-starter-cache** | 缓存功能 | Redis、本地缓存、缓存注解、分布式锁 |
| **iam-spring-boot-starter-mq** | 消息队列 | RocketMQ、事件发布、消息处理 |
| **iam-spring-boot-starter-audit** | 审计功能 | 操作审计、登录日志、数据变更追踪 |
| **iam-spring-boot-starter-notification** | 通知功能 | 邮件、短信、站内信、推送通知 |
| **iam-spring-boot-starter-gateway** | 网关功能 | 路由、限流、熔断、负载均衡 |

## 快速开始

### 1. 基础使用

最小化配置，只需引入基础starter：

```xml
<dependency>
    <groupId>com.xiaoxin</groupId>
    <artifactId>iam-spring-boot-starter</artifactId>
</dependency>
```

### 2. Web应用

开发Web应用，引入Web相关starter：

```xml
<dependencies>
    <!-- 基础功能 -->
    <dependency>
        <groupId>com.xiaoxin</groupId>
        <artifactId>iam-spring-boot-starter</artifactId>
    </dependency>
    
    <!-- Web功能 -->
    <dependency>
        <groupId>com.xiaoxin</groupId>
        <artifactId>iam-spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- 安全功能 -->
    <dependency>
        <groupId>com.xiaoxin</groupId>
        <artifactId>iam-spring-boot-starter-security</artifactId>
    </dependency>
</dependencies>
```

### 3. 完整IAM应用

开发完整的IAM应用，引入所有相关starter：

```xml
<dependencies>
    <!-- 基础功能 -->
    <dependency>
        <groupId>com.xiaoxin</groupId>
        <artifactId>iam-spring-boot-starter</artifactId>
    </dependency>
    
    <!-- Web功能 -->
    <dependency>
        <groupId>com.xiaoxin</groupId>
        <artifactId>iam-spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- 安全功能 -->
    <dependency>
        <groupId>com.xiaoxin</groupId>
        <artifactId>iam-spring-boot-starter-security</artifactId>
    </dependency>
    
    <!-- 数据访问 -->
    <dependency>
        <groupId>com.xiaoxin</groupId>
        <artifactId>iam-spring-boot-starter-data</artifactId>
    </dependency>
    
    <!-- 缓存功能 -->
    <dependency>
        <groupId>com.xiaoxin</groupId>
        <artifactId>iam-spring-boot-starter-cache</artifactId>
    </dependency>
    
    <!-- 审计功能 -->
    <dependency>
        <groupId>com.xiaoxin</groupId>
        <artifactId>iam-spring-boot-starter-audit</artifactId>
    </dependency>
</dependencies>
```

## 配置示例

### 基础配置

```yaml
iam:
  # 基础配置
  enabled: true
  application-name: iam-demo
  version: 1.0.0
  enable-unified-response: true
  enable-global-exception-handler: true
  
  # 核心功能配置
  core:
    enable-operation-log: true
    enable-performance-monitor: true
    thread-pool:
      core-size: 8
      max-size: 20
```

### Web配置

```yaml
iam:
  web:
    # CORS配置
    cors:
      enabled: true
      allowed-origin-patterns: ["*"]
    
    # API文档配置
    api-doc:
      enabled: true
      title: "IAM Demo API"
    
    # 请求日志配置
    request-log:
      enabled: true
      include-headers: false
```

### 安全配置

```yaml
iam:
  security:
    # JWT配置
    jwt:
      enabled: true
      secret: "your-jwt-secret"
      access-token-expiration: 2h
    
    # 密码策略
    password:
      min-length: 8
      require-uppercase: true
      require-digits: true
    
    # 验证码配置
    captcha:
      enabled: true
      type: IMAGE
      length: 4
```

### 数据访问配置

```yaml
iam:
  data:
    # 多数据源配置
    datasource:
      primary: master
      sources:
        master:
          url: jdbc:mysql://localhost:3306/iam_master
          username: root
          password: password
        slave:
          url: jdbc:mysql://localhost:3306/iam_slave
          username: readonly
          password: password
    
    # MyBatis Plus配置
    mybatis-plus:
      enable-sql-log: true
      enable-page-helper: true
```

## 使用指南

### 1. 开发RESTful API

```java
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    @Operation(summary = "获取用户信息")
    @OperationLog(module = "用户管理", operation = "查看用户")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
    
    @PostMapping
    @Operation(summary = "创建用户")
    @OperationLog(module = "用户管理", operation = "创建用户")
    @PreAuthorize("hasAuthority('user.create')")
    public User createUser(@Valid @RequestBody UserCreateDTO dto) {
        return userService.createUser(dto);
    }
}
```

### 2. 配置安全认证

```java
@Service
public class AuthService {
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public LoginResponse login(LoginRequest request) {
        // 验证用户凭据
        User user = validateCredentials(request.getUsername(), request.getPassword());
        
        // 生成JWT令牌
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(UserVO.from(user))
                .build();
    }
}
```

### 3. 使用数据访问

```java
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Cacheable(value = "users", key = "#id")
    @OperationLog(module = "用户管理", operation = "查询用户")
    public User getUser(Long id) {
        return userMapper.selectById(id);
    }
    
    @Transactional
    @CacheEvict(value = "users", key = "#user.id")
    @OperationLog(module = "用户管理", operation = "更新用户")
    public void updateUser(User user) {
        userMapper.updateById(user);
    }
}
```

### 4. 发送通知

```java
@Service
public class NotificationService {
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private SmsService smsService;
    
    @Async
    public void sendWelcomeNotification(User user) {
        // 发送欢迎邮件
        emailService.sendEmail(
            user.getEmail(),
            "欢迎注册",
            "welcome-template",
            Map.of("username", user.getUsername())
        );
        
        // 发送短信通知
        smsService.sendSms(
            user.getPhone(),
            "您的账户已成功注册，欢迎使用IAM平台！"
        );
    }
}
```

## 自动配置

所有starter都提供了自动配置功能，开箱即用：

### 自动配置的Bean

1. **基础功能**
   - `GlobalExceptionHandler` - 全局异常处理器
   - `IamResponseBodyAdvice` - 统一响应体处理器
   - `OperationLogAspect` - 操作日志切面
   - `PerformanceMonitorAspect` - 性能监控切面

2. **Web功能**
   - `CorsFilter` - CORS过滤器
   - `OpenAPI` - API文档配置
   - `OkHttpClient` - HTTP客户端
   - `RequestLoggingFilter` - 请求日志过滤器

3. **安全功能**
   - `PasswordEncoder` - 密码编码器
   - `JwtTokenProvider` - JWT令牌提供者
   - `SecurityFilterChain` - 安全过滤器链
   - `CaptchaService` - 验证码服务

4. **数据访问**
   - `DataSource` - 多数据源配置
   - `SqlSessionFactory` - MyBatis会话工厂
   - `PlatformTransactionManager` - 事务管理器
   - `PageHelper` - 分页助手

## 扩展开发

### 1. 自定义Starter

如果需要创建新的功能starter：

```java
@Configuration
@ConditionalOnClass(YourFeatureClass.class)
@EnableConfigurationProperties(YourProperties.class)
public class YourFeatureAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public YourFeatureService yourFeatureService() {
        return new YourFeatureService();
    }
}
```

在`META-INF/spring.factories`中注册：

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.xiaoxin.iam.starter.yourfeature.YourFeatureAutoConfiguration
```

### 2. 自定义配置

```java
@Configuration
public class CustomConfig {
    
    @Bean
    @Primary
    public CustomBean customBean() {
        return new CustomBean();
    }
}
```

### 3. 条件注解

使用Spring Boot的条件注解来控制Bean的创建：

```java
@Bean
@ConditionalOnProperty(prefix = "iam.feature", name = "enabled", havingValue = "true")
@ConditionalOnMissingBean
public FeatureService featureService() {
    return new FeatureService();
}
```

## 最佳实践

### 1. 依赖管理

- 使用BOM统一管理依赖版本
- 避免传递依赖冲突
- 合理使用`optional`和`scope`

### 2. 配置管理

- 提供合理的默认配置
- 支持外部化配置
- 使用配置属性类进行类型安全的配置

### 3. 条件装配

- 使用条件注解控制Bean的创建
- 提供`@ConditionalOnMissingBean`让用户可以覆盖默认配置
- 合理使用`@ConditionalOnProperty`支持功能开关

### 4. 文档化

- 为每个starter提供详细的README
- 提供完整的配置属性说明
- 包含使用示例和最佳实践

## 性能优化

### 1. 启动优化

- 使用`@ConditionalOnClass`避免不必要的类加载
- 延迟初始化非关键Bean
- 优化自动配置顺序

### 2. 运行时优化

- 合理配置线程池大小
- 使用缓存减少重复计算
- 优化数据库查询和连接池配置

### 3. 内存优化

- 避免内存泄漏
- 合理设置缓存大小和过期时间
- 使用对象池重用昂贵对象

## 故障排查

### 1. 自动配置问题

```bash
# 查看自动配置报告
java -jar app.jar --debug

# 查看条件求值报告
java -jar app.jar --trace
```

### 2. 依赖冲突

```bash
# 查看依赖树
mvn dependency:tree

# 分析依赖
mvn dependency:analyze
```

### 3. 配置问题

```bash
# 查看有效配置
curl http://localhost:8080/actuator/configprops

# 查看环境变量
curl http://localhost:8080/actuator/env
```

## 版本历史

### 1.0.0-SNAPSHOT
- 初始版本发布
- 提供9个核心starter模块
- 支持完整的IAM平台功能
- 提供丰富的配置选项和扩展点

## 贡献指南

1. **代码规范**: 遵循阿里巴巴Java开发手册
2. **测试覆盖**: 单元测试覆盖率不低于80%
3. **文档更新**: 及时更新相关文档
4. **向后兼容**: 保持API的向后兼容性

## 许可证

本项目采用Apache 2.0许可证，详情请参阅[LICENSE](../LICENSE)文件。
