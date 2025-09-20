# IAM Common 结果封装模块

## 概述

本模块提供了完整的结果封装机制，包括统一响应结果、分页结果、结果构建器、结果工具类等，为整个IAM平台提供统一的结果返回格式。

## 核心组件

### 1. Result<T> - 统一响应结果

基础的结果封装类，支持泛型数据。

```java
// 基本用法
Result<String> result = Result.success("操作成功");
Result<User> userResult = Result.success(user);
Result<Void> voidResult = Result.failed("操作失败");

// 带消息的成功结果
Result<String> result = Result.success("用户创建成功", "user123");

// 带错误码的失败结果
Result<String> result = Result.failed(400, "参数错误");

// 带结果枚举的失败结果
Result<String> result = Result.failed(ResultCode.USER_NOT_FOUND);
```

### 2. ResultBuilder<T> - 结果构建器

提供链式调用的方式构建Result对象。

```java
// 基本用法
Result<String> result = ResultBuilder.<String>builder()
    .success()
    .message("操作成功")
    .data("result")
    .build();

// 条件构建
Result<String> result = ResultBuilder.<String>builder()
    .condition(user != null, "用户存在", "用户不存在", user.getName())
    .build();

// 安全执行
Result<String> result = ResultBuilder.<String>builder()
    .safeExecute(() -> {
        // 可能抛出异常的代码
        return "result";
    }, "操作失败")
    .build();

// 链式调用
Result<String> result = ResultBuilder.<String>builder()
    .ifNotNull(data, "数据存在", "数据不存在")
    .ifNotBlank(str, "字符串不为空", "字符串为空")
    .ifNotEmpty(list, "列表不为空", "列表为空")
    .build();
```

### 4. BasicResultCode - 基础结果状态码枚举

## 使用规范

### BasicResultCode vs ResultCode

- **BasicResultCode**: 基础HTTP状态码，用于通用的成功/失败状态
  - 包含：SUCCESS(200), FAILED(500), PARAM_ERROR(400), UNAUTHORIZED(401) 等
  - 适用于：通用响应、基础状态判断

- **ResultCode**: 业务错误码，用于具体的业务场景
  - 包含：用户相关(2001-2006)、认证相关(3001-3005)、权限相关(4001-4003) 等
  - 适用于：具体业务异常、详细错误信息

### 使用建议

```java
// 基础状态使用 BasicResultCode
Result<String> result = Result.success(); // 使用 BasicResultCode.SUCCESS
Result<String> result = Result.failed("参数错误"); // 使用 BasicResultCode.FAILED

// 业务异常使用 ResultCode
throw new BusinessException(ResultCode.USER_NOT_FOUND);
return Result.failed(ResultCode.TOKEN_EXPIRED);
```

定义常用的HTTP状态码和业务状态码。

```java
// 基本用法
Result<String> result = Result.failed(BasicResultCode.UNAUTHORIZED);

// 判断状态码类型
if (BasicResultCode.SUCCESS.isSuccess()) {
    // 成功状态码
}

if (BasicResultCode.PARAM_ERROR.isClientError()) {
    // 客户端错误
}

if (BasicResultCode.INTERNAL_SERVER_ERROR.isServerError()) {
    // 服务器错误
}

// 根据错误码获取枚举
BasicResultCode basicResultCode = BasicResultCode.getByCode(404);
```

### 5. ResultUtils - 结果工具类

提供Result相关的工具方法。

```java
// 安全执行
Result<String> result = ResultUtils.safeExecute(() -> {
    return "result";
});

// 条件判断
Result<String> result = ResultUtils.conditional(user != null, user.getName(), "用户不存在");

// 数据转换
Result<String> result = ResultUtils.map(userResult, User::getName);

// 批量处理
List<Supplier<String>> operations = Arrays.asList(
    () -> "result1",
    () -> "result2",
    () -> "result3"
);
Result<List<String>> result = ResultUtils.batchProcess(operations);

// 合并结果
Result<List<String>> result = ResultUtils.merge(result1, result2, result3);

// 获取第一个成功的结果
Result<String> result = ResultUtils.firstSuccess(result1, result2, result3);
```

## 使用示例

### 1. Controller层使用

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
            return Result.failed(e.getCode(), e.getMessage());
        }
    }
    
    @GetMapping
    public Result<PageResult<User>> getUsers(@RequestParam(defaultValue = "1") Long current,
                                           @RequestParam(defaultValue = "10") Long size) {
        try {
            PageResult<User> pageResult = userService.getUsers(current, size);
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.failed("查询失败: " + e.getMessage());
        }
    }
    
    @PostMapping
    public Result<Void> createUser(@RequestBody @Valid User user) {
        try {
            userService.createUser(user);
            return Result.success("用户创建成功");
        } catch (BusinessException e) {
            return Result.failed(e);
        }
    }
}
```

### 2. Service层使用

```java
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public Result<User> getUserById(Long id) {
        // 使用ResultBuilder
        return ResultBuilder.<User>builder()
            .ifNotNull(id, "用户ID不能为空", "用户ID不能为空")
            .safeExecute(() -> {
                User user = userRepository.findById(id);
                if (user == null) {
                    throw new BusinessException(ResultCode.USER_NOT_FOUND);
                }
                return user;
            }, "查询用户失败")
            .build();
    }
    
    public Result<PageResult<User>> getUsers(Long current, Long size) {
        // 使用ResultUtils
        return ResultUtils.safeExecute(() -> {
            List<User> users = userRepository.findAll(current, size);
            Long total = userRepository.count();
            return PageResult.of(users, total, current, size);
        }, "查询用户列表失败");
    }
    
    public Result<Void> createUser(User user) {
        // 使用条件判断
        return ResultUtils.conditional(
            user != null && user.getName() != null,
            "用户创建成功",
            "用户信息不完整"
        );
    }
}
```

### 3. 异常处理中使用

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.failed(e.getCode(), e.getMessage());
    }
    
    @ExceptionHandler(ValidationException.class)
    public Result<Void> handleValidationException(ValidationException e) {
        return Result.failed(ResultCode.PARAM_ERROR.getCode(), e.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        return Result.failed(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "系统内部错误");
    }
}
```

