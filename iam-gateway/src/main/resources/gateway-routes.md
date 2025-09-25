# IAM网关路由配置说明

## 路由架构设计

### 1. 路由分层设计
- **第一层：业务域路由** - 按照服务划分的路由
- **第二层：功能模块路由** - 按照功能模块进一步细分
- **第三层：兜底路由** - 通用路由规则

### 2. 路由命名规范
- 服务名称 + 功能模块：`iam-auth-service-login`
- 特殊功能路由：`iam-frontend-assets`、`health-check`

## 详细路由配置

### 认证服务路由 (iam-auth-service)

#### 1. 登录相关路由 (auth-login)
- **路径**: `/api/v1/auth/login`, `/api/v1/auth/logout`, `/api/v1/auth/refresh`
- **限流**: 20 QPS, 突发 40
- **熔断**: auth-login-cb
- **重试**: 3次，指数退避

#### 2. OAuth2路由 (auth-oauth2)
- **路径**: `/api/v1/auth/oauth2/**`, `/api/v1/auth/callback/**`
- **限流**: 10 QPS, 突发 20
- **熔断**: auth-oauth2-cb

#### 3. 通用认证路由 (auth-service)
- **路径**: `/api/v1/auth/**`
- **限流**: 30 QPS, 突发 60
- **熔断**: auth-service-cb

### 核心业务服务路由 (iam-core-service)

#### 1. 用户管理路由 (core-users)
- **路径**: `/api/v1/core/users/**`
- **限流**: 50 QPS, 突发 100
- **熔断**: core-users-cb
- **特殊处理**: 添加服务标识头

#### 2. 角色权限路由 (core-roles)
- **路径**: `/api/v1/core/roles/**`, `/api/v1/core/permissions/**`
- **限流**: 40 QPS, 突发 80
- **熔断**: core-roles-cb

#### 3. 组织架构路由 (core-depts)
- **路径**: `/api/v1/core/depts/**`, `/api/v1/core/posts/**`
- **限流**: 30 QPS, 突发 60
- **熔断**: core-depts-cb

#### 4. 通用核心路由 (core-service)
- **路径**: `/api/v1/core/**`
- **限流**: 60 QPS, 突发 120
- **熔断**: core-service-cb

### 系统服务路由 (iam-system-service)

#### 1. 配置管理路由 (system-config)
- **路径**: `/api/v1/system/config/**`, `/api/v1/system/dict/**`
- **限流**: 20 QPS, 突发 40
- **熔断**: system-config-cb

#### 2. 通知管理路由 (system-notification)
- **路径**: `/api/v1/system/notifications/**`
- **限流**: 30 QPS, 突发 60
- **熔断**: system-notification-cb

#### 3. 通用系统路由 (system-service)
- **路径**: `/api/v1/system/**`
- **限流**: 40 QPS, 突发 80
- **熔断**: system-service-cb

### 审计服务路由 (iam-audit-service)

#### 1. 日志查询路由 (audit-logs)
- **路径**: `/api/v1/audit/logs/**`
- **限流**: 15 QPS, 突发 30
- **熔断**: audit-logs-cb

#### 2. 报表生成路由 (audit-reports)
- **路径**: `/api/v1/audit/reports/**`
- **限流**: 5 QPS, 突发 10 (报表生成资源消耗大)
- **熔断**: audit-reports-cb

#### 3. 通用审计路由 (audit-service)
- **路径**: `/api/v1/audit/**`
- **限流**: 20 QPS, 突发 40
- **熔断**: audit-service-cb

### 前端应用路由 (iam-frontend)

#### 1. 静态资源路由 (frontend-assets)
- **路径**: `/assets/**`, `/static/**`, `/favicon.ico`
- **限流**: 100 QPS, 突发 200
- **路径重写**: 去除前缀

#### 2. 页面路由 (frontend)
- **路径**: `/`, `/login`, `/dashboard`, `/users/**`, `/roles/**`, `/system/**`
- **限流**: 50 QPS, 突发 100

### 特殊路由

#### 1. 健康检查路由 (health-check)
- **路径**: `/actuator/health/**`
- **限流**: 20 QPS, 突发 40
- **支持**: 所有服务的健康检查

#### 2. WebSocket路由 (websocket-notification)
- **路径**: `/ws/notifications/**`
- **协议**: WebSocket over LoadBalancer
- **限流**: 10 QPS, 突发 20

## 路由过滤器链

### 全局过滤器
1. **LoggingGlobalFilter** (优先级: -200)
   - 请求日志记录
   - 响应时间统计
   - 慢请求告警

2. **AuthGlobalFilter** (优先级: -100)
   - 认证验证
   - 白名单检查
   - Token解析

### 路由级过滤器
1. **RequestRateLimiter** - 限流控制
2. **Sentinel** - 限流熔断保护
3. **Retry** - 重试机制
4. **AddRequestHeader** - 添加请求头
5. **SetPath** - 路径重写

## 限流策略

### 限流级别
- **严格限流**: 审计报表生成 (5 QPS)
- **中等限流**: 认证登录 (20 QPS)
- **宽松限流**: 核心业务 (60 QPS)
- **大流量**: 静态资源 (100 QPS)

### 限流维度
- **IP限流**: 防止单IP恶意请求
- **用户限流**: 防止单用户过度使用
- **API限流**: 防止热点API过载

## 熔断策略

### 熔断配置
- **失败率阈值**: 50%
- **慢调用阈值**: 50%
- **慢调用时间**: 2000ms
- **最小调用次数**: 10次
- **滑动窗口**: 100次

### 降级处理
- **认证服务**: 返回认证服务不可用
- **核心服务**: 返回核心服务不可用
- **系统服务**: 返回系统服务不可用
- **审计服务**: 返回审计服务不可用

## 重试策略

### 重试配置
- **最大重试次数**: 3次
- **初始间隔**: 1000ms
- **最大间隔**: 10000ms
- **倍数**: 2.0
- **适用方法**: GET, POST

### 重试场景
- **网络超时**: 自动重试
- **服务暂时不可用**: 自动重试
- **5xx服务器错误**: 自动重试

## 路由优先级

路由匹配按照配置文件中的顺序进行，更具体的路由配置在前面：

1. **具体功能路由** (如: auth-login, core-users)
2. **OAuth2特殊路由** (如: auth-oauth2)
3. **通用服务路由** (如: auth-service, core-service)
4. **静态资源路由** (如: frontend-assets)
5. **兜底路由** (如: frontend, health-check)

## 监控指标

### 路由监控
- **请求量**: 各路由的请求统计
- **响应时间**: 路由响应时间分布
- **错误率**: 路由错误率统计
- **限流统计**: 限流触发次数

### 服务监控
- **服务可用性**: 各微服务健康状态
- **熔断状态**: 熔断器状态监控
- **负载均衡**: 服务实例负载情况

## 配置热更新

### 支持热更新的配置
- **限流规则**: 通过Nacos配置中心更新
- **熔断规则**: 通过Sentinel控制台更新
- **路由规则**: 通过Nacos配置中心更新

### 配置更新流程
1. 修改Nacos配置文件
2. 网关自动监听配置变化
3. 动态更新路由规则
4. 无需重启服务
