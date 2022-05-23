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
 * 上传头像
 * @param file 头像文件
 * @returns 通用响应体，result 为 头像 URL
 */
export const uploadAvatar = (file: File) => {
  const data = new FormData()
  data.append('file', file)

  return ARFactory({
    url: '/user/avatar',
    method: 'patch',
    timeout: 600000,
    data
  }) as Promise<{
    succeed: boolean
    res: {
      result: string
      message: string
    }
  }>
}

/**
 * 修改用户昵称
 * @param nickname 新昵称
 * @returns 仅状态响应体，result 为 null
 */
export const changeNickname = (nickname: string) =>
  ARFactory({
    url: `/user/nickname/${nickname}`,
    method: 'patch'
  }) as Promise<{
    succeed: boolean
    res: {
      result: null
      message: string
    }
  }>

/**
 * 修改用户密码
 * @param password 新密码
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
