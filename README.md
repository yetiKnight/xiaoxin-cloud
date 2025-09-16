# IAM平台 - 企业级统一身份与权限管理平台

## 项目简介

IAM平台是一个基于Spring Cloud Alibaba构建的企业级统一身份与权限管理平台，为企业所有业务系统提供统一身份认证、集中权限管理、组织架构管理、合规审计的能力。

## 技术栈

### 后端技术栈
- **基础框架**: Spring Boot 3.2.9 + Spring Security 6.1.5
- **微服务框架**: Spring Cloud Alibaba 2023.0.3.3
- **服务注册发现**: Nacos 2.4.2
- **API网关**: Spring Cloud Gateway 4.1.0
- **熔断限流**: Sentinel 1.8.8
- **分布式事务**: Seata 2.1.0
- **消息队列**: RocketMQ 5.3.1
- **数据库**: MySQL 8.0 + MyBatis Plus 3.5.4.1
- **缓存**: Redis 6.0
- **搜索引擎**: Elasticsearch 7.17.9
- **文档数据库**: MongoDB 5.0

### 前端技术栈
- **前端框架**: Vue 3.0 + TypeScript
- **UI组件库**: Element Plus
- **状态管理**: Pinia
- **路由管理**: Vue Router 4.0
- **构建工具**: Vite 4.0+

### 运维技术栈
- **容器化**: Docker + Docker Compose
- **监控**: Prometheus + Grafana
- **日志**: ELK Stack (Elasticsearch + Logstash + Kibana)
- **链路追踪**: Jaeger

## 项目结构

```
iam-platform/
├── iam-common/                    # 公共模块
│   ├── src/main/java/com/xiaoxin/iam/common/
│   │   ├── result/                # 统一响应结果
│   │   ├── exception/             # 全局异常处理
│   │   ├── constant/              # 常量定义
│   │   ├── util/                  # 工具类
│   │   ├── config/                # 公共配置
│   │   ├── entity/                # 公共实体
│   │   ├── dto/                   # 数据传输对象
│   │   └── enums/                 # 枚举类
│   └── pom.xml
├── iam-gateway/                   # API网关服务
│   ├── src/main/java/com/xiaoxin/iam/gateway/
│   │   ├── config/                # 网关配置
│   │   └── filter/                # 网关过滤器
│   └── pom.xml
├── iam-auth-service/              # 认证服务
│   ├── src/main/java/com/xiaoxin/iam/auth/
│   │   ├── controller/            # 控制器
│   │   ├── service/               # 服务层
│   │   ├── entity/                # 实体类
│   │   ├── dto/                   # 数据传输对象
│   │   ├── mapper/                # 数据访问层
│   │   └── config/                # 配置类
│   └── pom.xml
├── services/                      # 微服务模块
│   ├── iam-auth-service/                  # 认证服务 (8081)
│   ├── iam-core-service/                  # 核心业务服务 (8082)
│   ├── iam-audit-service/                 # 审计服务 (8083)
│   └── iam-system-service/                # 系统服务 (8084)
├── apps/                          # 应用层服务
│   └── iam-frontend/                      # 前端服务 (8088)
├── config-init/                   # 配置初始化
│   ├── config/                    # Nacos配置
│   ├── sql/                       # 数据库脚本
│   └── scripts/                   # 启动脚本
├── docker-compose/                # Docker编排
├── docs/                          # 文档
└── pom.xml                        # 父级POM
```

## 微服务架构

### 服务拆分
- **iam-gateway**: API网关，统一入口，路由转发，限流熔断 (8080)
- **iam-auth-service**: 认证服务，OAuth2.1认证，JWT Token管理 (8081)
- **iam-core-service**: 核心业务服务，用户/权限/组织管理 (8082)
- **iam-audit-service**: 审计服务，操作日志，合规审计 (8083)
- **iam-system-service**: 系统服务，配置/通知/监控 (8084)
- **iam-frontend**: 前端服务，管理后台界面 (8088)

