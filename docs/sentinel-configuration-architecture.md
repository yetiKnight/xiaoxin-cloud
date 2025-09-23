# Sentinel配置架构说明

## 📋 配置架构概述

采用**分层配置架构**，将Sentinel配置分为平台层和服务层，避免配置冲突，提高可维护性。

## 🏗️ 配置分层

### 1. 平台层配置 (`iam-platform-dev.yml`)

```yaml
spring:
  cloud:
    sentinel:
      transport:
        dashboard: localhost:8181  # 统一Dashboard地址
      eager: true                  # 统一启动配置
      # 端口和数据源配置由各服务自定义
```

**作用**：
- 统一管理Dashboard地址
- 统一基础配置（如eager启动）
- 便于环境切换时统一修改

### 2. 服务层配置

各服务在本地`application.yml`中配置个性化参数：

#### iam-gateway (端口: 8717)
```yaml
spring:
  cloud:
    sentinel:
      transport:
        port: 8717
      datasource:
        gw-flow:
          nacos:
            dataId: iam-gateway-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: gw-flow
        gw-api-group:
          nacos:
            dataId: iam-gateway-api-group-rules  
            groupId: SENTINEL_GROUP
            rule-type: gw-api-group
```

#### iam-auth-service (端口: 8718)
```yaml
spring:
  cloud:
    sentinel:
      transport:
        port: 8718
      datasource:
        flow:
          nacos:
            dataId: iam-auth-service-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow
```

#### iam-core-service (端口: 8719)
```yaml
spring:
  cloud:
    sentinel:
      transport:
        port: 8719
      datasource:
        flow:
          nacos:
            dataId: iam-core-service-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow
```

#### iam-system-service (端口: 8720)
```yaml
spring:
  cloud:
    sentinel:
      transport:
        port: 8720
      datasource:
        flow:
          nacos:
            dataId: iam-system-service-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow
```

#### iam-audit-service (端口: 8721)
```yaml
spring:
  cloud:
    sentinel:
      transport:
        port: 8721
      datasource:
        flow:
          nacos:
            dataId: iam-audit-service-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow
```

## 🔧 端口分配

| 服务 | Sentinel端口 | 说明 |
|---|---|---|
| iam-gateway | 8717 | 网关服务，支持网关流控规则 |
| iam-auth-service | 8718 | 认证服务 |
| iam-core-service | 8719 | 核心业务服务 |
| iam-system-service | 8720 | 系统服务 |
| iam-audit-service | 8721 | 审计服务 |

## 📊 规则配置

### 规则文件命名规范
```
{服务名}-{规则类型}-rules
例：iam-auth-service-flow-rules
```

### 网关特殊规则
- `gw-flow`: 网关流控规则
- `gw-api-group`: API分组规则

### 规则存储
- **位置**: Nacos配置中心
- **分组**: SENTINEL_GROUP
- **命名空间**: dev

## ✅ 架构优势

1. **避免冲突**: 端口和数据源配置完全隔离
2. **统一管理**: Dashboard地址等通用配置集中管理
3. **个性化**: 各服务可根据业务特点配置专用规则
4. **易维护**: 修改Dashboard地址只需更新平台配置
5. **可扩展**: 新增服务只需分配新端口和规则文件

## 🚀 使用说明

1. **启动Sentinel Dashboard**: http://localhost:8181
2. **访问各服务监控**: 在Dashboard中可看到各服务的监控数据
3. **配置规则**: 通过Nacos配置中心或Dashboard配置限流规则
4. **查看效果**: 触发限流后可在Dashboard看到实时数据

## 🔍 故障排查

1. **服务未在Dashboard显示**: 检查端口是否冲突，确保各服务端口唯一
2. **规则不生效**: 检查Nacos中规则文件是否存在且格式正确
3. **Dashboard连接失败**: 检查Dashboard地址配置是否正确

