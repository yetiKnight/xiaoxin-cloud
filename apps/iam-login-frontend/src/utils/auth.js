// 用户认证相关工具函数

/**
 * 验证邮箱格式
 * @param {string} email 
 * @returns {boolean}
 */
export function validateEmail(email) {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return re.test(email)
}

/**
 * 验证密码强度
 * @param {string} password 
 * @returns {boolean}
 */
export function validatePassword(password) {
  // 至少8位，包含字母和数字
  const re = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*#?&]{8,}$/
  return re.test(password)
}

/**
 * 设置认证Token
 * @param {string} token 
 */
export function setToken(token) {
  localStorage.setItem('token', token)
}

/**
 * 获取认证Token
 * @returns {string|null}
 */
export function getToken() {
  return localStorage.getItem('token')
}

/**
 * 移除认证Token
 */
export function removeToken() {
  localStorage.removeItem('token')
}

/**
 * 检查是否已认证
 * @returns {boolean}
 */
export function isAuthenticated() {
  return !!getToken()
}