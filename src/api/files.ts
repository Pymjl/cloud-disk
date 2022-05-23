import { ARFactory } from '@/utils/axios'

/**
 * 上传文件
 * @param path 上传到的路径
 * @param file 文件
 * @returns 仅状态返回体，result 为 null
 */
export const uploadFile = (path: string, file: File) => {
  const data = new FormData()
  data.append('path', path)
  data.append('file', file)

  ARFactory({
    url: `/files/upload`,
    method: 'post',
    timeout: 600000
  }) as Promise<{
    succeed: boolean
    res: {
      result: null
      message: string
    }
  }>
}

/**
 * 获取文件列表
 * @param path 路径
 * @returns 通用响应体，result 为文件信息结构体数组
 */
export const getFileList = (path: string) =>
  ARFactory({
    url: `/files/list`,
    method: 'get',
    params: { path }
  }) as Promise<{
    succeed: boolean
    res: {
      result: Array<{
        type: string
        name: string
        link: string | null
      }>
      message: string
    }
  }>

/**
 * 移动文件
 * @param originPath 原来的路径
 * @param targetPath 目标路径，需要带上原来的文件名称
 * @returns 仅状态返回体，result 为 null
 */
export const moveFile = (originPath: string, targetPath: string) => {
  const data = new FormData()
  data.append('originPath', originPath)
  data.append('targetPath', targetPath)

  ARFactory({
    url: `/files/move/file`,
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

/**
 * 移动文件夹
 * @param originPath 原来的文件夹路径，注意这个路径的最后是需要移动的文件夹名称
 * @param targetPath 目标文件夹路径，表示将文件夹复制到这个文件夹下面
 * @returns 仅状态返回体，result 为 null
 */
export const moveFolder = (originPath: string, targetPath: string) => {
  const data = new FormData()
  data.append('originPath', originPath)
  data.append('targetPath', targetPath)

  ARFactory({
    url: `/files/move/folder`,
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

/**
 * 复制文件
 * @param originPath 原来的完整文件路径名，eg. test/dir/RC.jpg
 * @param targetPath 复制后的文件路径名，eg. test/dir/RC-1.jpg
 * @returns 仅状态返回体，result 为 null
 */
export const copyFile = (originPath: string, targetPath: string) => {
  const data = new FormData()
  data.append('originPath', originPath)
  data.append('targetPath', targetPath)

  ARFactory({
    url: `/files/copy`,
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

/**
 * 复制文件夹
 * @param originPath 原始路径 eg: foo/dest/
 * @param targetPath 目标路径 eg: foo/bar/，移动后的目录为 foo/bar/dest/xxx
 * @returns 仅状态返回体，result 为 null
 */
export const copyFolder = (originPath: string, targetPath: string) => {
  const data = new FormData()
  data.append('originPath', originPath)
  data.append('targetPath', targetPath)

  ARFactory({
    url: `/files/copy`,
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

// FIXME 此接口有问题，需要确认文档
/**
 * 新建文件夹
 * @param path 文件夹自身路径
 * @returns 仅状态返回体，result 为 null
 */
export const newFolder = (path: string) =>
  ARFactory({
    url: `/folder`,
    method: 'post',
    params: { path }
  }) as Promise<{
    succeed: boolean
    res: {
      result: null
      message: string
    }
  }>

/**
 * 将文件或者文件夹添加到回收站
 * @param path 要删除的文件或文件夹路径
 * @returns 仅状态返回体，result 为 null
 */
export const moveToTrash = (path: string) => {
  const data = new FormData()
  data.append('path', path)

  ARFactory({
    url: `/files/file`,
    method: 'delete',
    data
  }) as Promise<{
    succeed: boolean
    res: {
      result: null
      message: string
    }
  }>
}

/**
 * 获取回收站文件列表
 * @param path 路径
 * @returns 通用响应体，result 为文件信息结构体数组
 */
export const getTrashList = (path: string) =>
  ARFactory({
    url: `/trash/list`,
    method: 'get',
    params: { path }
  }) as Promise<{
    succeed: boolean
    res: {
      result: Array<{
        type: string
        name: string
        link: string | null
      }>
      message: string
    }
  }>

/**
 * 删除回收站中的文件或文件夹
 * @param path 要删除的文件或文件夹路径
 * @returns 仅状态返回体，result 为 null
 */
export const deletePermanently = (path: string) =>
  ARFactory({
    url: `/trash/file`,
    method: 'delete',
    params: { path }
  }) as Promise<{
    succeed: boolean
    res: {
      result: null
      message: string
    }
  }>
