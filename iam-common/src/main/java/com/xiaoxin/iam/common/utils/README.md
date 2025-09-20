# 工具类使用文档

本文档介绍了 iam-common 模块中提供的各种工具类的使用方法。

## 目录

- [DateUtils - 日期时间工具类](#dateutils---日期时间工具类)
- [StringUtils - 字符串工具类](#stringutils---字符串工具类)
- [JsonUtils - JSON工具类](#jsonutils---json工具类)
- [CollectionUtils - 集合工具类](#collectionutils---集合工具类)
- [ValidationUtils - 验证工具类](#validationutils---验证工具类)
- [FileUtils - 文件工具类](#fileutils---文件工具类)
- [CryptoUtils - 加密工具类](#cryptoutils---加密工具类)

## DateUtils - 日期时间工具类

### 基本用法

```java
// 获取当前时间
LocalDate today = DateUtils.now();
LocalTime now = DateUtils.nowTime();
LocalDateTime nowDateTime = DateUtils.nowDateTime();

// 格式化日期
String dateStr = DateUtils.format(today, "yyyy-MM-dd");
String dateTimeStr = DateUtils.format(nowDateTime, "yyyy-MM-dd HH:mm:ss");

// 解析日期
LocalDate date = DateUtils.parseDate("2024-01-01", "yyyy-MM-dd");
LocalDateTime dateTime = DateUtils.parseDateTime("2024-01-01 12:00:00", "yyyy-MM-dd HH:mm:ss");
```

### 日期计算

```java
// 日期加减
LocalDate tomorrow = DateUtils.plusDays(today, 1);
LocalDate lastMonth = DateUtils.minusMonths(today, 1);

// 获取月份的第一天和最后一天
LocalDate firstDay = DateUtils.firstDayOfMonth(today);
LocalDate lastDay = DateUtils.lastDayOfMonth(today);

// 计算日期差
long daysBetween = DateUtils.daysBetween(startDate, endDate);
long hoursBetween = DateUtils.hoursBetween(startDateTime, endDateTime);
```

### 日期判断

```java
// 判断日期关系
boolean isToday = DateUtils.isToday(date);
boolean isYesterday = DateUtils.isYesterday(date);
boolean isThisWeek = DateUtils.isThisWeek(date);
boolean isThisMonth = DateUtils.isThisMonth(date);

// 判断闰年
boolean isLeap = DateUtils.isLeapYear(2024);
```

## StringUtils - 字符串工具类

### 空值判断

```java
// 判断字符串是否为空
boolean isEmpty = StringUtils.isEmpty(str);
boolean isBlank = StringUtils.isBlank(str);
boolean isNotEmpty = StringUtils.isNotEmpty(str);
boolean isNotBlank = StringUtils.isNotBlank(str);
```

### 字符串处理

```java
// 去除空白
String trimmed = StringUtils.trim(str);
String leftTrimmed = StringUtils.trimLeft(str);
String rightTrimmed = StringUtils.trimRight(str);
String allTrimmed = StringUtils.trimAll(str);

// 字符串替换
String replaced = StringUtils.replace(str, "old", "new");
String replacedIgnoreCase = StringUtils.replaceIgnoreCase(str, "OLD", "new");
```

### 字符串转换

```java
// 大小写转换
String lower = StringUtils.toLowerCase(str);
String upper = StringUtils.toUpperCase(str);
String capitalized = StringUtils.capitalize(str);

// 驼峰转换
String underline = StringUtils.camelToUnderline("camelCase");
String camel = StringUtils.underlineToCamel("snake_case");
String hyphen = StringUtils.camelToHyphen("camelCase");
```

### 字符串验证

```java
// 数字验证
boolean isNumeric = StringUtils.isNumeric(str);
boolean isInteger = StringUtils.isInteger(str);
boolean isLong = StringUtils.isLong(str);

// 格式验证
boolean isEmail = StringUtils.isEmail(str);
boolean isPhone = StringUtils.isPhone(str);
boolean isIdCard = StringUtils.isIdCard(str);
boolean isUrl = StringUtils.isUrl(str);
```

### 字符串分割和连接

```java
// 分割字符串
String[] parts = StringUtils.split(str, ",");
List<String> list = StringUtils.splitToList(str, ",");

// 连接字符串
String joined = StringUtils.join(parts, ",");
String joinedList = StringUtils.join(list, ",");
```

## JsonUtils - JSON工具类

### 对象转JSON

```java
// 对象转JSON字符串
String json = JsonUtils.toJson(obj);
String prettyJson = JsonUtils.toPrettyJson(obj);
byte[] jsonBytes = JsonUtils.toJsonBytes(obj);
```

### JSON转对象

```java
// JSON字符串转对象
User user = JsonUtils.fromJson(json, User.class);
List<User> users = JsonUtils.toList(json, User.class);

// 使用TypeReference
List<User> users = JsonUtils.fromJson(json, new TypeReference<List<User>>() {});
```

### JSON转Map

```java
// JSON转Map
Map<String, Object> map = JsonUtils.toMap(json);
Map<String, Object> mapFromObj = JsonUtils.toMap(obj);

// Map转对象
User user = JsonUtils.mapToObject(map, User.class);
```

### JSON验证和操作

```java
// 验证JSON格式
boolean isValid = JsonUtils.isValidJson(json);
boolean isArray = JsonUtils.isJsonArray(json);
boolean isObject = JsonUtils.isJsonObject(json);

// 获取JSON中的值
String value = JsonUtils.getString(json, "key");
Integer intValue = JsonUtils.getInt(json, "key");
Boolean boolValue = JsonUtils.getBoolean(json, "key");
```

## CollectionUtils - 集合工具类

### 空值判断

```java
// 判断集合是否为空
boolean isEmpty = CollectionUtils.isEmpty(list);
boolean isNotEmpty = CollectionUtils.isNotEmpty(list);
boolean isMapEmpty = CollectionUtils.isEmpty(map);
```

### 集合创建

```java
// 创建集合
List<String> list = CollectionUtils.listOf("a", "b", "c");
Set<String> set = CollectionUtils.setOf("a", "b", "c");
Map<String, String> map = CollectionUtils.mapOf("key1", "value1", "key2", "value2");
```

### 集合转换

```java
// 数组转集合
List<String> list = CollectionUtils.arrayToList(array);
Set<String> set = CollectionUtils.arrayToSet(array);

// 集合间转换
Set<String> set = CollectionUtils.listToSet(list);
List<String> list = CollectionUtils.setToList(set);
```

### 集合过滤和映射

```java
// 过滤集合
List<String> filtered = CollectionUtils.filter(list, s -> s.length() > 3);
Set<String> filteredSet = CollectionUtils.filter(set, s -> s.startsWith("a"));

// 映射集合
List<String> mapped = CollectionUtils.map(list, String::toUpperCase);
Set<String> mappedSet = CollectionUtils.map(set, String::toLowerCase);
```

### 集合查找

```java
// 查找元素
String found = CollectionUtils.findFirst(list, s -> s.startsWith("a"));
List<String> allFound = CollectionUtils.findAll(list, s -> s.length() > 3);

// 查找Map中的值
String value = CollectionUtils.findValue(map, "key");
String defaultValue = CollectionUtils.findValue(map, "key", "default");
```

### 集合合并和交集

```java
// 合并集合
List<String> merged = CollectionUtils.merge(list1, list2);
Set<String> mergedSet = CollectionUtils.merge(set1, set2);
Map<String, String> mergedMap = CollectionUtils.merge(map1, map2);

// 计算交集
List<String> intersection = CollectionUtils.intersection(list1, list2);
Set<String> intersectionSet = CollectionUtils.intersection(set1, set2);
```

## ValidationUtils - 验证工具类

### 基础验证

```java
// 空值验证
boolean isEmpty = ValidationUtils.isEmpty(str);
boolean isBlank = ValidationUtils.isBlank(str);
boolean isNull = ValidationUtils.isNull(obj);
```

### 长度验证

```java
// 长度验证
boolean isLengthValid = ValidationUtils.isLengthBetween(str, 3, 20);
boolean isLengthEqual = ValidationUtils.isLengthEqual(str, 10);
boolean isLengthGreater = ValidationUtils.isLengthGreaterThan(str, 5);
```

### 数字验证

```java
// 数字验证
boolean isNumeric = ValidationUtils.isNumeric(str);
boolean isInteger = ValidationUtils.isInteger(str);
boolean isLong = ValidationUtils.isLong(str);
boolean isInRange = ValidationUtils.isNumberBetween(str, 0, 100);
```

### 格式验证

```java
// 邮箱验证
boolean isEmail = ValidationUtils.isEmail(email);
boolean isEmailStrict = ValidationUtils.isEmailStrict(email);

// 手机号验证
boolean isPhone = ValidationUtils.isPhone(phone);
boolean isPhoneInternational = ValidationUtils.isPhoneInternational(phone);

// 身份证验证
boolean isIdCard = ValidationUtils.isIdCard(idCard);
boolean isIdCardWithChecksum = ValidationUtils.isIdCardWithChecksum(idCard);

// URL验证
boolean isUrl = ValidationUtils.isUrl(url);
boolean isHttpUrl = ValidationUtils.isHttpUrl(url);
```

### 密码验证

```java
// 密码格式验证
boolean isPassword = ValidationUtils.isPassword(password);
boolean isStrongPassword = ValidationUtils.isStrongPassword(password);

// 密码强度检查
PasswordStrength strength = ValidationUtils.getPasswordStrength(password);
```

## FileUtils - 文件工具类

### 文件存在性检查

```java
// 检查文件是否存在
boolean exists = FileUtils.exists(filePath);
boolean isFile = FileUtils.isFile(filePath);
boolean isDirectory = FileUtils.isDirectory(dirPath);
```

### 文件创建

```java
// 创建文件
boolean created = FileUtils.createFile(filePath);

// 创建目录
boolean dirCreated = FileUtils.createDirectory(dirPath);

// 创建临时文件
File tempFile = FileUtils.createTempFile("prefix", ".txt");
```

### 文件删除

```java
// 删除文件
boolean deleted = FileUtils.deleteFile(filePath);

// 删除目录（递归删除）
boolean dirDeleted = FileUtils.deleteDirectory(dirPath);
```

### 文件复制和移动

```java
// 复制文件
boolean copied = FileUtils.copyFile(sourcePath, targetPath);

// 复制目录
boolean dirCopied = FileUtils.copyDirectory(sourcePath, targetPath);

// 移动文件
boolean moved = FileUtils.moveFile(sourcePath, targetPath);
```

### 文件读写

```java
// 读取文件
String content = FileUtils.readFile(filePath);
byte[] bytes = FileUtils.readFileBytes(filePath);
List<String> lines = FileUtils.readLines(filePath);

// 写入文件
boolean written = FileUtils.writeFile(filePath, content);
boolean bytesWritten = FileUtils.writeFile(filePath, bytes);
boolean linesWritten = FileUtils.writeFile(filePath, lines);

// 追加内容
boolean appended = FileUtils.appendFile(filePath, content);
```

### 文件信息

```java
// 获取文件信息
long size = FileUtils.getFileSize(filePath);
String extension = FileUtils.getFileExtension(filePath);
String fileName = FileUtils.getFileName(filePath);
String directory = FileUtils.getFileDirectory(filePath);
LocalDateTime lastModified = FileUtils.getLastModifiedTime(filePath);

// 格式化文件大小
String formattedSize = FileUtils.formatFileSize(size);

// 获取MIME类型
String mimeType = FileUtils.getMimeType(filePath);
boolean isImage = FileUtils.isImageFile(filePath);
boolean isText = FileUtils.isTextFile(filePath);
```

### 文件搜索

```java
// 搜索文件
List<String> files = FileUtils.searchFiles(dirPath, ".*\\.txt$");
List<String> txtFiles = FileUtils.searchFilesByExtension(dirPath, "txt");
List<String> nameFiles = FileUtils.searchFilesByName(dirPath, "test");
```

## CryptoUtils - 加密工具类

### 哈希加密

```java
// MD5加密
String md5 = CryptoUtils.md5(input);
String md5WithSalt = CryptoUtils.md5WithSalt(input, salt);
String md5Multiple = CryptoUtils.md5Multiple(input, 3);

// SHA加密
String sha1 = CryptoUtils.sha1(input);
String sha256 = CryptoUtils.sha256(input);
String sha512 = CryptoUtils.sha512(input);
String shaWithSalt = CryptoUtils.shaWithSalt(input, salt, CryptoUtils.SHA256);
```

### 对称加密

```java
// AES加密
String aesKey = CryptoUtils.generateAESKey();
String encrypted = CryptoUtils.aesEncrypt(input, aesKey);
String decrypted = CryptoUtils.aesDecrypt(encrypted, aesKey);

// DES加密
String desKey = CryptoUtils.generateDESKey();
String encrypted = CryptoUtils.desEncrypt(input, desKey);
String decrypted = CryptoUtils.desDecrypt(encrypted, desKey);

// 3DES加密
String des3Key = CryptoUtils.generate3DESKey();
String encrypted = CryptoUtils.des3Encrypt(input, des3Key);
String decrypted = CryptoUtils.des3Decrypt(encrypted, des3Key);
```

### Base64编码

```java
// Base64编码
String encoded = CryptoUtils.base64Encode(input);
String encodedBytes = CryptoUtils.base64Encode(bytes);

// Base64解码
String decoded = CryptoUtils.base64Decode(encoded);
byte[] decodedBytes = CryptoUtils.base64DecodeToBytes(encoded);
```

### 密码生成

```java
// 生成随机密码
String password = CryptoUtils.generatePassword(12);
String alphanumericPassword = CryptoUtils.generateAlphanumericPassword(12);
String numericPassword = CryptoUtils.generateNumericPassword(6);

// 生成盐值
String salt = CryptoUtils.generateSalt(16);
String alphanumericSalt = CryptoUtils.generateAlphanumericSalt(16);
```

### 密码验证

```java
// 验证密码
boolean isValid = CryptoUtils.verifyPassword(input, hashedPassword, salt, CryptoUtils.SHA256);
boolean isMD5Valid = CryptoUtils.verifyMD5Password(input, hashedPassword, salt);
boolean isSHA256Valid = CryptoUtils.verifySHA256Password(input, hashedPassword, salt);
```

### 工具方法

```java
// 生成UUID
String uuid = CryptoUtils.generateUUID();
String uuidWithoutHyphens = CryptoUtils.generateUUIDWithoutHyphens();

// 生成随机字符串
String randomString = CryptoUtils.generateRandomString(10);
String randomNumber = CryptoUtils.generateRandomNumberString(6);
String randomLetter = CryptoUtils.generateRandomLetterString(8);

// 字节数组转换
String hex = CryptoUtils.bytesToHex(bytes);
byte[] bytes = CryptoUtils.hexToBytes(hex);
```

## 注意事项

1. **空值处理**：所有工具类都提供了完善的空值处理，避免空指针异常。

2. **异常处理**：工具类内部会捕获异常并转换为运行时异常，使用时需要注意异常处理。

3. **性能考虑**：对于大量数据的处理，建议使用流式处理或分批处理。

4. **线程安全**：大部分工具类都是线程安全的，但某些方法可能不是线程安全的，使用时需要注意。

5. **编码格式**：字符串处理默认使用UTF-8编码，如需其他编码请使用相应的方法。

6. **文件操作**：文件操作可能涉及IO异常，建议在业务代码中进行适当的异常处理。

7. **加密安全**：加密工具类提供了多种加密算法，使用时请根据安全需求选择合适的算法。

8. **内存管理**：处理大文件时要注意内存使用，建议使用流式处理。

## 扩展建议

1. **添加更多验证规则**：可以根据业务需求添加更多的验证规则。

2. **支持更多文件格式**：可以添加对更多文件格式的支持。

3. **添加缓存机制**：对于频繁使用的操作可以添加缓存机制。

4. **添加异步处理**：对于耗时的操作可以添加异步处理支持。

5. **添加监控和日志**：可以添加操作监控和日志记录功能。