## 高级用法

### 1. 链式操作

```java
// 使用Result的链式操作
Result<String> result = Result.success("原始数据")
    .ifSuccess(data -> log.info("处理数据: {}", data))
    .map(data -> data.toUpperCase())
    .ifFailed(failedResult -> log.error("操作失败: {}", failedResult.getMessage()));

// 使用ResultBuilder的链式操作
Result<String> result = ResultBuilder.<String>builder()
    .success()
    .message("操作成功")
    .data("原始数据")
    .ifSuccess(data -> log.info("处理数据: {}", data))
    .build();
```

### 2. 数据转换

```java
// 使用map转换数据
Result<String> result = Result.success(user)
    .map(User::getName)
    .map(String::toUpperCase);

// 使用flatMap扁平化转换
Result<String> result = Result.success(user)
    .flatMap(u -> Result.success(u.getName()));

// 使用ResultUtils转换
Result<String> result = ResultUtils.map(userResult, User::getName);
```

### 3. 条件处理

```java
// 使用ResultBuilder的条件处理
Result<String> result = ResultBuilder.<String>builder()
    .ifNotNull(user, "用户存在", "用户不存在")
    .ifNotBlank(user.getName(), "用户名不为空", "用户名为空")
    .ifNotEmpty(user.getRoles(), "用户有角色", "用户无角色")
    .build();

// 使用ResultUtils的条件处理
Result<String> result = ResultUtils.conditional(
    user != null && user.isEnabled(),
    "用户可用",
    "用户不可用"
);
```

### 4. 批量处理

```java
// 批量处理多个操作
List<Supplier<String>> operations = Arrays.asList(
    () -> service1.getData(),
    () -> service2.getData(),
    () -> service3.getData()
);

Result<List<String>> result = ResultUtils.batchProcess(operations);

// 合并多个结果
Result<List<String>> result = ResultUtils.merge(
    service1.getResult(),
    service2.getResult(),
    service3.getResult()
);

// 获取第一个成功的结果
Result<String> result = ResultUtils.firstSuccess(
    service1.getResult(),
    service2.getResult(),
    service3.getResult()
);
```

## 最佳实践

### 1. 统一使用Result封装

```java
// 推荐：所有接口都返回Result
@GetMapping("/users")
public Result<PageResult<User>> getUsers() {
    // ...
}

// 不推荐：直接返回数据
@GetMapping("/users")
public PageResult<User> getUsers() {
    // ...
}
```

### 2. 合理使用错误码

```java
// 推荐：使用预定义的错误码
return Result.failed(ResultCode.USER_NOT_FOUND);

// 不推荐：使用硬编码的错误码
return Result.failed(404, "用户不存在");
```

### 3. 提供有意义的错误消息

```java
// 推荐：提供具体的错误信息
return Result.failed("用户ID不能为空");

// 不推荐：提供模糊的错误信息
return Result.failed("参数错误");
```

### 4. 使用链式操作提高代码可读性

```java
// 推荐：使用链式操作
return Result.success(user)
    .ifSuccess(u -> log.info("用户查询成功: {}", u.getName()))
    .map(User::getName);

// 不推荐：嵌套的if-else
if (user != null) {
    log.info("用户查询成功: {}", user.getName());
    return Result.success(user.getName());
} else {
    return Result.failed("用户不存在");
}
```

### 5. 合理使用分页结果

```java
// 推荐：使用PageResult封装分页数据
public Result<PageResult<User>> getUsers(Long current, Long size) {
    PageResult<User> pageResult = PageResult.of(users, total, current, size);
    return Result.success(pageResult);
}

// 不推荐：直接返回List
public Result<List<User>> getUsers(Long current, Long size) {
    return Result.success(users);
}
```

## 注意事项

1. **性能考虑**: Result对象会创建额外的对象，在性能敏感的场景下需要权衡
2. **内存使用**: 大量使用Result对象会增加内存使用，注意及时释放
3. **序列化**: Result对象实现了Serializable，可以安全地进行序列化
4. **线程安全**: Result对象是不可变的，线程安全
5. **空值处理**: 使用hasData()方法检查数据是否存在，避免空指针异常

## 扩展

如果需要自定义结果类型，可以：

1. 继承Result类
2. 实现IResult接口
3. 使用ResultBuilder构建自定义结果
4. 扩展ResultUtils添加自定义工具方法

通过合理使用这些结果封装类，可以大大提高代码的可读性、可维护性和一致性。
