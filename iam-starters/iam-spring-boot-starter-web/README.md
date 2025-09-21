# IAM Spring Boot Starter Web

IAM平台Web相关Spring Boot Starter，提供Web开发所需的核心功能。

## 功能特性

### 🎯 核心特性

- ✅ **CORS支持**: 灵活的跨域资源共享配置
- ✅ **API文档**: 集成Knife4j和Swagger，自动生成API文档
- ✅ **HTTP客户端**: 预配置OkHttp和Apache HttpClient
- ✅ **JSON处理**: 统一的Jackson配置，支持时间格式化
- ✅ **请求日志**: 自动记录HTTP请求日志
- ✅ **Feign客户端**: 自动启用和配置Feign客户端
- ✅ **负载均衡**: 集成Spring Cloud LoadBalancer

### 📦 包含依赖

- IAM Spring Boot Starter (基础功能)
- Spring Boot Web
- Spring Boot WebFlux (可选)
- Spring Cloud OpenFeign
- Spring Cloud LoadBalancer
- Knife4j OpenAPI3
- Jackson时间处理
- OkHttp
- Apache HttpClient

## 使用方式

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.xiaoxin</groupId>
    <artifactId>iam-spring-boot-starter-web</artifactId>
</dependency>
```

### 2. 配置属性

```yaml
iam:
  web:
    # CORS配置
    cors:
      enabled: true
      allowed-origin-patterns:
        - "*"
      allowed-methods:
        - GET
        - POST
        - PUT
        - DELETE
        - OPTIONS
      allowed-headers:
        - "*"
      exposed-headers:
        - Content-Disposition
      allow-credentials: true
      max-age: 3600s
    
    # API文档配置
    api-doc:
      enabled: true
      title: "IAM平台API文档"
      description: "IAM平台RESTful API接口文档"
      version: "1.0.0"
      contact:
        name: "IAM Team"
        email: "iam@xiaoxin.com"
        url: "https://www.xiaoxin.com"
      license:
        name: "Apache 2.0"
        url: "https://www.apache.org/licenses/LICENSE-2.0"
    
    # HTTP客户端配置
    http-client:
      connect-timeout: 10s
      read-timeout: 30s
      write-timeout: 30s
      max-connections: 200
      max-connections-per-host: 50
      keep-alive-duration: 5m
    
    # 请求日志配置
    request-log:
      enabled: true
      include-headers: false
      include-request-payload: false
      include-response-payload: false
      max-payload-length: 1000
```

## 功能详解

### 1. CORS支持

自动配置跨域资源共享，支持灵活的配置：

```java
@RestController
@CrossOrigin // 可选，已全局配置
public class UserController {
    
    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }
}
```

### 2. API文档

自动集成Knife4j和Swagger，访问地址：
- Knife4j UI: `http://localhost:8080/doc.html`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

使用示例：
```java
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {
    
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户详细信息")
    @Parameter(name = "id", description = "用户ID", required = true)
    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        return Result.success(userService.getUser(id));
    }
}
```

### 3. HTTP客户端

提供预配置的OkHttp客户端：

```java
@Service
public class ExternalApiService {
    
    @Autowired
    private OkHttpClient okHttpClient;
    
    public String callExternalApi(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        try (Response response = okHttpClient.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("调用外部API失败", e);
        }
    }
}
```

### 4. JSON处理

统一的Jackson配置：

```java
@RestController
public class DataController {
    
    @PostMapping("/data")
    public Result<DataResponse> processData(@RequestBody DataRequest request) {
        // 自动处理日期时间格式：yyyy-MM-dd HH:mm:ss
        // 忽略未知属性
        // 处理时区转换
        return Result.success(dataService.process(request));
    }
}
```

### 5. 请求日志

自动记录HTTP请求日志：

```
2024-01-01 12:00:00 INFO  - HTTP请求 - POST /api/v1/users - 状态: 200, 耗时: 156ms, 客户端: 192.168.1.100
2024-01-01 12:00:01 WARN  - HTTP请求 - GET /api/v1/reports - 状态: 200, 耗时: 1200ms, 客户端: 192.168.1.101
```

### 6. Feign客户端

自动扫描和配置Feign客户端：

```java
@FeignClient(name = "iam-auth-service", path = "/api/v1/auth")
public interface AuthServiceClient {
    
    @GetMapping("/validate")
    Result<Boolean> validateToken(@RequestParam String token);
    
    @PostMapping("/refresh")
    Result<TokenResponse> refreshToken(@RequestBody TokenRequest request);
}
```

