<template>
  <div class="social-login">
    <div class="divider">
      <span>或使用第三方账号登录</span>
    </div>
    <div class="social-buttons">
      <el-button 
        type="info" 
        plain 
        @click="handleSocialLogin('wechat')"
        class="social-button"
      >
        <i class="el-icon-user"></i>
        微信登录
      </el-button>
      <el-button 
        type="primary" 
        plain 
        @click="handleSocialLogin('dingtalk')"
        class="social-button"
      >
        <i class="el-icon-office-building"></i>
        钉钉登录
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { defineEmits } from 'vue'

// 定义事件
const emit = defineEmits(['social-login'])

// 第三方登录处理
const handleSocialLogin = (provider) => {
  // 触发第三方登录事件
  emit('social-login', provider)
}
</script>

<style scoped>
.social-login {
  margin-top: 20px;
}

.divider {
  display: flex;
  align-items: center;
  text-align: center;
  color: #909399;
  margin: 20px 0;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  border-bottom: 1px solid #dcdfe6;
}

.divider::before {
  margin-right: 10px;
}

.divider::after {
  margin-left: 10px;
}

.social-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.social-button {
  width: 120px;
}
</style>