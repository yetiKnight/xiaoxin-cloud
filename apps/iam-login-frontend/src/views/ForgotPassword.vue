<template>
  <div class="forgot-password-container">
    <div class="forgot-password-form">
      <h2>忘记密码</h2>
      <el-form 
        ref="forgotFormRef" 
        :model="forgotForm" 
        :rules="forgotRules" 
        label-width="80px"
        @submit.prevent="handleForgot"
      >
        <el-form-item label="邮箱" prop="email">
          <el-input 
            v-model="forgotForm.email" 
            placeholder="请输入注册时使用的邮箱" 
            clearable
          />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            native-type="submit" 
            :loading="loading"
            style="width: 100%"
          >
            发送重置链接
          </el-button>
        </el-form-item>
        
        <div class="forgot-links">
          <el-link type="primary" @click="$router.push('/login')">
            返回登录
          </el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { validateEmail } from '@/utils/auth'
import request from '@/utils/request'

// 表单引用
const forgotFormRef = ref()

// 路由实例
const router = useRouter()

// 表单数据
const forgotForm = reactive({
  email: ''
})

// 加载状态
const loading = ref(false)

// 表单验证规则
const forgotRules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { validator: validateEmailRule, trigger: 'blur' }
  ]
}

// 邮箱验证规则
function validateEmailRule(rule, value, callback) {
  if (!value) {
    callback(new Error('请输入邮箱'))
  } else if (!validateEmail(value)) {
    callback(new Error('请输入正确的邮箱格式'))
  } else {
    callback()
  }
}

// 忘记密码处理
const handleForgot = async () => {
  if (!forgotFormRef.value) return
  
  await forgotFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 调用忘记密码API
        const response = await request.post('/v1/auth/forgot-password', {
          email: forgotForm.email
        })
        
        if (response.code === 200) {
          ElMessage.success('重置链接已发送到您的邮箱，请查收')
          // 跳转到登录页面
          router.push('/login')
        } else {
          ElMessage.error(response.message || '发送失败')
        }
      } catch (error) {
        console.error('发送失败:', error)
        ElMessage.error('发送失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.forgot-password-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f0f2f5;
}

.forgot-password-form {
  width: 400px;
  padding: 30px;
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.forgot-password-form h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.forgot-links {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>