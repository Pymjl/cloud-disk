import { ARFactory } from '@/utils/axios'

/**
 * 获取邮箱验证码
 * @param username 用户名
 * @param key 请求图片验证码时的唯一标识
 * @param code 图片验证码显示的字符串
 * @returns 标准返回体
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
