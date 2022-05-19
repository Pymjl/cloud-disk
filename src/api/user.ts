import { ARFactory } from '@/utils/axios'

/**
 * 获取当前用户信息
 */
export const getUserInfo = () =>
  ARFactory({
    url: '/user/info',
    method: 'get'
  }) as Promise<{
    succeed: boolean
    res: {
      result: {
        id: number
        username: string
        nickname: string
        avatar: string
        identity: number
        createTime: number
        updateTime: number
      }
      message: string
    }
  }>
