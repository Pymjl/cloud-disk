<script lang="ts">
import { defineComponent, h, reactive, onMounted, toRefs } from 'vue'
import { DataTableColumns, NButton, NTag, useDialog, useMessage } from 'naive-ui'
import { addAdmin, delUser, getUserList } from '@/api/admin'

export default defineComponent({
  name: 'AdminPanel',
  setup() {
    // 定义 UserInfo 内容
    type UserInfo = {
      id: number
      username: string
      nickname: string
      avatar: string
      identity: number
      createTime: number
      updateTime: number
    }

    // 组件内部用到的一些状态
    const state = reactive({
      loading: true,
      list: [] as UserInfo[],
      currentPage: 1,
      pagination: {
        pageSize: 6,
        itemCount: 0,
        pageCount: 0
      }
    })

    // 刷新列表
    const message = useMessage()
    const dialog = useDialog()
    const refreshList = async () => {
      state.loading = true
      await getUserList(state.currentPage, state.pagination.pageSize).then(
        ({ succeed, res }) => {
          if (succeed) {
            state.list = res.records
            state.pagination.pageSize = res.pageSize
            state.pagination.itemCount = res.totalElements
            state.pagination.pageCount = res.totalPages
          } else {
            message.warning(res.message)
          }
        },
        (err) => {
          if (err.response) {
            // 服务器响应状态码不属于 2xx
            message.error(err.response.data.error)
          } else if (err.request) {
            // 未收到服务器响应
            message.error('请求发送失败，请检查您的网络')
          }
        }
      )
      state.loading = false
    }
    const handlePageChange = (page: number) => {
      state.currentPage = page
      refreshList()
    }

    // 表格列
    const columns: DataTableColumns<UserInfo> = [
      {
        title: '用户名',
        key: 'username',
        align: 'center'
      },
      {
        title: '昵称',
        key: 'nickname',
        align: 'center'
      },
      {
        title: '权限',
        key: 'identity',
        render(row: UserInfo) {
          return h(
            NTag,
            {
              type: row.identity === 0 ? 'info' : 'warning',
              onClick: () => {
                if (row.identity === 0) {
                  dialog.warning({
                    title: '警告',
                    content: `你确定将 ${row.username} 提升为管理员吗？`,
                    positiveText: '确定',
                    negativeText: '取消',
                    onPositiveClick: () => {
                      addAdmin(row.id).then(
                        ({ succeed, res }) => {
                          if (succeed) {
                            refreshList()
                            message.success('操作成功')
                          } else {
                            message.warning(res.message)
                          }
                        },
                        (err) => {
                          if (err.response) {
                            // 服务器响应状态码不属于 2xx
                            message.error(err.response.data.error)
                          } else if (err.request) {
                            // 未收到服务器响应
                            message.error('请求发送失败，请检查您的网络')
                          }
                        }
                      )
                    }
                  })
                }
              }
            },
            { default: () => (row.identity === 0 ? '普通用户' : '管理员') }
          )
        },
        align: 'center'
      },
      {
        title: '操作',
        key: 'actions',
        render(row: UserInfo) {
          return h(
            NButton,
            {
              type: 'error',
              secondary: true,
              onClick: () => {
                dialog.warning({
                  title: '警告',
                  content: `你确定要删除 ${row.username} 吗？`,
                  positiveText: '确定',
                  negativeText: '取消',
                  onPositiveClick: () => {
                    delUser(row.id).then(
                      ({ succeed, res }) => {
                        if (succeed) {
                          refreshList()
                          message.success('删除成功')
                        } else {
                          message.warning(res.message)
                        }
                      },
                      (err) => {
                        if (err.response) {
                          // 服务器响应状态码不属于 2xx
                          message.error(err.response.data.error)
                        } else if (err.request) {
                          // 未收到服务器响应
                          message.error('请求发送失败，请检查您的网络')
                        }
                      }
                    )
                  }
                })
              }
            },
            { default: () => '删除' }
          )
        },
        align: 'center'
      }
    ]

    onMounted(() => {
      refreshList()
    })

    return {
      columns,
      ...toRefs(state),
      handlePageChange
    }
  }
})
</script>

<template>
  <NDataTable remote :loading="loading" :columns="columns" :data="list" :pagination="pagination" @update:page="handlePageChange" />
</template>
