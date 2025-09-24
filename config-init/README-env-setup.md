# Windows基础服务环境变量配置指南

本目录提供了在Windows环境下快速配置小信云IAM平台基础服务所需环境变量的脚本工具。

## 📁 脚本文件说明

| 文件名 | 类型 | 功能 | 推荐场景 |
|--------|------|------|----------|
| `setup-env-windows.bat` | 批处理 | 基础服务环境变量设置 | 快速设置核心服务配置 |
| `check-env-windows.bat` | 批处理 | 环境变量检查验证 | 验证基础服务配置 |

## 🚀 快速开始

### 方式一：使用批处理脚本（推荐）

```cmd
# 1. 以管理员身份打开命令提示符
# 2. 进入项目目录
cd E:\projects\xiaoxin-cloud\config-init

# 3. 修改配置值（编辑setup-env-windows.bat顶部的配置区域）
notepad setup-env-windows.bat

# 4. 运行配置脚本
setup-env-windows.bat

# 5. 验证配置
check-env-windows.bat
```

## 🔧 配置说明

### 配置与逻辑分离设计

新版本脚本采用**配置与逻辑分离**的设计：

- **配置区域**：脚本顶部集中定义所有环境变量
- **逻辑区域**：脚本底部处理环境变量设置逻辑
- **易于维护**：只需修改配置区域即可应用新配置

### 修改配置的方法

1. **编辑配置文件**：
   ```cmd
   notepad setup-env-windows.bat
   ```

2. **找到配置区域**（脚本顶部）：
   ```batch
   :: ==========================================
   :: 配置变量定义区域 - 在此修改配置值
   :: ==========================================
   ```

3. **修改对应变量**：
   ```batch
   :: 修改前
   set "REDIS_PASSWORD_VALUE=123456"
   
   :: 修改后
   set "REDIS_PASSWORD_VALUE=your_secure_password"
   ```

4. **保存并重新运行脚本**


## 🔧 配置的环境变量

### 微服务基础设施配置
```bash
NACOS_SERVER_ADDR=localhost:8848     # Nacos注册/配置中心
NACOS_NAMESPACE=dev                  # Nacos命名空间
REDIS_HOST=localhost                 # Redis缓存服务器
REDIS_PORT=6379                      # Redis端口
REDIS_PASSWORD=123456                # Redis密码
SENTINEL_DASHBOARD=localhost:8181    # Sentinel流控控制台
ROCKETMQ_NAME_SERVER=localhost:9876  # RocketMQ消息服务器
SEATA_SERVER=localhost:8091          # Seata分布式事务服务器
```



## ⚠️ 重要注意事项

### 1. 管理员权限
- **系统级环境变量**：需要管理员权限，对所有用户生效
- **用户级环境变量**：无需管理员权限，仅对当前用户生效

### 2. 生产环境安全
生产环境请务必：
- 使用安全的Redis密码
- 设置复杂的数据库密码

### 3. 环境变量生效
设置完成后需要：
- 重启IDE（如IntelliJ IDEA、VS Code）
- 重启命令行工具
- 或重启Windows（确保所有应用生效）

## 🔍 故障排除

### Q: 环境变量设置后不生效？
**A:** 请重启IDE或命令行工具。Windows需要重新加载环境变量。

### Q: 如何修改配置值？
**A:** 编辑 `setup-env-windows.bat` 顶部的配置区域：
```batch
:: 找到对应变量并修改
set "REDIS_PASSWORD_VALUE=your_new_password"
```

### Q: 如何快速找到配置模板？
**A:** 查看 `env-config-template.bat` 文件，包含各环境的配置示例：
```cmd
type env-config-template.bat
```

### Q: 批处理脚本中文乱码？
**A:** 确保命令提示符使用UTF-8编码，或在IDE中以UTF-8编码打开文件。

### Q: 如何验证配置是否正确？
**A:** 运行检查脚本：
```cmd
check-env-windows.bat
```
脚本会显示每个变量的状态：匹配默认值、自定义值或未设置。

### Q: 如何删除环境变量？
**A:** 使用命令行：
```cmd
setx 变量名 "" /M        # 删除系统级变量（需管理员权限）
setx 变量名 ""           # 删除用户级变量
```

### Q: 如何查看当前所有环境变量？
**A:** 在命令行运行：
```cmd
set                    # 查看所有环境变量
echo %变量名%           # 查看特定变量
```

### Q: 不同环境如何快速切换配置？
**A:** 直接编辑配置文件：
1. 编辑 `setup-env-windows.bat` 顶部的配置区域
2. 修改对应环境的配置值（如服务器地址、端口等）
3. 重新运行脚本应用新配置

## 📖 相关文档

- [Spring Cloud Alibaba开发规范](../docs/spring-cloud-alibaba-guide.md)
- [Nacos配置中心使用指南](./README-nacos-configuration.md)
- [数据库配置说明](./README-database.md)

## 🤝 贡献

如果发现脚本问题或需要改进，请提交Issue或Pull Request。
