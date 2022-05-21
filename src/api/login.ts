import { ARFactory } from '@/utils/axios'

/**
 * 获取邮箱验证码
 * @param username 用户名
 * @param key 请求图片验证码时的唯一标识
 * @param code 图片验证码显示的字符串
 * @returns 通用响应体，result 为邮箱验证码标识
 */
export const getEmailVerify = (username: string, key: string, code: string) =>
  ARFactory({
    url: '/codes/email',
    method: 'get',
    params: { username, key, code }
  }) as Promise<{
    succeed: boolean
    res: {
      result: string
      message: string
    }
  }>

/**
 * 登录
 * @param username 用户名
 * @param password 密码
 * @param verifyCode 邮箱验证码
 * @param verifyKey 邮箱验证码标识
 * @returns 通用响应体，result 为 Token
 */
export const login = (username: string, password: string, verifyCode: string, verifyKey: string) =>
  ARFactory({
    url: '/login',
    method: 'post',
    data: { username, password, verifyCode, verifyKey }
  }) as Promise<{
    succeed: boolean
    res: {
      result: string
      message: string
    }
  }>

/**
 * 注册
 * @param username 用户名
 * @param nickname 昵称
 * @param password 密码
 * @param verifyCode 邮箱验证码
 * @param verifyKey 邮箱验证码标识
 * @returns 仅状态响应体，result 为 null
 */
export const register = (username: string, nickname: string, password: string, verifyCode: string, verifyKey: string) =>
  ARFactory({
    url: '/register',
    method: 'post',
    data: { username, nickname, password, verifyCode, verifyKey }
  }) as Promise<{
    succeed: boolean
    res: {
      result: null
      message: string
    }
  }>

/**
 * 找回密码
 * @param username 用户名
 * @param verifyCode 邮箱验证码
 * @returns 仅状态响应体，result 为 null
 */
export const recovery = (username: string, verifyCode: string) => {
  const data = new FormData()
  data.append('username', username)
  data.append('verifyCode', verifyCode)

  return ARFactory({
    url: '/reset/password',
    method: 'post',
    data
  }) as Promise<{
    succeed: boolean
    res: {
      result: null
      message: string
    }
  }>
}
