// 本地存储工具函数

/**
 * 设置本地存储项
 * @param {string} key 
 * @param {any} value 
 */
export function setStorage(key, value) {
  try {
    localStorage.setItem(key, JSON.stringify(value))
  } catch (error) {
    console.error('设置本地存储失败:', error)
  }
}

/**
 * 获取本地存储项
 * @param {string} key 
 * @returns {any}
 */
export function getStorage(key) {
  try {
    const item = localStorage.getItem(key)
    return item ? JSON.parse(item) : null
  } catch (error) {
    console.error('获取本地存储失败:', error)
    return null
  }
}

/**
 * 移除本地存储项
 * @param {string} key 
 */
export function removeStorage(key) {
  try {
    localStorage.removeItem(key)
  } catch (error) {
    console.error('移除本地存储失败:', error)
  }
}

/**
 * 清空本地存储
 */
export function clearStorage() {
  try {
    localStorage.clear()
  } catch (error) {
    console.error('清空本地存储失败:', error)
  }
}