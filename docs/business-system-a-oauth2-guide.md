# 业务系统A OAuth2授权码模式验证指南

## 概述

本文档介绍了如何使用业务系统A来验证OAuth2授权码模式的登录认证流程。

## 系统架构

```
graph TB
    A[用户] --> B[业务系统A]
    B --> C[认证服务器]
    C --> D[用户认证]
    D --> C
    C --> B
    B --> E[访问受保护资源]
```

## 验证步骤

### 1. 启动相关服务

确保以下服务已启动：
1. 认证服务器 (端口 3000)
2. 业务系统A (端口 3001)

### 2. 访问业务系统A

打开浏览器访问: http://localhost:3001

### 3. 执行OAuth2认证流程

1. 在登录页面点击"OAuth2授权登录"按钮
2. 系统将重定向到认证服务器
3. 在认证服务器上输入用户名和密码进行登录
4. 授权后将重定向回业务系统A
5. 系统将显示认证成功信息并跳转到仪表板

### 4. 验证认证结果

1. 查看仪表板页面的用户信息
2. 访问个人资料页面查看详细信息
3. 检查浏览器本地存储中的访问令牌

## 关键代码说明

### 授权URL生成

```javascript
// src/utils/auth.js
export function generateAuthUrl(clientId, redirectUri, scope = 'read') {
  const baseUrl = 'http://localhost:3000/oauth/authorize'
  const state = generateRandomString()
  
  // 保存state用于后续验证
  sessionStorage.setItem('oauth_state', state)
  
  const params = new URLSearchParams({
    response_type: 'code',
    client_id: clientId,
    redirect_uri: redirectUri,
    scope: scope,
    state: state
  })
  
  return `${baseUrl}?${params.toString()}`
}
```

### 授权码处理

```javascript
// src/views/Callback.vue
const handleAuthorizationCode = async (code, state) => {
  try {
    // 验证state参数
    const savedState = sessionStorage.getItem('oauth_state')
    if (state !== savedState) {
      throw new Error('state参数不匹配，可能存在安全风险')
    }
    
    // 使用授权码换取访问令牌
    const response = await request.post('/oauth/token', {
      grant_type: 'authorization_code',
      code: code,
      client_id: 'business_system_a',
      client_secret: 'business_system_a_secret',
      redirect_uri: 'http://localhost:3001/callback'
    })
    
    if (response.access_token) {
      // 保存令牌
      authStore.setToken(response.access_token)
      
      // 获取用户信息
      const userResponse = await request.get('/v1/user/info')
      authStore.setUser(userResponse.data)
      
      // 跳转到首页
      router.push('/dashboard')
    }
  } catch (err) {
    console.error('处理授权码失败:', err)
    error.value = true
    errorMessage.value = err.message || '认证过程出现错误'
  }
}
```

## 安全注意事项

1. **客户端密钥保护**: 在生产环境中，客户端密钥不应暴露在前端代码中，应通过后端服务进行令牌交换
2. **State参数验证**: 必须验证state参数以防止CSRF攻击
3. **令牌存储**: 访问令牌存储在localStorage中，生产环境中应考虑更安全的存储方式
4. **HTTPS**: 生产环境中应使用HTTPS保护通信安全

## 故障排除

### 1. 认证服务器无法访问
- 检查认证服务器是否已启动
- 确认端口配置是否正确
- 检查网络连接

### 2. 授权码无效
- 检查客户端ID和密钥是否正确
- 确认回调URL是否匹配配置
- 验证授权码是否已过期

### 3. 令牌交换失败
- 检查认证服务器的令牌端点配置
- 确认请求参数是否正确
- 查看认证服务器日志获取详细错误信息

## 扩展功能

1. **刷新令牌**: 实现刷新令牌功能以延长用户会话
2. **权限控制**: 基于用户角色的细粒度权限控制
3. **API访问**: 使用访问令牌调用受保护的API资源
4. **单点登出**: 实现单点登出功能