### 数据存储
- **iam_auth**: 认证相关数据（OAuth2客户端、授权信息）
- **iam_core**: 核心业务数据（用户、权限、组织）
- **iam_audit**: 审计日志数据（操作日志、登录日志）
- **iam_system**: 系统配置数据（配置、字典、通知）

## 快速开始

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Docker & Docker Compose

### 1. 克隆项目
```bash
git clone <repository-url>
cd iam-platform
```

### 2. 启动基础服务
```bash
# 启动MySQL、Redis、Nacos等基础服务
cd docker-compose
docker-compose up -d mysql redis nacos rocketmq-nameserver rocketmq-broker sentinel-dashboard
```

### 3. 初始化数据库
```bash
# 执行数据库初始化脚本
mysql -h localhost -u root -p < config-init/sql/init.sql
```

### 4. 配置Nacos
访问 http://localhost:8848/nacos，用户名/密码：nacos/nacos
导入 `config-init/config/` 目录下的配置文件

### 5. 编译项目
```bash
mvn clean compile -DskipTests
```

### 6. 启动服务
```bash
# 使用启动脚本
chmod +x config-init/scripts/*.sh
./config-init/scripts/start.sh

# 或手动启动各个服务
mvn spring-boot:run -pl iam-gateway
mvn spring-boot:run -pl services/iam-auth-service
mvn spring-boot:run -pl services/iam-core-service
mvn spring-boot:run -pl services/iam-audit-service
mvn spring-boot:run -pl services/iam-system-service
mvn spring-boot:run -pl apps/iam-frontend
```

### 7. 访问系统
- **管理后台**: http://localhost:8080
- **API文档**: http://localhost:8080/doc.html
- **Nacos控制台**: http://localhost:8848/nacos
- **Sentinel控制台**: http://localhost:8080
- **RocketMQ控制台**: http://localhost:8180

## 默认账号
- **管理员账号**: admin
- **默认密码**: 123456

## 功能特性

### 用户管理
- 用户信息管理（CRUD、批量导入导出）
- 用户认证（多种登录方式、单点登录）
- 用户生命周期管理
- 多租户支持

### 权限管理
- RBAC权限模型
- 角色权限管理
- 菜单权限控制
- 数据权限隔离
- 字段权限控制

### 组织架构
- 部门层级管理
- 岗位体系管理
- 组织架构可视化
- 部门权限隔离

### 系统管理
- 菜单管理
- 字典管理
- 参数配置
- 系统监控

### 审计功能
- 操作日志记录
- 登录日志记录
- 审计报表生成
- 合规性检查

## 开发指南

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用Lombok简化代码
- 统一异常处理
- 统一响应格式

### 接口规范
- RESTful API设计
- 统一响应格式：`{code, message, data, timestamp}`
- 接口版本管理：`/api/v1/`
- OpenAPI 3.0文档

### 安全规范
- JWT Token认证
- 接口权限验证
- 数据脱敏
- 操作审计

## 部署指南

### Docker部署
```bash
# 构建镜像
docker build -t iam-platform .

# 启动服务
docker-compose up -d
```

### Kubernetes部署
```bash
# 应用Kubernetes配置
kubectl apply -f k8s/
```

## 监控运维

### 监控指标
- 服务健康状态
- 接口调用量
- 响应时间
- 错误率
- 资源使用率

### 日志管理
- 集中日志收集
- 日志检索分析
- 异常告警
- 日志归档

### 性能优化
- 缓存策略优化
- 数据库连接池调优
- JVM参数调优
- 接口性能优化

## 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 许可证

本项目采用 Apache 2.0 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 联系方式

- 项目维护者：小新
- 邮箱：admin@xiaoxin.com
- 项目地址：https://github.com/xiaoxin/iam-platform

## 更新日志

### v1.0.0 (2024-01-01)
- 初始版本发布
- 基础功能实现
- 微服务架构搭建
- 统一认证授权
- 权限管理功能
- 组织架构管理
- 审计日志功能
