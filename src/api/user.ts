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

/**
 * 修改用户密码
 * @returns 仅状态响应体，result 为 null
 */
export const changePassword = (password: string) => {
  const data = new FormData()
  data.append('password', password)

  return ARFactory({
    url: '/user/password',
    method: 'patch',
    data
  }) as Promise<{
    succeed: boolean
    res: {
      result: null
      message: string
    }
  }>
}
