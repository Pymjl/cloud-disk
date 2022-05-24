<script lang="ts">
import { Component, defineComponent, h, inject, nextTick, onMounted, reactive, ref, Ref, toRefs } from 'vue'
import { NIcon, NInput, useDialog, useMessage } from 'naive-ui'
import { Add, Folder, Document, VolumeFileStorage, TrashCan } from '@vicons/carbon'
import {
  getFileList,
  getTrashList,
  uploadFile,
  newFolder,
  moveFile,
  renameFolder,
  moveToTrash,
  deletePermanently,
  recover
} from '@/api/files'
import TopNav from '@/components/public/TopNav.vue'
import AdminPanel from '@/components/admin/Admin.vue'
import PersonalInformation from '@/components/user/Personal.vue'
import CustomDropdown from '@/components/files/CustomDropdown.vue'
import FolderSelect from '@/components/files/FolderSelect.vue'

export default defineComponent({
  name: 'FilesPage',
  components: { TopNav, Add, CustomDropdown, AdminPanel, PersonalInformation, FolderSelect },
  setup() {
    // 文件列表项类型
    type FileItem = { type: string; name: string; link: string | null }

    const message = useMessage()
    const dialog = useDialog()
    const inputValue = ref('')
    const currentItem = ref({}) as Ref<FileItem>
    const showModal = ref(false)

    // 抽屉控制
    const personalActive = inject('personalActive') as Ref<boolean>
    const adminActive = inject('adminActive') as Ref<boolean>

    // 图标渲染工具函数
    function renderIcon(icon: Component) {
      return () => h(NIcon, null, { default: () => h(icon) })
    }

    // 组件内状态
    const state = reactive({
      // mode 指示显示何种文件列表
      mode: 'files',
      // 加载状态
      loading: true,
      // 当前路径，根目录为空字符串，目录间用 / 分隔，末尾无 /
      path: '',
      // 列表数据
      list: [] as FileItem[]
    })

    // 路径选择组件参数
    const folderSelectProps = reactive({
      mode: '',
      type: '',
      path: ''
    })

    // 文件列表
    const columns = [
      {
        title: '名称',
        key: 'name',
        render(row: FileItem) {
          return [
            row.type === 'dir' ? h(NIcon, { component: Folder, size: 22 }) : h(NIcon, { component: Document, size: 22 }),
            h('span', row.name)
          ]
        }
      }
    ]

    // 侧边菜单
    const menuOptions = [
      {
        label: '我的文件',
        icon: renderIcon(VolumeFileStorage),
        key: 'files'
      },
      {
        label: '回收站',
        icon: renderIcon(TrashCan),
        key: 'trash'
      }
    ]
    const handleMenuUpdate = (value: string) => {
      state.path = ''
      state.mode = value
      refreshList()
    }

    // 获取文件列表
    const refreshList = async () => {
      state.loading = true
      let func = null
      switch (state.mode) {
        case 'files':
          func = getFileList
          break
        case 'trash':
          func = getTrashList
          break
      }
      if (func) {
        await func(state.path).then(
          ({ succeed, res }) => {
            if (succeed) {
              if (res.result) {
                // 处理带 / 后缀的文件夹名称
                state.list = res.result.map((item) => {
                  if (item.type === 'dir' && item.name.endsWith('/')) {
                    item.name = item.name.slice(0, -1)
                  }
                  return item
                })
              } else {
                // 进入了空文件夹
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
      }
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

    // 上传文件
    const upload = () => {
      const input = document.createElement('input') as HTMLInputElement
      input.type = 'file'
      input.onchange = () => {
        if (!input.files) return
        if (!input.files[0]) return
        const msg = message.loading('上传中...', { duration: 0 })
        uploadFile(state.path ? `${state.path}/` : '', input.files[0])
          .then(
            ({ succeed, res }) => {
              if (succeed) {
                refreshList()
                message.success('文件上传成功')
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
                message.error('文件上传失败，请检查您的网络')
              }
            }
          )
          .finally(() => {
            msg.destroy()
          })
      }
      input.click()
    }

    // 新建文件夹
    const mkdir = () => {
      dialog.info({
        title: '新建文件夹',
        content: () =>
          h(NInput, {
            placeholder: '请输入文件夹名称',
            onUpdateValue: (value) => (inputValue.value = value)
          }),
        positiveText: '确定',
        negativeText: '取消',
        onPositiveClick: () => {
          if (inputValue.value) {
            newFolder(state.path ? `${state.path}/${inputValue.value}` : inputValue.value).then(
              ({ succeed, res }) => {
                if (succeed) {
                  refreshList()
                  message.success('新建文件夹成功')
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
          } else {
            message.warning('请输入文件夹名称')
            return false
          }
        }
      })
    }

    // 菜单
    const dropdownType = ref('')
    const dropdownFlag = ref(false)
    const xRef = ref(0)
    const yRef = ref(0)

    // 新建按钮菜单
    const openNewDropdown = (e: MouseEvent) => {
      if (e.target instanceof HTMLElement && !e.target.classList.contains('n-data-table-td')) e.preventDefault()
      dropdownType.value = 'new'
      xRef.value = e.clientX
      yRef.value = e.clientY
      nextTick().then(() => {
        // 通过更新 dropdownFlag 触发组件更新
        dropdownFlag.value = !dropdownFlag.value
      })
    }

    // 文件列表列处理
    const rowProps = (row: FileItem) => {
      return {
        ondblclick: () => {
          // 在回收站中不允许打开文件或目录
          if (state.mode === 'trash') return
          if (row.type === 'dir') {
            if (!state.path) {
              state.path = row.name
            } else {
              state.path = `${state.path}/${row.name}`
            }
            refreshList()
          } else {
            // 打开文件
            if (row.link) window.open(row.link)
          }
        },
        onContextmenu: (e: MouseEvent) => {
          e.preventDefault()
          // 将当前行数据存入 currentItem
          currentItem.value = row
          // 这里通过 state.mode 先判断在哪个模式，再传入不同的菜单类型
          dropdownType.value = state.mode === 'files' ? row.type : 'trash'
          xRef.value = e.clientX
          yRef.value = e.clientY
          nextTick().then(() => {
            // 通过更新 dropdownFlag 触发组件更新
            dropdownFlag.value = !dropdownFlag.value
          })
        }
      }
    }

    // 右键菜单选择处理
    const handleSelected = (value: string) => {
      switch (value) {
        case 'new-folder':
          mkdir()
          break
        case 'upload-file':
          upload()
          break
        case 'rename-file':
          inputValue.value = currentItem.value.name
          dialog.info({
            title: '重命名文件',
            content: () =>
              h(NInput, {
                defaultValue: inputValue.value,
                placeholder: '请输入文件名',
                onUpdateValue: (value) => (inputValue.value = value)
              }),
            positiveText: '确定',
            negativeText: '取消',
            onPositiveClick: () => {
              if (inputValue.value) {
                moveFile(
                  `${state.path ? state.path + '/' : ''}${currentItem.value.name}`,
                  `${state.path ? state.path + '/' : ''}${inputValue.value}`
                ).then(
                  ({ succeed, res }) => {
                    if (succeed) {
                      refreshList()
                      message.success('重命名文件成功')
                    } else {
                      message.warning(res.message)
                      return false
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
              } else {
                message.warning('请输入文件名')
              }
            }
          })
          break
        case 'copy-file':
          folderSelectProps.type = 'file'
          folderSelectProps.mode = 'copy'
          folderSelectProps.path = `${state.path ? state.path + '/' : ''}${currentItem.value.name}`
          showModal.value = true
          break
        case 'move-file':
          folderSelectProps.type = 'file'
          folderSelectProps.mode = 'move'
          folderSelectProps.path = `${state.path ? state.path + '/' : ''}${currentItem.value.name}`
          showModal.value = true
          break
        case 'del-file':
          moveToTrash(`${state.path ? state.path + '/' : ''}${currentItem.value.name}`).then(
            ({ succeed, res }) => {
              if (succeed) {
                refreshList()
                message.success('删除文件成功')
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
        case 'rename-folder':
          inputValue.value = currentItem.value.name
          dialog.info({
            title: '重命名文件夹',
            content: () =>
              h(NInput, {
                defaultValue: inputValue.value,
                placeholder: '请输入文件夹名',
                onUpdateValue: (value) => (inputValue.value = value)
              }),
            positiveText: '确定',
            negativeText: '取消',
            onPositiveClick: () => {
              if (inputValue.value) {
                renameFolder(
                  `${state.path ? state.path + '/' : ''}${currentItem.value.name}/`,
                  `${state.path ? state.path + '/' : ''}${inputValue.value}/`
                ).then(
                  ({ succeed, res }) => {
                    if (succeed) {
                      refreshList()
                      message.success('重命名文件夹成功')
                    } else {
                      message.warning(res.message)
                      return false
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
              } else {
                message.warning('请输入文件夹名')
              }
            }
          })
          break
        case 'copy-folder':
          folderSelectProps.type = 'dir'
          folderSelectProps.mode = 'copy'
          folderSelectProps.path = `${state.path ? state.path + '/' : ''}${currentItem.value.name}/`
          showModal.value = true
          break
        case 'move-folder':
          folderSelectProps.type = 'dir'
          folderSelectProps.mode = 'move'
          folderSelectProps.path = `${state.path ? state.path + '/' : ''}${currentItem.value.name}/`
          showModal.value = true
          break
        case 'del-folder':
          moveToTrash(`${state.path ? state.path + '/' : ''}${currentItem.value.name}/`).then(
            ({ succeed, res }) => {
              if (succeed) {
                refreshList()
                message.success('删除文件夹成功')
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
        case 'restore':
          recover(`${state.path ? state.path + '/' : ''}${currentItem.value.name}/`).then(
            ({ succeed, res }) => {
              if (succeed) {
                refreshList()
                message.success('恢复成功')
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
        case 'delete':
          deletePermanently(
            currentItem.value.type === 'file'
              ? `${state.path ? state.path + '/' : ''}${currentItem.value.name}`
              : `${state.path ? state.path + '/' : ''}${currentItem.value.name}/`
          ).then(
            ({ succeed, res }) => {
              if (succeed) {
                refreshList()
                message.success(`永久删除${currentItem.value.type === 'file' ? '文件' : '文件夹'}成功`)
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
    }

    // 处理文件夹选择
    const handleFolderSelected = () => {
      showModal.value = false
      refreshList()
    }

    // 清空回收站
    const emptyTrash = () => {
      deletePermanently('').then(
        ({ succeed, res }) => {
          if (succeed) {
            refreshList()
            message.success('清空回收站成功')
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

    onMounted(() => {
      refreshList()
    })

    return {
      showModal,
      personalActive,
      adminActive,
      menuOptions,
      columns,
      dropdownType,
      dropdownFlag,
      xRef,
      yRef,
      folderSelectProps,
      ...toRefs(state),
      rowProps,
      openNewDropdown,
      handleMenuUpdate,
      backParent,
      handleFolderSelected,
      handleSelected,
      emptyTrash
    }
  }
})
</script>

<template>
  <TopNav />
  <NLayout has-sider class="files-container">
    <NLayoutSider bordered>
      <!-- 蓝色新建大按钮 -->
      <div class="new-btn">
        <NButton secondary type="info" size="large" round :disabled="mode !== 'files'" @click="openNewDropdown">
          <template #icon>
            <NIcon>
              <Add />
            </NIcon>
          </template>
          新建
        </NButton>
      </div>
      <!-- 侧栏导航 -->
      <NMenu default-value="files" :options="menuOptions" @update:value="handleMenuUpdate" />
    </NLayoutSider>
    <NLayoutContent>
      <div class="toolbar">
        <NBreadcrumb>
          <NBreadcrumbItem v-if="path.includes('/')" @click="backParent">{{ path.split('/')[path.split('/').length - 2] }}</NBreadcrumbItem>
          <NBreadcrumbItem v-else @click="backParent">{{ mode === 'files' ? '我的文件' : '回收站' }}</NBreadcrumbItem>
          <NBreadcrumbItem v-if="path">{{ path.substring(path.lastIndexOf('/') + 1) }}</NBreadcrumbItem>
        </NBreadcrumb>
        <NButton v-if="mode === 'trash'" secondary style="margin-left: auto" @click="emptyTrash">清空回收站</NButton>
      </div>
      <!-- 文件列表 -->
      <NDataTable class="data-list" :loading="loading" :columns="columns" :row-props="rowProps" :data="list" :bordered="false" />
    </NLayoutContent>
  </NLayout>
  <!-- 复制/移动位置选择 -->
  <NModal v-model:show="showModal" preset="card" size="small" title="请选择目标文件夹" style="max-width: 25rem; max-height: 100vh">
    <FolderSelect
      :mode="folderSelectProps.mode"
      :type="folderSelectProps.type"
      :path="folderSelectProps.path"
      @selected="handleFolderSelected"
    />
  </NModal>
  <!-- 右键菜单 -->
  <CustomDropdown :flag="dropdownFlag" :type="dropdownType" :x="xRef" :y="yRef" @selected="handleSelected" />
  <!-- 管理面板 -->
  <NDrawer v-model:show="adminActive" :width="768">
    <NDrawerContent title="管理面板" closable>
      <AdminPanel />
    </NDrawerContent>
  </NDrawer>
  <!-- 个人信息 -->
  <NDrawer v-model:show="personalActive" :width="384">
    <NDrawerContent title="个人信息" closable>
      <PersonalInformation />
    </NDrawerContent>
  </NDrawer>
</template>

<style lang="scss">
.files-container {
  height: calc(100vh - 3.5rem);

  .new-btn {
    padding: 0.75rem 1rem 0.5rem;
  }

  .toolbar {
    display: flex;
    align-items: center;
    height: calc(2.75rem - 1px);
    padding: 0 0.75rem;
    border-bottom: 1px solid #eee;

    .n-breadcrumb-item {
      font-size: 1rem;
    }
  }

  .n-data-table {
    height: calc(100vh - 6.25rem);
  }

  .n-data-table-thead,
  .n-data-table-th {
    background-color: transparent;
  }

  .n-data-table-td {
    display: flex;
    align-items: center;

    span {
      margin-left: 0.5rem;
      font-size: 1rem;
      line-height: 22px;
    }
  }
}
</style>
