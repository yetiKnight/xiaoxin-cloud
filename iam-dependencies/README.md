# IAM平台依赖管理模块

## 概述

`iam-dependencies` 模块是IAM平台的统一依赖版本管理模块，负责管理整个项目中所有第三方依赖的版本，确保版本一致性和兼容性。

## 目标

- **统一版本管理**：集中管理所有第三方依赖版本
- **版本兼容性**：确保依赖之间的版本兼容
- **简化维护**：减少版本冲突和维护成本
- **标准化**：提供标准的依赖引用方式

## 功能特性

### 🎯 核心特性

- ✅ **完整的技术栈覆盖**：涵盖微服务开发所需的全部依赖
- ✅ **版本兼容性保证**：经过测试的稳定版本组合
- ✅ **分类管理**：按功能分类组织依赖
- ✅ **插件管理**：统一管理Maven构建插件
- ✅ **环境配置**：支持多环境配置

### 📦 依赖分类

#### 1. Spring Framework 生态
- Spring Boot 3.2.9
- Spring Cloud 2023.0.3  
- Spring Cloud Alibaba 2023.0.3.3
- Spring Security 6.2.7

#### 2. 数据库相关
- MySQL 8.0.33
- PostgreSQL 42.7.4
- H2 2.2.224
- MyBatis Plus 3.5.4.1
- Dynamic DataSource 4.2.0

#### 3. 缓存相关
- Redisson 3.24.3
- Jedis 4.4.6
- Lettuce 6.3.2.RELEASE

#### 4. 消息队列
- RocketMQ 5.1.4
- Kafka 3.6.1
- RabbitMQ 3.12.8

#### 5. 搜索引擎
- Elasticsearch 8.11.0
- Spring Data Elasticsearch 5.2.0

#### 6. 监控相关
- Micrometer 1.12.0
- Prometheus 0.16.0
- SkyWalking 9.0.0

#### 7. 工具类库
- Hutool 5.8.22
- Guava 32.1.3-jre
- Apache Commons Lang3 3.14.0
- Apache Commons Collections4 4.4
- Apache Commons IO 2.15.0
- FastJSON2 2.0.43

#### 8. 安全相关
- JWT 0.12.3
- BCrypt 0.4
- Jasypt 3.0.5

#### 9. API文档
- Knife4j 4.4.0
- Swagger 2.2.8
- SpringDoc OpenAPI 2.2.0

#### 10. 第三方SDK
- 阿里云SDK 4.6.4
- 阿里云短信 2.0.24
- 腾讯云SDK 3.1.879
- 微信SDK 4.4.0
- 钉钉SDK 2.0.0
- 七牛云SDK 7.15.1

#### 11. 文件处理
- Apache POI 5.2.5
- EasyExcel 3.3.2
- iText PDF 5.5.13.3

#### 12. 网络请求
- OkHttp 4.12.0
- Apache HttpClient 4.5.14
- Retrofit 2.9.0

#### 13. 任务调度
- XXL-JOB 2.4.1
- Quartz 2.3.2

#### 14. 分布式组件
- Redisson 分布式锁 3.24.3
- Sentinel 限流熔断 1.8.6
- Seata 分布式事务 1.7.1
- Nacos 配置中心 2.3.0

#### 15. 测试框架
- JUnit 5 5.10.1
- Mockito 5.7.0
- TestContainers 1.19.3

## 使用方式

### 1. 在父项目中引入

在根目录的 `pom.xml` 中引入依赖管理：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.xiaoxin</groupId>
            <artifactId>iam-dependencies</artifactId>
            <version>1.0.0-SNAPSHOT</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 2. 在子模块中使用依赖

在子模块的 `pom.xml` 中，无需指定版本号：

```xml
<dependencies>
    <!-- Spring Boot Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- MyBatis Plus -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
    </dependency>
    
    <!-- Hutool 工具类 -->
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
    </dependency>
</dependencies>
```

### 3. 使用IAM平台自有依赖

```xml
<dependencies>
    <!-- IAM 公共模块 -->
    <dependency>
        <groupId>com.xiaoxin</groupId>
        <artifactId>iam-common</artifactId>
    </dependency>
    
    <!-- IAM Web Starter -->
    <dependency>
        <groupId>com.xiaoxin</groupId>
        <artifactId>iam-spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

## Maven插件配置

### 1. 编译插件

支持Java 17和注解处理器（Lombok、MapStruct）：

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
</plugin>
```

