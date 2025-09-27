# IAM平台依赖管理指南

## 概述

IAM平台采用集中化的依赖管理策略，通过`iam-dependencies`模块统一管理所有依赖版本和插件配置，确保整个项目的依赖一致性和可维护性。

## 核心设计原则

### 1. 单一依赖源
- 所有版本定义集中在`iam-dependencies/pom.xml`中
- 子模块无需指定具体版本号
- 避免版本冲突和重复定义

### 2. 分层依赖管理
```
根pom.xml (iam-platform)
├── 引入 iam-dependencies BOM
└── 子模块继承版本管理
    ├── iam-common
    ├── iam-gateway  
    ├── services/*
    └── apps/*
```

### 3. 版本统一策略
- Spring Boot: 3.2.9
- Spring Cloud: 2023.0.3
- Spring Cloud Alibaba: 2023.0.3.3
- Java: 17
- 所有第三方库版本在dependencies模块中统一维护

## 目录结构

```
iam-dependencies/
├── pom.xml                     # BOM依赖管理文件
└── README.md                   # 详细的依赖说明文档
```

## 使用方式

### 在根pom.xml中引入
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

### 在子模块中使用
```xml
<dependencies>
    <!-- 直接使用，无需指定版本 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
    </dependency>
</dependencies>
```

## 依赖分类

### 1. Spring生态系列
- **Spring Boot**: 核心框架，版本3.2.9
- **Spring Cloud**: 微服务框架，版本2023.0.3  
- **Spring Cloud Alibaba**: 阿里云微服务套件，版本2023.0.3.3
- **Spring Security**: 安全框架，版本6.1.5

### 2. 数据库相关
- **MySQL**: 数据库驱动，版本8.0.33
- **MyBatis-Plus**: ORM框架，版本3.5.4.1
- **HikariCP**: 连接池，Spring Boot默认连接池

### 3. 工具类库
- **Hutool**: Java工具库，版本5.8.22
- **FastJSON2**: JSON处理，版本2.0.43
- **Lombok**: 代码生成，版本1.18.30
- **MapStruct**: 对象映射，版本1.5.5.Final

### 4. 文档和监控
- **Knife4j**: API文档，版本4.3.0
- **Spring Boot Admin**: 应用监控

### 5. 安全组件
- **JJWT**: JWT处理，版本0.11.5
- **Spring Security**: 安全框架

### 6. 中间件客户端
- **Redisson**: Redis客户端，版本3.24.3
- **RocketMQ**: 消息队列，版本2.2.3

### 7. 第三方SDK
- **阿里云短信**: dysmsapi，版本2.0.24
- **微信SDK**: weixin-java-mp，版本4.4.0

## 插件管理

### Maven编译插件
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <source>17</source>
        <target>17</target>
        <encoding>UTF-8</encoding>
        <annotationProcessorPaths>
            <!-- Lombok和MapStruct处理器 -->
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

### 代码生成插件
- **MyBatis-Plus代码生成器**: 自动生成Entity、Mapper、Service
- **MapStruct处理器**: 编译时生成映射代码
- **Lombok处理器**: 编译时生成getter/setter等

## 版本升级策略

### 1. 小版本升级
- 定期关注依赖库的安全更新
- 优先升级bug修复版本
- 在测试环境充分验证

### 2. 大版本升级
- 制定详细的升级计划
- 评估兼容性影响
- 分阶段逐步升级

### 3. 升级流程
1. 在`iam-dependencies`中更新版本
2. 执行全量测试
3. 提交代码review
4. 部署验证

## 最佳实践

### 1. 添加新依赖
```bash
# 1. 在iam-dependencies/pom.xml中添加依赖管理
# 2. 在需要的子模块中引用（不指定版本）
# 3. 更新本文档说明
```

### 2. 排除依赖冲突
```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>example-starter</artifactId>
    <exclusions>
        <exclusion>
            <groupId>conflicting.group</groupId>
            <artifactId>conflicting-artifact</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### 3. 检查依赖树
```bash
# 查看依赖树
mvn dependency:tree

# 分析依赖冲突
mvn dependency:analyze
```

## 常见问题

### Q: 如何添加新的第三方依赖？
A: 在`iam-dependencies/pom.xml`的相应分类下添加依赖管理，然后在需要的子模块中引用。

### Q: 版本冲突怎么处理？
A: 在`iam-dependencies`中明确指定版本，必要时使用exclusions排除冲突依赖。

### Q: 如何确保所有模块使用统一版本？
A: 通过BOM机制，子模块只需声明groupId和artifactId，版本自动继承。

## 维护说明

- **负责人**: IAM平台架构组
- **更新频率**: 每月检查一次依赖更新
- **文档维护**: 每次依赖变更都需要更新本文档
- **版本记录**: 重要版本变更需要在CHANGELOG中记录

---

*最后更新时间: 2024年12月*
*文档版本: v1.0*
