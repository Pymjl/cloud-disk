import { ARFactory } from '@/utils/axios'

/**
 * 获取用户列表
 * @param currentPage 当前页
 * @param pageSize 每页数量
 * @returns 一种很奇怪的分页格式
 */
export const getUserList = (currentPage: number, pageSize: number) =>
  ARFactory({
    url: `/admin/users/${currentPage}/${pageSize}`,
    method: 'get'
  }) as Promise<{
    succeed: boolean
    res: {
      records: Array<{
        id: number
        username: string
        nickname: string
        avatar: string
        identity: number
        createTime: number
        updateTime: number
      }>
      message: string
      totalElements: number
      currentPage: number
      pageSize: number
      totalPages: number
      numberOfElements: number
    }
  }>

/**
 * 添加管理员
 * @param id 用户 ID
 * @returns 仅状态返回体，result 为 null
 */
export const addAdmin = (id: number) =>
  ARFactory({
    url: `/admin/user/${id}`,
    method: 'patch'
  }) as Promise<{
    succeed: boolean
    res: {
      result: null
      message: string
    }
  }>

/**
 * 删除用户
 * @param id 用户 ID
 * @returns 仅状态返回体，result 为 null
 */
export const delUser = (id: number) =>
  ARFactory({
    url: `/admin/user/${id}`,
    method: 'delete'
  }) as Promise<{
    succeed: boolean
    res: {
      result: null
      message: string
    }
  }>
