<template>
  <div class="login-component">
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
          登录
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { defineEmits, reactive, ref } from 'vue'

// 定义事件
const emit = defineEmits(['login'])

// 表单引用
const loginFormRef = ref()

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

// 登录处理
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 触发登录事件
        emit('login', {
          username: loginForm.username,
          password: loginForm.password,
          rememberMe: rememberMe.value
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
    loginFormRef.value?.resetFields()
  }
})
</script>

<style scoped>
.login-component {
  padding: 20px;
}
</style>