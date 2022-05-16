/**
 * 在本地存储中新增键值对
 * @param key 键
 * @param value 值
 * @param expires 过期时间（毫秒）
 */
const setItem = (key: string, value: unknown, expires?: number) => {
  if (expires) {
    // 带有过期时间的情形
    localStorage.setItem(
      key,
      JSON.stringify({
        value,
        expires,
        startTime: new Date().getTime()
      })
    )
  } else {
    // 永不过期的情形
    localStorage.setItem(key, JSON.stringify({ value }))
  }
}

/**
 * 在本地存储中删除键值对
 * @param key 键
 */
const removeItem = (key: string) => {
  localStorage.removeItem(key)
}

/**
 * 从本地存储中获取键值对的值
 * @param key 键
 */
const getItem = (key: string) => {
  const data = localStorage.getItem(key)
  if (data) {
    const { value, expires, startTime } = JSON.parse(data)
    if (expires) {
      // 带有过期时间的情形
      const now = new Date().getTime()
      if (now - startTime > expires) {
        // 过期了
        removeItem(key)
        return null
      } else {
        return value
      }
    } else {
      // 永不过期的情形
      return value
    }
  }
  return null
}

/**
 * 清空本地存储
 */
const clear = () => {
  localStorage.clear()
}

export default { setItem, removeItem, getItem, clear }
