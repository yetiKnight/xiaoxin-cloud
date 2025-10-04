<template>
  <div class="dashboard-container">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>业务系统B - 仪表板</span>
        </div>
      </template>
      <div class="welcome-content">
        <h3>欢迎使用业务系统B</h3>
        <p>您已成功通过OAuth2认证</p>
        <el-button type="primary" @click="fetchUserData">获取用户信息</el-button>
      </div>
    </el-card>
    
    <el-card class="data-card" v-if="userData">
      <template #header>
        <div class="card-header">
          <span>用户信息</span>
        </div>
      </template>
      <div class="user-data">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="用户ID">{{ userData.id }}</el-descriptions-item>
          <el-descriptions-item label="用户名">{{ userData.username }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ userData.email }}</el-descriptions-item>
          <el-descriptions-item label="角色">{{ userData.roles?.join(', ') }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
    
    <el-card class="api-card">
      <template #header>
        <div class="card-header">
          <span>API调用测试</span>
        </div>
      </template>
      <div class="api-test">
        <el-button type="success" @click="callBusinessApi">调用业务API</el-button>
        <div class="api-response" v-if="apiResponse">
          <h4>API响应:</h4>
          <pre>{{ JSON.stringify(apiResponse, null, 2) }}</pre>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'

const userData = ref(null)
const apiResponse = ref(null)

// 获取用户信息
const fetchUserData = async () => {
  try {
    // 从localStorage获取访问令牌
    const accessToken = localStorage.getItem('access_token')
    
    if (!accessToken) {
      ElMessage.error('未找到访问令牌，请重新登录')
      return
    }
    
    // 设置请求头
    request.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`
    
    // 调用用户信息API（通过网关）
    const response = await request.get('http://localhost:8080/api/user/info')
    userData.value = response.data
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败: ' + (error.response?.data?.message || error.message))
  }
}

// 调用业务API
const callBusinessApi = async () => {
  try {
    // 从localStorage获取访问令牌
    const accessToken = localStorage.getItem('access_token')
    
    if (!accessToken) {
      ElMessage.error('未找到访问令牌，请重新登录')
      return
    }
    
    // 设置请求头
    request.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`
    
    // 调用业务API（通过网关）
    const response = await request.get('http://localhost:8080/api/business/data')
    apiResponse.value = response.data
    ElMessage.success('API调用成功')
  } catch (error) {
    console.error('API调用失败:', error)
    apiResponse.value = error.response?.data || { error: error.message }
    ElMessage.error('API调用失败: ' + (error.response?.data?.message || error.message))
  }
}

// 页面加载时自动获取用户信息
onMounted(() => {
  // 检查是否有访问令牌
  const accessToken = localStorage.getItem('access_token')
  if (accessToken) {
    // 可以在这里自动获取用户信息或者等待用户点击按钮
    // fetchUserData()
  } else {
    ElMessage.warning('未检测到访问令牌，请先登录')
  }
})
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-card {
  margin-bottom: 20px;
}

.card-header {
  font-weight: bold;
  font-size: 16px;
}

.welcome-content {
  text-align: center;
  padding: 20px 0;
}

.welcome-content h3 {
  margin-bottom: 10px;
}

.user-data {
  padding: 20px 0;
}

.api-card {
  margin-top: 20px;
}

.api-test {
  padding: 20px 0;
}

.api-response {
  margin-top: 20px;
  padding: 15px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.api-response h4 {
  margin-top: 0;
}

.api-response pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  margin: 0;
}
</style>