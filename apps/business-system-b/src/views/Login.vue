<template>
  <div class="login-container">
    <div class="login-card">
      <h2>业务系统B登录</h2>
      <p class="description">使用OAuth2授权码模式进行认证</p>
      
      <div class="login-options">
        <el-button 
          type="primary" 
          @click="handleOAuthLogin"
          size="large"
          style="width: 100%"
        >
          OAuth2授权登录
        </el-button>
        
        <div class="divider">
          <span>或</span>
        </div>
        
        <el-button 
          @click="handleDirectLogin"
          size="large"
          style="width: 100%"
        >
          直接访问演示页面
        </el-button>
      </div>
      
      <div class="info-section">
        <h3>系统说明</h3>
        <ul>
          <li>本系统用于验证OAuth2授权码模式</li>
          <li>点击"OAuth2授权登录"将跳转到认证服务器</li>
          <li>认证成功后将返回本系统</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { onMounted } from 'vue'

const router = useRouter()

// 处理OAuth2登录
const handleOAuthLogin = () => {
  // 构造登录前端SPA的URL，携带当前系统的redirect_uri
  const loginFrontendUrl = new URL('http://localhost:3000/login')
  // 将业务系统B的回调地址作为redirect_uri传递
  loginFrontendUrl.searchParams.append('redirect_uri', 'http://localhost:8082')
  
  // 跳转到登录前端SPA
  window.location.href = loginFrontendUrl.toString()
}

// 处理直接登录（演示用）
const handleDirectLogin = () => {
  router.push('/dashboard')
}

// 页面加载时检查URL参数中的access_token
onMounted(() => {
  const urlParams = new URLSearchParams(window.location.search)
  const accessToken = urlParams.get('access_token')
  
  if (accessToken) {
    // 保存访问令牌
    localStorage.setItem('access_token', accessToken)
    
    // 移除URL中的access_token参数
    urlParams.delete('access_token')
    const newUrl = window.location.pathname + (urlParams.toString() ? '?' + urlParams.toString() : '')
    window.history.replaceState({}, document.title, newUrl)
    
    // 跳转到仪表板
    router.push('/dashboard')
  }
})
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 60px);
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 400px;
  padding: 40px 30px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  text-align: center;
}

.login-card h2 {
  margin-bottom: 10px;
  color: #333;
}

.description {
  color: #666;
  margin-bottom: 30px;
}

.login-options {
  margin-bottom: 30px;
}

.divider {
  display: flex;
  align-items: center;
  margin: 20px 0;
  color: #999;
}

.divider::before,
.divider::after {
  content: "";
  flex: 1;
  border-bottom: 1px solid #eee;
}

.divider::before {
  margin-right: 10px;
}

.divider::after {
  margin-left: 10px;
}

.info-section {
  text-align: left;
  background-color: #f5f5f5;
  padding: 15px;
  border-radius: 4px;
}

.info-section h3 {
  margin-top: 0;
  margin-bottom: 10px;
  color: #333;
}

.info-section ul {
  padding-left: 20px;
  margin: 0;
}

.info-section li {
  margin-bottom: 8px;
  color: #666;
}
</style>