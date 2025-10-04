<template>
  <div class="register-component">
    <el-form 
      ref="registerFormRef" 
      :model="registerForm" 
      :rules="registerRules" 
      label-width="80px"
      @submit.prevent="handleRegister"
    >
      <el-form-item label="用户名" prop="username">
        <el-input 
          v-model="registerForm.username" 
          placeholder="请输入用户名" 
          clearable
        />
      </el-form-item>
      
      <el-form-item label="邮箱" prop="email">
        <el-input 
          v-model="registerForm.email" 
          placeholder="请输入邮箱" 
          clearable
        />
      </el-form-item>
      
      <el-form-item label="密码" prop="password">
        <el-input 
          v-model="registerForm.password" 
          type="password" 
          placeholder="请输入密码" 
          show-password
        />
      </el-form-item>
      
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input 
          v-model="registerForm.confirmPassword" 
          type="password" 
          placeholder="请再次输入密码" 
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
          注册
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, defineEmits } from 'vue'
import { validateEmail, validatePassword } from '@/utils/auth'

// 定义事件
const emit = defineEmits(['register'])

// 表单引用
const registerFormRef = ref()

// 表单数据
const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

// 加载状态
const loading = ref(false)

// 表单验证规则
const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度为3-20个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { validator: validateEmailRule, trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { validator: validatePasswordRule, trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
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

// 密码验证规则
function validatePasswordRule(rule, value, callback) {
  if (!value) {
    callback(new Error('请输入密码'))
  } else if (!validatePassword(value)) {
    callback(new Error('密码至少8位，包含字母和数字'))
  } else {
    callback()
  }
}

// 确认密码验证规则
function validateConfirmPassword(rule, value, callback) {
  if (!value) {
    callback(new Error('请确认密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

// 注册处理
const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 触发注册事件
        emit('register', {
          username: registerForm.username,
          email: registerForm.email,
          password: registerForm.password
        })
      } finally {
        loading.value = false
      }
    }
  })
}

// 定义暴露给父组件的方法
defineExpose({
  resetForm: () => {
    registerFormRef.value?.resetFields()
  }
})
</script>

<style scoped>
.register-component {
  padding: 20px;
}
</style>