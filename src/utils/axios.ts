import axios from 'axios'
import ls from './ls'
import { API_URL } from '@/config'

const Axios = axios.create({
  baseURL: API_URL,
  timeout: 10000
})

// 当前所有请求
const currentReq = new Map()

/**
 * 添加请求
 */
const addReq = (config: import('axios').AxiosRequestConfig) => {
  const reqToken = [config.method, config.url, JSON.stringify(config.params), JSON.stringify(config.data)].join('&')
  config.cancelToken =
    config.cancelToken ||
    new axios.CancelToken((cancel) => {
      if (!currentReq.has(reqToken)) currentReq.set(reqToken, cancel)
    })
}

/**
 * 删除当前重复的请求
 */
const delReq = (config: import('axios').AxiosRequestConfig) => {
  const reqToken = [config.method, config.url, JSON.stringify(config.params), JSON.stringify(config.data)].join('&')
  if (currentReq.has(reqToken)) {
    const cancel = currentReq.get(reqToken)
    cancel(reqToken)
    currentReq.delete(reqToken)
  }
}

// 若重复发起同一请求，较旧的请求将被取消
Axios.interceptors.request.use(
  (config) => {
    delReq(config)
    addReq(config)
    return config
  },
  (err) => Promise.reject(err)
)

// 请求完成后正常移出 Map
Axios.interceptors.response.use(
  (response) => {
    delReq(response)
    return response
  },
  (err) => Promise.reject(err)
)

// 判断令牌是否已失效
Axios.interceptors.response.use(
  (response) => {
    if (response.data.message === 'token已过期,请重新登录') {
      // 清空本地存储（清除 Token）
      ls.clear()
      // 刷新页面将会导致路由前置守卫发现 Token 已不存在，然后会跳转到登录页面
      // eslint-disable-next-line no-restricted-globals
      location.reload()
    }
    return response
  },
  (err) => Promise.reject(err)
)

/**
 * 清空请求
 */
export const cleanReq = () => {
  currentReq.forEach((cancel, url) => cancel(url))
  currentReq.clear()
}

/**
 * 请求构造工厂
 * @param config 请求配置参数，由于设计缺陷，传入的 headers 参数将被忽略
 */
export const ARFactory = async (config: import('axios').AxiosRequestConfig): Promise<{ succeed: boolean; res: unknown }> => {
  const {
    data: { succeed = false, ...res }
  } = await Axios(
    ls.getItem('token') ? { ...config, headers: { ...config.headers, Authorization: `Bearer ${ls.getItem('token')}` } } : config
  )
  return { succeed, res }
}