### 2. 测试插件

单元测试和集成测试支持：

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
</plugin>

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
</plugin>
```

### 3. 代码质量检查

```xml
<!-- 代码覆盖率 -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
</plugin>

<!-- 静态代码分析 -->
<plugin>
    <groupId>com.github.spotbugs</groupId>
    <artifactId>spotbugs-maven-plugin</artifactId>
</plugin>

<!-- 代码规范检查 -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
</plugin>
```

## Profile配置

### 开发环境
```bash
mvn clean compile -Pdev
```

### 测试环境
```bash
mvn clean test -Ptest
```

### 生产环境
```bash
mvn clean package -Pprod
```

### 发布构建
```bash
mvn clean package -Prelease
```

### 代码质量检查
```bash
mvn clean test -Pquality
```

## 版本升级策略

### 1. 版本兼容性原则

- **主版本升级**：可能包含破坏性变更，需要全面测试
- **次版本升级**：新功能添加，向后兼容
- **补丁版本升级**：bug修复，安全更新

### 2. 升级流程

1. **评估影响**：分析版本变更日志
2. **测试验证**：在测试环境验证兼容性
3. **逐步升级**：分模块逐步升级
4. **回滚准备**：准备回滚方案

### 3. 版本管理规范

```xml
<!-- 版本号格式：主版本.次版本.补丁版本 -->
<spring-boot.version>3.2.9</spring-boot.version>

<!-- 使用变量引用 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>${spring-boot.version}</version>
</dependency>
```

## 依赖冲突解决

### 1. 查看依赖树

```bash
mvn dependency:tree
```

### 2. 排除冲突依赖

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>some-library</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### 3. 强制指定版本

```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.9</version>
</dependency>
```

## 最佳实践

### 1. 版本管理

- ✅ 使用BOM方式管理依赖版本
- ✅ 统一使用变量定义版本号
- ✅ 定期更新依赖版本
- ✅ 关注安全漏洞修复

### 2. 依赖引入

- ✅ 按需引入，避免不必要的依赖
- ✅ 使用官方推荐的starter
- ✅ 避免传递依赖冲突
- ✅ 优化依赖范围（scope）

### 3. 构建优化

- ✅ 使用Maven并行构建
- ✅ 配置合理的JVM参数
- ✅ 利用依赖缓存
- ✅ 排除测试依赖

## 常见问题

### Q1: 如何查看当前使用的依赖版本？

```bash
mvn dependency:list
mvn dependency:tree
```

### Q2: 如何解决依赖冲突？

1. 使用 `mvn dependency:tree` 查看依赖树
2. 识别冲突的依赖
3. 使用 `<exclusions>` 排除冲突依赖
4. 在dependencyManagement中强制指定版本

### Q3: 如何添加新的依赖？

1. 在 `iam-dependencies/pom.xml` 中添加依赖管理
2. 定义版本变量
3. 在需要的模块中引入依赖（无需指定版本）
4. 测试验证兼容性

### Q4: 如何自定义插件配置？

在子模块的pom.xml中重写插件配置：

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <!-- 自定义配置 -->
            </configuration>
        </plugin>
    </plugins>
</build>
```

## 维护指南

### 1. 版本更新流程

1. **调研新版本**：查看release notes和breaking changes
2. **兼容性评估**：评估对现有代码的影响
3. **测试验证**：在测试环境充分测试
4. **文档更新**：更新相关文档和版本说明
5. **发布通知**：通知团队版本变更

### 2. 监控和维护

- 定期检查依赖安全漏洞
- 关注依赖的生命周期状态
- 及时更新过时的依赖
- 清理不再使用的依赖

### 3. 文档维护

- 保持README文档的最新状态
- 记录重要的版本变更
- 提供清晰的使用示例
- 维护问题解决方案

## 贡献指南

### 提交规范

- feat: 新功能
- fix: 修复bug
- docs: 文档更新
- style: 代码格式调整
- refactor: 代码重构
- test: 测试相关
- chore: 构建/工具相关

### Pull Request

1. Fork项目
2. 创建feature分支
3. 提交代码变更
4. 创建Pull Request
5. 等待代码审查

---

## 联系方式

如有问题或建议，请联系：

- **项目负责人**: xiaoxin
- **邮箱**: xiaoxin@example.com
- **项目地址**: https://github.com/xiaoxin/iam-platform

---

*最后更新时间: 2024年12月19日*
