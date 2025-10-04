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
          sub-title="正在跳转到业务系统..."
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const loading = ref(true)
const error = ref(false)
const errorMessage = ref('')

// 重试登录
const retryLogin = () => {
  router.push('/login')
}

// 处理授权码 - 直接重定向回业务系统B
const handleAuthorizationCode = (code, state) => {
  try {
    // 解析state参数，获取原始业务系统地址
    let redirectUri = 'http://localhost:8082/callback' // 默认业务系统B回调地址
    if (state) {
      try {
        const stateData = JSON.parse(atob(state))
        if (stateData.redirect_uri) {
          // 构造业务系统的回调URL
          const businessSystemUrl = new URL('/callback', stateData.redirect_uri)
          redirectUri = businessSystemUrl.toString()
        }
      } catch (e) {
        console.warn('解析state参数失败:', e)
      }
    }
    
    // 将授权码重定向到业务系统B的回调地址
    const redirectUrl = new URL(redirectUri)
    redirectUrl.searchParams.append('code', code)
    redirectUrl.searchParams.append('state', state)
    window.location.href = redirectUrl.toString()
  } catch (err) {
    console.error('处理授权码失败:', err)
    error.value = true
    errorMessage.value = err.message || '认证过程出现错误'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  // 获取URL参数
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
  min-height: 100vh;
  padding: 20px;
  background-color: #f0f2f5;
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