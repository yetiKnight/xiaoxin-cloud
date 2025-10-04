<template>
  <div class="login-container">
    <div class="login-form">
      <h2>用户登录</h2>
      <div class="login-description">
        <p>欢迎使用统一认证平台</p>
        <p>即将跳转到认证服务器完成登录</p>
      </div>
      
      <el-form 
        ref="loginFormRef" 
        :model="loginForm" 
        :rules="loginRules" 
        label-width="80px"
        @submit.prevent="handleLogin"
      >
        <el-form-item label="用户名" prop="username">
          <el-input 
            v-model="loginForm.username" 
            placeholder="请输入用户名或邮箱" 
            clearable
          />
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="请输入密码" 
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-checkbox v-model="rememberMe">记住我</el-checkbox>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            native-type="submit" 
            :loading="loading"
            style="width: 100%"
          >
            授权码登录
          </el-button>
        </el-form-item>
        
        <div class="login-info">
          <p>安全提示：您的凭据将直接提交给认证服务器</p>
        </div>
      </el-form>
      
      <div class="login-links">
        <el-link type="primary" @click="$router.push('/forgot-password')">
          忘记密码?
        </el-link>
        <el-link type="primary" @click="$router.push('/register')">
          注册账号
        </el-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'

// 表单引用
const loginFormRef = ref()

// 路由实例
const router = useRouter()

// 表单数据
const loginForm = reactive({
  username: '',
  password: ''
})

// 记住我
const rememberMe = ref(false)

// 加载状态
const loading = ref(false)

// 表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名或邮箱', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

// 登录处理 - 默认就是授权码方式登录
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 获取回调地址（从URL参数或默认值）
        const urlParams = new URLSearchParams(window.location.search)
        const redirectUri = urlParams.get('redirect_uri') || 'http://localhost:3000/callback'
        
        // 生成PKCE参数
        const codeVerifier = generateCodeVerifier()
        const codeChallenge = await generateCodeChallenge(codeVerifier)
        
        // 保存code_verifier到sessionStorage用于后续换取token
        sessionStorage.setItem('oauth_code_verifier', codeVerifier)
        
        // 构造授权请求URL - 使用相对路径通过代理访问认证服务
        const authUrl = new URL('/oauth2/authorize', window.location.origin)
        authUrl.searchParams.append('response_type', 'code')
        authUrl.searchParams.append('client_id', 'iam-login-client')  // 使用数据库中配置的登录前端SPA客户端ID
        authUrl.searchParams.append('redirect_uri', redirectUri)
        authUrl.searchParams.append('scope', 'openid profile email')
        authUrl.searchParams.append('code_challenge', codeChallenge)
        authUrl.searchParams.append('code_challenge_method', 'S256')
        
        // 如果有redirect_uri参数，保存到state中
        if (urlParams.get('redirect_uri')) {
          const state = btoa(JSON.stringify({ redirect_uri: urlParams.get('redirect_uri') }))
          authUrl.searchParams.append('state', state)
        }
        
        // 如果用户输入了用户名密码，可以尝试预填充到认证服务器（可选）
        if (loginForm.username) {
          // 注意：出于安全考虑，不建议直接传递密码到认证服务器
          // 可以传递用户名作为预填充
          // 这需要认证服务器支持相应的参数
        }
        
        // 重定向到授权服务器
        window.location.href = authUrl.toString()
      } catch (error) {
        console.error('登录失败:', error)
        ElMessage.error('登录失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
  })
}

// 生成PKCE code_verifier
function generateCodeVerifier() {
  const array = new Uint8Array(32)
  window.crypto.getRandomValues(array)
  return base64URLEncode(array)
}

// 生成PKCE code_challenge
function generateCodeChallenge(codeVerifier) {
  const encoder = new TextEncoder()
  const data = encoder.encode(codeVerifier)
  return sha256(data).then(hash => base64URLEncode(hash))
}

// SHA-256哈希函数
async function sha256(data) {
  const hash = await crypto.subtle.digest('SHA-256', data)
  return new Uint8Array(hash)
}

// Base64 URL编码
function base64URLEncode(array) {
  let string = ''
  for (let i = 0; i < array.byteLength; i++) {
    string += String.fromCharCode(array[i])
  }
  return btoa(string)
    .replace(/\+/g, '-')
    .replace(/\//g, '_')
    .replace(/=/g, '')
}

// 页面加载时，如果选择了记住我，自动填充用户名
onMounted(() => {
  const rememberedUsername = localStorage.getItem('rememberedUsername')
  if (rememberedUsername) {
    loginForm.username = rememberedUsername
    rememberMe.value = true
  }
  
  // 自动触发登录，提供更好的用户体验
  // 注意：在生产环境中可能需要用户明确点击登录按钮
  // setTimeout(() => {
  //   if (loginFormRef.value) {
  //     handleLogin();
  //   }
  // }, 1000);
})
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f0f2f5;
}

.login-form {
  width: 400px;
  padding: 30px;
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-form h2 {
  text-align: center;
  margin-bottom: 10px;
  color: #333;
}

.login-description {
  text-align: center;
  margin-bottom: 20px;
  color: #666;
}

.login-description p {
  margin: 5px 0;
}

.login-info {
  margin-top: 15px;
  padding: 10px;
  background-color: #e6f7ff;
  border-radius: 4px;
  font-size: 12px;
  color: #666;
}

.login-links {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}
</style>