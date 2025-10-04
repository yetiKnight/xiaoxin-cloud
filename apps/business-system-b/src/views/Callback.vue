<template>
  <div class="callback-container">
    <div class="callback-card">
      <div v-if="loading" class="loading-section">
        <el-skeleton animated>
          <template #template>
            <el-skeleton-item variant="circle" style="width: 60px; height: 60px;" />
            <el-skeleton-item variant="h3" style="width: 50%" />
            <el-skeleton-item variant="p" style="width: 100%" />
            <el-skeleton-item variant="p" style="width: 80%" />
          </template>
        </el-skeleton>
      </div>
      
      <div v-else-if="error" class="error-section">
        <el-result
          icon="error"
          title="认证失败"
          :sub-title="errorMessage"
        >
          <template #extra>
            <el-button type="primary" @click="retryLogin">重新登录</el-button>
          </template>
        </el-result>
      </div>
      
      <div v-else class="success-section">
        <el-result
          icon="success"
          title="认证成功"
          sub-title="正在跳转到系统首页..."
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()

const loading = ref(true)
const error = ref(false)
const errorMessage = ref('')

// 重试登录
const retryLogin = () => {
  router.push('/login')
}

// 处理授权码 - 发送给后端处理（模拟后端处理）
const handleAuthorizationCode = async (code, state) => {
  try {
    // 在实际应用中，这里应该调用业务系统B的后端API
    // 后端再调用认证服务换取访问令牌
    // 为了演示目的，我们直接在前端处理，但在生产环境中应该由后端完成
    
    // 模拟发送授权码到后端
    console.log('发送授权码到后端处理:', { code, state });
    
    // 直接调用认证服务换取访问令牌（在生产环境中应由后端完成）
    const tokenResponse = await fetch('/oauth2/token', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams({
        grant_type: 'authorization_code',
        code: code,
        redirect_uri: 'http://localhost:8082/callback', // 业务系统B的回调地址
        client_id: 'business-system-b',  // 使用数据库中配置的业务系统B客户端ID
        client_secret: 'business-system-b-secret'  // 使用数据库中配置的业务系统B客户端密钥
      })
    })
    
    const tokenData = await tokenResponse.json()
    
    if (!tokenResponse.ok) {
      throw new Error(tokenData.error_description || '获取访问令牌失败')
    }
    
    if (tokenData.access_token) {
      // 保存访问令牌到localStorage
      localStorage.setItem('access_token', tokenData.access_token)
      
      // 跳转到仪表板
      setTimeout(() => {
        router.push('/dashboard')
      }, 1500)
    } else {
      throw new Error(tokenData.error_description || '获取访问令牌失败')
    }
  } catch (err) {
    console.error('处理授权码失败:', err)
    error.value = true
    errorMessage.value = err.message || '认证过程出现错误'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  // 获取URL参数（从登录前端SPA重定向过来的授权码）
  const urlParams = new URLSearchParams(window.location.search)
  const code = urlParams.get('code')
  const state = urlParams.get('state')
  const errorParam = urlParams.get('error')
  
  if (errorParam) {
    error.value = true
    errorMessage.value = urlParams.get('error_description') || '认证服务器返回错误'
    loading.value = false
    return
  }
  
  if (code) {
    handleAuthorizationCode(code, state)
  } else {
    error.value = true
    errorMessage.value = '缺少必要的认证参数'
    loading.value = false
  }
})
</script>

<style scoped>
.callback-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 60px);
  padding: 20px;
}

.callback-card {
  width: 100%;
  max-width: 500px;
  padding: 40px 30px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.loading-section {
  text-align: center;
}

.success-section,
.error-section {
  padding: 20px 0;
}
</style>