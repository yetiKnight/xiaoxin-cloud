<template>
  <div class="reset-password-container">
    <div class="reset-password-form">
      <h2>重置密码</h2>
      <el-form 
        ref="resetFormRef" 
        :model="resetForm" 
        :rules="resetRules" 
        label-width="80px"
        @submit.prevent="handleReset"
      >
        <el-form-item label="新密码" prop="password">
          <el-input 
            v-model="resetForm.password" 
            type="password" 
            placeholder="请输入新密码" 
            show-password
          />
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input 
            v-model="resetForm.confirmPassword" 
            type="password" 
            placeholder="请再次输入新密码" 
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            native-type="submit" 
            :loading="loading"
            style="width: 100%"
          >
            重置密码
          </el-button>
        </el-form-item>
        
        <div class="reset-links">
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
import { useRouter, useRoute } from 'vue-router'
import { validatePassword } from '@/utils/auth'
import request from '@/utils/request'

// 表单引用
const resetFormRef = ref()

// 路由实例
const router = useRouter()
const route = useRoute()

// 表单数据
const resetForm = reactive({
  password: '',
  confirmPassword: ''
})

// 加载状态
const loading = ref(false)

// 表单验证规则
const resetRules = {
  password: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { validator: validatePasswordRule, trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

// 密码验证规则
function validatePasswordRule(rule, value, callback) {
  if (!value) {
    callback(new Error('请输入新密码'))
  } else if (!validatePassword(value)) {
    callback(new Error('密码至少8位，包含字母和数字'))
  } else {
    callback()
  }
}

// 确认密码验证规则
function validateConfirmPassword(rule, value, callback) {
  if (!value) {
    callback(new Error('请确认新密码'))
  } else if (value !== resetForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

// 重置密码处理
const handleReset = async () => {
  if (!resetFormRef.value) return
  
  await resetFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 获取路由参数中的token
        const token = route.params.token
        
        // 调用重置密码API
        const response = await request.post(`/v1/auth/reset-password/${token}`, {
          password: resetForm.password
        })
        
        if (response.code === 200) {
          ElMessage.success('密码重置成功')
          // 跳转到登录页面
          router.push('/login')
        } else {
          ElMessage.error(response.message || '密码重置失败')
        }
      } catch (error) {
        console.error('密码重置失败:', error)
        ElMessage.error('密码重置失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.reset-password-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f0f2f5;
}

.reset-password-form {
  width: 400px;
  padding: 30px;
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.reset-password-form h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.reset-links {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>