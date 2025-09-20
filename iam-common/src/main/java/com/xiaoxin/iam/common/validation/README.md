# 数据验证注解使用文档

本文档介绍了 iam-common 模块中提供的各种数据验证注解的使用方法。

## 目录

- [概述](#概述)
- [基础使用](#基础使用)
- [验证注解详解](#验证注解详解)
  - [@Phone - 手机号验证](#phone---手机号验证)
  - [@Email - 邮箱验证](#email---邮箱验证)
  - [@IdCard - 身份证验证](#idcard---身份证验证)
  - [@Username - 用户名验证](#username---用户名验证)
  - [@Password - 密码强度验证](#password---密码强度验证)
  - [@EnumValue - 枚举值验证](#enumvalue---枚举值验证)
  - [@StringLength - 字符串长度验证](#stringlength---字符串长度验证)
  - [@DateRange - 日期范围验证](#daterange---日期范围验证)
- [验证分组](#验证分组)
- [自定义错误消息](#自定义错误消息)
- [在控制器中使用](#在控制器中使用)
- [在服务层使用](#在服务层使用)
- [最佳实践](#最佳实践)

## 概述

数据验证注解基于 Bean Validation (JSR-303/JSR-380) 标准实现，提供了丰富的数据验证功能。这些注解可以用于：

- **实体类字段验证**：验证实体对象的字段值
- **方法参数验证**：验证方法输入参数
- **API请求验证**：验证HTTP请求中的数据
- **配置参数验证**：验证配置文件中的参数

### 特性

- **易于使用**：基于注解，使用简单
- **功能丰富**：涵盖常见的业务验证场景
- **可配置**：支持灵活的参数配置
- **国际化**：支持多语言错误消息
- **高性能**：验证逻辑优化，性能良好

## 基础使用

### 1. 添加依赖

确保项目中包含 Bean Validation 相关依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### 2. 启用验证

在 Spring Boot 中，验证功能默认启用。如需手动配置：

```java
@Configuration
@EnableMethodValidation
public class ValidationConfig {
    // 配置代码
}
```

### 3. 基本使用示例

```java
public class UserRegisterRequest {
    
    @Phone(message = "手机号格式不正确")
    private String phone;
    
    @Email(strict = true, message = "邮箱格式不正确")
    private String email;
    
    @Username(type = Username.UsernameType.CHINESE, min = 2, max = 20)
    private String username;
    
    @Password(strength = Password.PasswordStrength.STRONG)
    private String password;
    
    // getter/setter...
}
```

## 验证注解详解

### @Phone - 手机号验证

验证手机号格式，支持中国大陆手机号和国际手机号。

#### 基本用法

```java
public class UserInfo {
    @Phone
    private String phone;
    
    @Phone(type = Phone.PhoneType.CN, nullable = false)
    private String mobile;
    
    @Phone(type = Phone.PhoneType.INTERNATIONAL)
    private String internationalPhone;
}
```

#### 参数说明

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `type` | `PhoneType` | `CN` | 手机号类型：CN(中国大陆)、INTERNATIONAL(国际)、ANY(任意) |
| `nullable` | `boolean` | `true` | 是否允许为空 |
| `message` | `String` | `"手机号格式不正确"` | 验证失败消息 |

#### 使用示例

```java
public class ContactForm {
    // 中国大陆手机号，不能为空
    @Phone(type = Phone.PhoneType.CN, nullable = false, 
           message = "请输入正确的手机号")
    private String phone;
    
    // 国际手机号，可以为空
    @Phone(type = Phone.PhoneType.INTERNATIONAL)
    private String emergencyContact;
    
    // 任意格式手机号
    @Phone(type = Phone.PhoneType.ANY)
    private String backupPhone;
}
```

### @Email - 邮箱验证

验证邮箱格式，支持标准验证和严格验证，可配置域名白名单和黑名单。

#### 基本用法

```java
public class UserInfo {
    @Email
    private String email;
    
    @Email(strict = true, nullable = false)
    private String workEmail;
    
    @Email(allowedDomains = {"company.com", "partner.com"})
    private String businessEmail;
}
```

#### 参数说明

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `strict` | `boolean` | `false` | 是否使用严格模式验证 |
| `nullable` | `boolean` | `true` | 是否允许为空 |
| `allowedDomains` | `String[]` | `{}` | 允许的邮箱域名（空数组表示不限制） |
| `blockedDomains` | `String[]` | `{}` | 禁止的邮箱域名 |
| `message` | `String` | `"邮箱格式不正确"` | 验证失败消息 |

#### 使用示例

```java
public class EmailSettings {
    // 严格模式验证，不能为空
    @Email(strict = true, nullable = false,
           message = "请输入有效的邮箱地址")
    private String primaryEmail;
    
    // 只允许公司邮箱
    @Email(allowedDomains = {"company.com", "subsidiary.com"},
           message = "请使用公司邮箱")
    private String workEmail;
    
    // 禁止使用临时邮箱
    @Email(blockedDomains = {"10minutemail.com", "temp-mail.org"},
           message = "不能使用临时邮箱")
    private String registrationEmail;
}
```

### @IdCard - 身份证验证

验证身份证号格式，支持15位和18位身份证，可选择是否验证校验码。

#### 基本用法

```java
public class PersonInfo {
    @IdCard
    private String idCard;
    
    @IdCard(type = IdCard.IdCardType.ID_18, checkCode = true)
    private String citizenId;
    
    @IdCard(type = IdCard.IdCardType.BOTH, nullable = false)
    private String identityNumber;
}
```

#### 参数说明

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `type` | `IdCardType` | `BOTH` | 身份证类型：ID_15(15位)、ID_18(18位)、BOTH(兼容) |
| `checkCode` | `boolean` | `true` | 是否验证校验码（仅对18位有效） |
| `nullable` | `boolean` | `true` | 是否允许为空 |
| `message` | `String` | `"身份证号格式不正确"` | 验证失败消息 |

#### 使用示例

```java
public class IdentityForm {
    // 18位身份证，验证校验码
    @IdCard(type = IdCard.IdCardType.ID_18, 
            checkCode = true, 
            nullable = false,
            message = "请输入正确的18位身份证号")
    private String idCardNumber;
    
    // 兼容15位和18位，不验证校验码
    @IdCard(type = IdCard.IdCardType.BOTH, 
            checkCode = false)
    private String legacyIdCard;
}
```

### @Username - 用户名验证

验证用户名格式，支持英文和中文用户名，可自定义长度和字符要求。

#### 基本用法

```java
public class UserAccount {
    @Username
    private String username;
    
    @Username(type = Username.UsernameType.CHINESE, min = 2, max = 10)
    private String displayName;
    
    @Username(type = Username.UsernameType.ENGLISH, 
              mustStartWithLetter = true,
              allowUnderscore = false)
    private String loginName;
}
```

#### 参数说明

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `type` | `UsernameType` | `ENGLISH` | 用户名类型：ENGLISH、CHINESE、CUSTOM |
| `min` | `int` | `3` | 最小长度 |
| `max` | `int` | `20` | 最大长度 |
| `allowUnderscore` | `boolean` | `true` | 是否允许下划线 |
| `allowDigit` | `boolean` | `true` | 是否允许数字 |
| `mustStartWithLetter` | `boolean` | `false` | 是否必须以字母开头 |
| `blacklist` | `String[]` | `{}` | 禁止的用户名列表 |
| `nullable` | `boolean` | `true` | 是否允许为空 |
| `message` | `String` | `"用户名格式不正确"` | 验证失败消息 |

#### 使用示例

```java
public class UserProfile {
    // 英文用户名，必须以字母开头，不允许下划线
    @Username(type = Username.UsernameType.ENGLISH,
              min = 4, max = 16,
              mustStartWithLetter = true,
              allowUnderscore = false,
              blacklist = {"admin", "root", "system"},
              message = "用户名必须4-16位字母数字，以字母开头")
    private String loginName;
    
    // 中文昵称
    @Username(type = Username.UsernameType.CHINESE,
              min = 2, max = 8,
              message = "昵称长度为2-8个字符")
    private String nickname;
    
    // 自定义格式，只验证长度和黑名单
    @Username(type = Username.UsernameType.CUSTOM,
              min = 1, max = 50,
              blacklist = {"匿名", "游客"})
    private String customName;
}
```

### @Password - 密码强度验证

验证密码强度，支持多种强度等级和自定义规则。

#### 基本用法

```java
public class PasswordForm {
    @Password
    private String password;
    
    @Password(strength = Password.PasswordStrength.STRONG)
    private String newPassword;
    
    @Password(strength = Password.PasswordStrength.CUSTOM,
              requireUppercase = true,
              requireLowercase = true,
              requireDigit = true,
              requireSpecialChar = true,
              min = 12)
    private String adminPassword;
}
```

#### 参数说明

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `strength` | `PasswordStrength` | `MEDIUM` | 密码强度：WEAK、MEDIUM、STRONG、CUSTOM |
| `min` | `int` | `8` | 最小长度 |
| `max` | `int` | `32` | 最大长度 |
| `requireUppercase` | `boolean` | `false` | 是否必须包含大写字母 |
| `requireLowercase` | `boolean` | `false` | 是否必须包含小写字母 |
| `requireDigit` | `boolean` | `false` | 是否必须包含数字 |
| `requireSpecialChar` | `boolean` | `false` | 是否必须包含特殊字符 |
| `allowedSpecialChars` | `String` | `"!@#$%^&*()_+-=[]{}|;:,.<>?"` | 允许的特殊字符 |
| `forbidConsecutiveSame` | `boolean` | `false` | 是否禁止连续相同字符 |
| `maxConsecutiveSame` | `int` | `3` | 连续相同字符的最大数量 |
| `forbidCommonPasswords` | `boolean` | `true` | 是否禁止常见弱密码 |
| `nullable` | `boolean` | `true` | 是否允许为空 |
| `message` | `String` | `"密码格式不正确"` | 验证失败消息 |

#### 使用示例

```java
public class SecuritySettings {
    // 中等强度密码
    @Password(strength = Password.PasswordStrength.MEDIUM,
              min = 8, max = 20,
              message = "密码至少8位，包含字母和数字")
    private String userPassword;
    
    // 强密码
    @Password(strength = Password.PasswordStrength.STRONG,
              min = 12,
              forbidConsecutiveSame = true,
              maxConsecutiveSame = 2,
              message = "密码至少12位，包含大小写字母、数字和特殊字符")
    private String adminPassword;
    
    // 自定义强度
    @Password(strength = Password.PasswordStrength.CUSTOM,
              min = 16,
              requireUppercase = true,
              requireLowercase = true,
              requireDigit = true,
              requireSpecialChar = true,
              allowedSpecialChars = "!@#$%",
              forbidConsecutiveSame = true,
              forbidCommonPasswords = true,
              message = "密码必须16位以上，包含大小写字母、数字和特殊字符(!@#$%)，不能有连续相同字符")
    private String superSecurePassword;
}
```

### @EnumValue - 枚举值验证

验证字符串值是否为指定枚举类的有效值。

#### 基本用法

```java
public enum UserStatus {
    ACTIVE, INACTIVE, PENDING, SUSPENDED
}

public class UserInfo {
    @EnumValue(enumClass = UserStatus.class)
    private String status;
    
    @EnumValue(enumClass = UserStatus.class, 
               ignoreCase = true,
               allowedValues = {"ACTIVE", "INACTIVE"})
    private String accountStatus;
}
```

#### 参数说明

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `enumClass` | `Class<? extends Enum<?>>` | 必填 | 枚举类 |
| `ignoreCase` | `boolean` | `false` | 是否忽略大小写 |
| `allowedValues` | `String[]` | `{}` | 允许的枚举值（如果指定，则只允许这些值） |
| `forbiddenValues` | `String[]` | `{}` | 禁止的枚举值 |
| `method` | `String` | `"name"` | 验证方法（默认使用name()方法） |
| `nullable` | `boolean` | `true` | 是否允许为空 |
| `message` | `String` | `"枚举值不正确"` | 验证失败消息 |

#### 使用示例

```java
public enum Gender {
    MALE("男"), FEMALE("女"), OTHER("其他");
    
    private final String description;
    
    Gender(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

public class PersonProfile {
    // 基本枚举验证
    @EnumValue(enumClass = UserStatus.class,
               nullable = false,
               message = "用户状态无效")
    private String status;
    
    // 忽略大小写，只允许部分值
    @EnumValue(enumClass = Gender.class,
               ignoreCase = true,
               allowedValues = {"MALE", "FEMALE"},
               message = "性别只能选择男或女")
    private String gender;
    
    // 使用自定义方法验证
    @EnumValue(enumClass = Gender.class,
               method = "getDescription",
               message = "请选择正确的性别")
    private String genderDescription;
    
    // 禁止某些值
    @EnumValue(enumClass = UserStatus.class,
               forbiddenValues = {"SUSPENDED"},
               message = "不能设置为已暂停状态")
    private String newStatus;
}
```

### @StringLength - 字符串长度验证

验证字符串长度是否在指定范围内，支持字符数和字节数两种计算方式。

#### 基本用法

```java
public class TextContent {
    @StringLength(min = 1, max = 100)
    private String title;
    
    @StringLength(min = 10, max = 1000, trim = true)
    private String content;
    
    @StringLength(max = 255, lengthType = StringLength.LengthType.BYTE)
    private String description;
}
```

#### 参数说明

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `min` | `int` | `0` | 最小长度 |
| `max` | `int` | `Integer.MAX_VALUE` | 最大长度 |
| `lengthType` | `LengthType` | `CHARACTER` | 长度计算方式：CHARACTER(字符数)、BYTE(字节数) |
| `charset` | `String` | `"UTF-8"` | 字符编码（仅在按字节计算时使用） |
| `trim` | `boolean` | `false` | 是否去除前后空白字符再计算长度 |
| `allowEmpty` | `boolean` | `true` | 是否允许空字符串（当nullable=false时有效） |
| `nullable` | `boolean` | `true` | 是否允许为空 |
| `message` | `String` | `"字符串长度必须在{min}到{max}个字符之间"` | 验证失败消息 |

#### 使用示例

```java
public class ArticleForm {
    // 标题长度验证（字符数）
    @StringLength(min = 5, max = 100, 
                  trim = true,
                  nullable = false,
                  message = "标题长度必须在5-100个字符之间")
    private String title;
    
    // 内容长度验证（去除空白字符）
    @StringLength(min = 50, max = 5000,
                  trim = true,
                  message = "内容长度必须在50-5000个字符之间")
    private String content;
    
    // 摘要长度验证（字节数，适合数据库存储）
    @StringLength(max = 255,
                  lengthType = StringLength.LengthType.BYTE,
                  charset = "UTF-8",
                  message = "摘要字节长度不能超过255")
    private String summary;
    
    // 标签验证（不允许空字符串）
    @StringLength(min = 1, max = 50,
                  trim = true,
                  nullable = false,
                  allowEmpty = false,
                  message = "标签不能为空，长度1-50个字符")
    private String tags;
}
```

### @DateRange - 日期范围验证

验证日期是否在指定范围内，支持多种日期类型和相对日期。

#### 基本用法

```java
public class EventForm {
    @DateRange(min = "2024-01-01", max = "2024-12-31")
    private String eventDate;
    
    @DateRange(relative = DateRange.RelativeDate.TODAY, offsetDays = 1)
    private Date startDate;
    
    @DateRange(min = "2024-01-01 00:00:00", 
               pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
```

#### 参数说明

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `min` | `String` | `""` | 最小日期（字符串格式） |
| `max` | `String` | `""` | 最大日期（字符串格式） |
| `pattern` | `String` | `"yyyy-MM-dd"` | 日期格式 |
| `relative` | `RelativeDate` | `NONE` | 相对日期基准 |
| `offsetDays` | `int` | `0` | 相对天数偏移 |
| `inclusive` | `boolean` | `true` | 是否包含边界值 |
| `timezone` | `String` | `"Asia/Shanghai"` | 时区 |
| `nullable` | `boolean` | `true` | 是否允许为空 |
| `message` | `String` | `"日期必须在{start}到{end}之间"` | 验证失败消息 |

#### RelativeDate 枚举

| 值 | 说明 |
|----|------|
| `NONE` | 无相对日期 |
| `TODAY` | 相对于当前日期 |
| `NOW` | 相对于当前时间 |
| `WEEK_START` | 本周开始 |
| `MONTH_START` | 本月开始 |
| `YEAR_START` | 本年开始 |

#### 使用示例

```java
public class ScheduleForm {
    // 固定日期范围
    @DateRange(min = "2024-01-01", max = "2024-12-31",
               pattern = "yyyy-MM-dd",
               message = "日期必须在2024年内")
    private String scheduleDate;
    
    // 相对于今天，只能选择未来7天内
    @DateRange(relative = DateRange.RelativeDate.TODAY,
               offsetDays = 7,
               inclusive = false,
               message = "只能选择未来7天内的日期")
    private LocalDate futureDate;
    
    // 相对于本月开始，到月末
    @DateRange(relative = DateRange.RelativeDate.MONTH_START,
               max = "2024-12-31",
               pattern = "yyyy-MM-dd")
    private Date monthlyEvent;
    
    // 时间戳验证（最近30天）
    @DateRange(relative = DateRange.RelativeDate.TODAY,
               offsetDays = -30,
               max = "",
               pattern = "yyyy-MM-dd HH:mm:ss",
               timezone = "UTC",
               message = "时间必须在最近30天内")
    private Long timestamp;
    
    // 生日验证（过去100年到今天）
    @DateRange(min = "1924-01-01",
               relative = DateRange.RelativeDate.TODAY,
               offsetDays = 0,
               message = "请输入有效的出生日期")
    private String birthday;
}
```

## 验证分组

Bean Validation 支持验证分组，可以在不同场景下应用不同的验证规则。

### 定义验证分组

```java
public interface CreateGroup {}
public interface UpdateGroup {}
public interface DeleteGroup {}
```

### 使用验证分组

```java
public class UserForm {
    @NotNull(groups = CreateGroup.class, message = "创建时用户名不能为空")
    @Username(groups = {CreateGroup.class, UpdateGroup.class})
    private String username;
    
    @NotNull(groups = CreateGroup.class, message = "创建时密码不能为空")
    @Password(strength = Password.PasswordStrength.STRONG, 
              groups = CreateGroup.class)
    private String password;
    
    @Email(groups = {CreateGroup.class, UpdateGroup.class})
    private String email;
}
```

### 在控制器中指定分组

```java
@RestController
public class UserController {
    
    @PostMapping("/users")
    public Result<User> createUser(@Validated(CreateGroup.class) @RequestBody UserForm form) {
        // 创建用户逻辑
        return Result.success();
    }
    
    @PutMapping("/users/{id}")
    public Result<User> updateUser(@Validated(UpdateGroup.class) @RequestBody UserForm form) {
        // 更新用户逻辑
        return Result.success();
    }
}
```

## 自定义错误消息

### 使用消息占位符

验证注解支持消息占位符，可以在错误消息中引用注解参数：

```java
public class ParameterForm {
    @StringLength(min = 5, max = 20, 
                  message = "字符串长度必须在{min}到{max}个字符之间")
    private String name;
    
    @DateRange(min = "2024-01-01", max = "2024-12-31",
               message = "日期必须在{min}到{max}之间")
    private String date;
}
```

### 国际化消息

在 `messages.properties` 文件中定义错误消息：

```properties
# messages.properties
validation.phone.invalid=手机号格式不正确
validation.email.invalid=邮箱格式不正确
validation.password.weak=密码强度太弱

# messages_en.properties
validation.phone.invalid=Invalid phone number format
validation.email.invalid=Invalid email format
validation.password.weak=Password is too weak
```

使用国际化消息：

```java
public class UserForm {
    @Phone(message = "{validation.phone.invalid}")
    private String phone;
    
    @Email(message = "{validation.email.invalid}")
    private String email;
    
    @Password(message = "{validation.password.weak}")
    private String password;
}
```

## 在控制器中使用

### 基本使用

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    // 验证请求体
    @PostMapping
    public Result<User> createUser(@Valid @RequestBody UserCreateRequest request) {
        // 处理逻辑
        return Result.success();
    }
    
    // 验证路径参数
    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable @Min(1) Long id) {
        // 处理逻辑
        return Result.success();
    }
    
    // 验证查询参数
    @GetMapping
    public Result<List<User>> listUsers(
            @RequestParam @StringLength(min = 1, max = 50) String keyword,
            @RequestParam @Min(1) @Max(100) Integer pageSize) {
        // 处理逻辑
        return Result.success();
    }
}
```

### 验证分组使用

```java
@RestController
public class UserController {
    
    @PostMapping("/register")
    public Result<?> register(@Validated(CreateGroup.class) @RequestBody UserForm form) {
        // 注册逻辑
        return Result.success();
    }
    
    @PutMapping("/profile")
    public Result<?> updateProfile(@Validated(UpdateGroup.class) @RequestBody UserForm form) {
        // 更新逻辑
        return Result.success();
    }
}
```

### 处理验证异常

```java
@RestControllerAdvice
public class ValidationExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        
        return Result.error("参数验证失败", errors);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        
        return Result.error("参数验证失败", errors);
    }
}
```

## 在服务层使用

### 方法参数验证

```java
@Service
@Validated
public class UserService {
    
    public User createUser(@Valid UserCreateRequest request) {
        // 业务逻辑
        return new User();
    }
    
    public User updateUser(@Min(1) Long userId, 
                          @Valid UserUpdateRequest request) {
        // 业务逻辑
        return new User();
    }
    
    public void deleteUser(@NotNull @Min(1) Long userId) {
        // 业务逻辑
    }
}
```

### 手动验证

```java
@Service
public class ValidationService {
    
    private final Validator validator;
    
    public ValidationService(Validator validator) {
        this.validator = validator;
    }
    
    public <T> void validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            throw new ValidationException(message);
        }
    }
    
    public void validateProperty(Object object, String propertyName) {
        Set<ConstraintViolation<Object>> violations = 
                validator.validateProperty(object, propertyName);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations.iterator().next().getMessage());
        }
    }
}
```

## 最佳实践

### 1. 合理选择验证位置

- **控制器层**：验证HTTP请求参数，防止无效数据进入系统
- **服务层**：验证业务逻辑参数，确保业务规则正确
- **实体层**：验证数据完整性，保证数据持久化时的正确性

### 2. 使用验证分组

```java
public class UserForm {
    // 只在创建时验证
    @NotNull(groups = CreateGroup.class)
    private String username;
    
    // 创建和更新时都验证
    @Email(groups = {CreateGroup.class, UpdateGroup.class})
    private String email;
    
    // 只在更新时验证
    @NotNull(groups = UpdateGroup.class)
    private Long id;
}
```

### 3. 组合使用多个注解

```java
public class ComplexForm {
    @NotBlank(message = "用户名不能为空")
    @Username(type = Username.UsernameType.ENGLISH, 
              min = 4, max = 20,
              message = "用户名格式不正确")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Password(strength = Password.PasswordStrength.STRONG,
              message = "密码强度不够")
    private String password;
}
```

### 4. 自定义验证消息

```java
public class MessageForm {
    @Phone(type = Phone.PhoneType.CN, 
           nullable = false,
           message = "请输入正确的中国大陆手机号码")
    private String phone;
    
    @StringLength(min = 5, max = 100,
                  message = "标题长度必须在{min}-{max}个字符之间")
    private String title;
}
```

### 5. 性能优化

- **缓存验证器**：避免重复创建验证器实例
- **合理使用正则表达式**：预编译正则表达式模式
- **避免过度验证**：在合适的层次进行验证，避免重复验证

```java
// 预编译正则表达式
public class OptimizedValidator {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    
    public boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
}
```

### 6. 错误处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        return Result.error("参数验证失败", errors);
    }
}
```

### 7. 测试验证逻辑

```java
@Test
public void testValidation() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    
    UserForm form = new UserForm();
    form.setPhone("invalid-phone");
    
    Set<ConstraintViolation<UserForm>> violations = validator.validate(form);
    assertFalse(violations.isEmpty());
    
    assertTrue(violations.stream()
        .anyMatch(v -> v.getMessage().contains("手机号")));
}
```

## 常见问题

### 1. 验证不生效

确保：
- 添加了 `@Valid` 或 `@Validated` 注解
- 启用了方法验证（`@EnableMethodValidation`）
- 类被Spring管理（添加了`@Component`等注解）

### 2. 自定义消息不显示

检查：
- 消息格式是否正确（使用 `{}` 占位符）
- 国际化文件是否正确配置
- 类路径中是否包含消息文件

### 3. 验证分组不起作用

确认：
- 使用了 `@Validated` 而不是 `@Valid`
- 正确指定了验证分组
- 注解中包含了对应的分组

### 4. 性能问题

优化建议：
- 使用缓存验证器
- 预编译正则表达式
- 避免在循环中进行验证
- 合理使用验证分组

这些验证注解为IAM平台提供了强大而灵活的数据验证能力，能够有效保证数据的完整性和业务规则的正确性。通过合理使用这些注解，可以大大减少手动验证代码，提高开发效率和代码质量。
