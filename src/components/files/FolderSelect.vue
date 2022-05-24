<script lang="ts">
import { defineComponent, HTMLAttributes, onMounted, reactive, toRefs } from 'vue'
import { useMessage } from 'naive-ui'
import { copyFile, getFileList, moveFile, moveFolder } from '@/api/files'

export default defineComponent({
  name: 'FolderSelect',
  props: {
    mode: {
      type: String,
      required: true
    },
    type: {
      type: String,
      required: true
    },
    path: {
      type: String,
      required: true
    }
  },
  emits: ['selected'],
  setup(props, { emit }) {
    const message = useMessage()

    // 文件夹列表项类型
    type FolderItem = { name: string }

    // 组件内状态
    const state = reactive({
      // 加载状态
      loading: true,
      // 当前路径，根目录为空字符串，目录间用 / 分隔，末尾无 /
      path: '',
      // 列表数据
      list: [] as FolderItem[]
    })

    // 表格列
    const columns = [
      {
        title: '名称',
        key: 'name'
      }
    ]

    // 文件列表列处理
    const rowProps = (row: FolderItem) => {
      return {
        ondblclick: () => {
          if (!state.path) {
            state.path = row.name
          } else {
            state.path = `${state.path}/${row.name}`
          }
          refreshList()
        }
      } as HTMLAttributes
    }

    // 获取文件夹列表
    const refreshList = async () => {
      state.loading = true
      await getFileList(state.path).then(
        ({ succeed, res }) => {
          if (succeed) {
            if (res.result) {
              // 去掉文件项
              state.list = res.result.filter((item) => item.type === 'dir')
              // 去掉文件夹名称中的 / 后缀
              state.list = state.list.map((item) => ({ name: item.name.slice(0, -1) }))
            } else {
              state.list = []
            }
          } else {
            state.path = ''
            state.list = []
            message.warning(res.message)
          }
        },
        (err) => {
          if (err.response) {
            // 服务器响应状态码不属于 2xx
            message.error(err.response.data.error)
          } else if (err.request) {
            // 未收到服务器响应
            message.error('登录请求发送失败，请检查您的网络')
          }
        }
      )
      state.loading = false
    }

    // 返回上一级
    const backParent = () => {
      if (state.path === '') return
      const path = state.path.split('/')
      path.pop()
      state.path = path.join('/')
      refreshList()
    }

    // 确认选择
    const confirm = () => {
      // 处理复制/移动操作
      switch (props.type) {
        case 'dir':
          switch (props.mode) {
            // 文件夹复制
            case 'copy':
              copyFile(
                props.path,
                `${state.path ? state.path + '/' : ''}${props.path.slice(props.path.slice(0, -1).lastIndexOf('/') + 1)}`
              ).then(
                ({ succeed, res }) => {
                  if (succeed) {
                    message.success('文件夹复制成功')
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
              break
            // 文件夹移动
            case 'move':
              moveFolder(props.path, state.path ? state.path + '/' : '').then(
                ({ succeed, res }) => {
                  if (succeed) {
                    message.success('文件夹移动成功')
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
              break
          }
          break
        case 'file':
          switch (props.mode) {
            // 文件复制
            case 'copy':
              copyFile(props.path, `${state.path ? state.path + '/' : ''}${props.path.slice(props.path.lastIndexOf('/') + 1)}`).then(
                ({ succeed, res }) => {
                  if (succeed) {
                    message.success('文件复制成功')
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
              break
            // 文件移动
            case 'move':
              moveFile(props.path, `${state.path ? state.path + '/' : ''}${props.path.slice(props.path.lastIndexOf('/') + 1)}`).then(
                ({ succeed, res }) => {
                  if (succeed) {
                    message.success('文件移动成功')
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
              break
          }
          break
      }
      // 弹出 selected 事件，该 Modal 将被关闭
      emit('selected')
    }

    onMounted(() => {
      refreshList()
    })

    return { columns, rowProps, ...toRefs(state), backParent, confirm }
  }
})
</script>

<template>
  <div class="folder-select">
    <div class="current">
      当前路径：
      <NBreadcrumb>
        <NBreadcrumbItem v-if="path.includes('/')" @click="backParent">{{ path.split('/')[path.split('/').length - 2] }}</NBreadcrumbItem>
        <NBreadcrumbItem v-else @click="backParent">我的文件</NBreadcrumbItem>
        <NBreadcrumbItem v-if="path">{{ path.substring(path.lastIndexOf('/') + 1) }}</NBreadcrumbItem>
      </NBreadcrumb>
    </div>
    <NDataTable :loading="loading" :columns="columns" :row-props="rowProps" :data="list" />
    <NButton type="primary" @click="confirm">确认</NButton>
  </div>
</template>

<style lang="scss">
.folder-select {
  .current {
    display: flex;
    margin-bottom: 0.75rem;
  }

  .n-data-table-thead {
    display: none;
  }

  .n-button {
    float: right;
    margin-top: 0.75rem;
  }
}
</style>