使用Feign客户端：
```java
@Service
public class AuthService {
    
    @Autowired
    private AuthServiceClient authServiceClient;
    
    public boolean validateToken(String token) {
        Result<Boolean> result = authServiceClient.validateToken(token);
        return result.isSuccess() && result.getData();
    }
}
```

## 配置属性

### CORS配置 (`iam.web.cors.*`)

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `enabled` | Boolean | true | 是否启用CORS |
| `allowed-origin-patterns` | List<String> | ["*"] | 允许的源模式 |
| `allowed-methods` | List<String> | [GET,POST,PUT,DELETE,OPTIONS] | 允许的HTTP方法 |
| `allowed-headers` | List<String> | ["*"] | 允许的请求头 |
| `exposed-headers` | List<String> | [Content-Disposition] | 暴露的响应头 |
| `allow-credentials` | Boolean | true | 是否允许凭证 |
| `max-age` | Duration | 1h | 预检请求缓存时间 |

### API文档配置 (`iam.web.api-doc.*`)

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `enabled` | Boolean | true | 是否启用API文档 |
| `title` | String | IAM平台API文档 | 文档标题 |
| `description` | String | IAM平台RESTful API接口文档 | 文档描述 |
| `version` | String | 1.0.0 | API版本 |
| `contact.name` | String | IAM Team | 联系人姓名 |
| `contact.email` | String | iam@xiaoxin.com | 联系人邮箱 |
| `contact.url` | String | https://www.xiaoxin.com | 联系人网址 |
| `license.name` | String | Apache 2.0 | 许可证名称 |
| `license.url` | String | https://www.apache.org/licenses/LICENSE-2.0 | 许可证网址 |

### HTTP客户端配置 (`iam.web.http-client.*`)

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `connect-timeout` | Duration | 10s | 连接超时时间 |
| `read-timeout` | Duration | 30s | 读取超时时间 |
| `write-timeout` | Duration | 30s | 写入超时时间 |
| `max-connections` | Integer | 200 | 最大连接数 |
| `max-connections-per-host` | Integer | 50 | 每个主机最大连接数 |
| `keep-alive-duration` | Duration | 5m | 连接保活时间 |

### 请求日志配置 (`iam.web.request-log.*`)

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `enabled` | Boolean | true | 是否启用请求日志 |
| `include-headers` | Boolean | false | 是否记录请求头 |
| `include-request-payload` | Boolean | false | 是否记录请求体 |
| `include-response-payload` | Boolean | false | 是否记录响应体 |
| `max-payload-length` | Integer | 1000 | 最大载荷长度 |

## 自动配置

本Starter会自动配置以下Bean：

1. `CorsConfigurationSource` - CORS配置源
2. `CorsFilter` - CORS过滤器
3. `OpenAPI` - OpenAPI文档配置
4. `OkHttpClient` - HTTP客户端
5. `ObjectMapper` - JSON序列化配置
6. `RequestLoggingFilter` - 请求日志过滤器

## 扩展自定义

### 1. 自定义CORS配置

```java
@Configuration
public class CustomCorsConfig {
    
    @Bean
    @Primary
    public CorsConfigurationSource customCorsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 自定义CORS配置
        return new UrlBasedCorsConfigurationSource();
    }
}
```

### 2. 自定义API文档

```java
@Configuration
public class CustomApiDocConfig {
    
    @Bean
    @Primary
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("自定义API文档"));
    }
}
```

### 3. 自定义HTTP客户端

```java
@Configuration
public class CustomHttpClientConfig {
    
    @Bean
    @Primary
    public OkHttpClient customOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();
    }
}
```

## 最佳实践

### 1. API设计

- 使用RESTful风格的URL设计
- 统一使用Result包装响应结果
- 合理使用HTTP状态码
- 提供完整的API文档

### 2. 安全考虑

- 谨慎配置CORS允许的源
- 敏感信息不要记录在请求日志中
- 使用HTTPS传输敏感数据
- 实施适当的认证和授权

### 3. 性能优化

- 合理设置HTTP客户端超时时间
- 使用连接池管理HTTP连接
- 避免在请求日志中记录大量数据
- 监控慢请求并进行优化

## 注意事项

1. **CORS配置**: 生产环境应限制允许的源，避免使用通配符
2. **请求日志**: 记录请求体和响应体会影响性能，谨慎启用
3. **API文档**: 生产环境建议禁用API文档或限制访问
4. **HTTP客户端**: 根据实际需求调整超时时间和连接池大小

## 版本历史

### 1.0.0-SNAPSHOT
- 初始版本
- 提供Web开发核心功能
- CORS、API文档、HTTP客户端支持
- JSON处理和请求日志功能
