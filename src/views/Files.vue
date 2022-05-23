<script lang="ts">
import { Component, defineComponent, h, inject, nextTick, onMounted, reactive, ref, Ref, toRefs } from 'vue'
import { NIcon, useMessage } from 'naive-ui'
import { Add, Folder, Document, VolumeFileStorage, TrashCan } from '@vicons/carbon'
import { getFileList } from '@/api/files'
import TopNav from '@/components/public/TopNav.vue'
import AdminPanel from '@/components/admin/Admin.vue'
import PersonalInformation from '@/components/user/Personal.vue'
import CustomDropdown from '@/components/files/CustomDropdown.vue'

export default defineComponent({
  name: 'FilesPage',
  components: { TopNav, Add, CustomDropdown, AdminPanel, PersonalInformation },
  setup() {
    const message = useMessage()

    // 抽屉控制
    const personalActive = inject('personalActive') as Ref<boolean>
    const adminActive = inject('adminActive') as Ref<boolean>

    // 图标渲染工具函数
    function renderIcon(icon: Component) {
      return () => h(NIcon, null, { default: () => h(icon) })
    }

    // 文件列表项类型
    type FileItem = { type: string; name: string; link: string | null }

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
      state.mode = value
    }

    // 获取文件列表
    const fetchList = () => {
      state.loading = true
      getFileList(state.path).then(
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
      state.loading = false
    }

    // 返回上一级
    const backParent = () => {
      if (state.path === '') return
      const path = state.path.split('/')
      path.pop()
      state.path = path.join('/')
      fetchList()
    }

    // 打开右键菜单
    const dropdownType = ref('')
    const dropdownFlag = ref(false)
    const xRef = ref(0)
    const yRef = ref(0)
    const openNewDropdown = (e: MouseEvent) => {
      e.preventDefault()
      dropdownType.value = 'new'
      xRef.value = e.clientX
      yRef.value = e.clientY
      nextTick().then(() => {
        // 通过更新 dropdownFlag 触发组件更新
        dropdownFlag.value = !dropdownFlag.value
      })
    }

    onMounted(() => {
      fetchList()
    })

    return {
      personalActive,
      adminActive,
      menuOptions,
      columns,
      dropdownType,
      dropdownFlag,
      xRef,
      yRef,
      ...toRefs(state),
      handleMenuUpdate,
      backParent,
      openNewDropdown
    }
  }
})
</script>

<template>
  <TopNav />
  <NLayout has-sider class="files-container">
    <NLayoutSider bordered>
      <div class="new-btn">
        <NButton secondary type="info" size="large" round @click="openNewDropdown">
          <template #icon>
            <NIcon>
              <Add />
            </NIcon>
          </template>
          新建
        </NButton>
      </div>
      <NMenu default-value="files" :options="menuOptions" @update:value="handleMenuUpdate" />
    </NLayoutSider>
    <NLayoutContent>
      <div class="toolbar">
        <NBreadcrumb>
          <NBreadcrumbItem v-if="path.includes('/')" @click="backParent">{{ path.split('/')[path.split('/').length - 2] }}</NBreadcrumbItem>
          <NBreadcrumbItem v-else @click="backParent">{{ mode === 'files' ? '我的文件' : '回收站' }}</NBreadcrumbItem>
          <NBreadcrumbItem v-if="path">{{ path.substring(path.lastIndexOf('/') + 1) }}</NBreadcrumbItem>
        </NBreadcrumb>
      </div>
      <NDataTable :loading="loading" :columns="columns" :data="list" :bordered="false" />
    </NLayoutContent>
  </NLayout>
  <CustomDropdown :flag="dropdownFlag" :type="dropdownType" :x="xRef" :y="yRef" />
  <NDrawer v-model:show="adminActive" :width="768">
    <NDrawerContent title="管理面板" closable>
      <AdminPanel />
    </NDrawerContent>
  </NDrawer>
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
    padding: 0.5rem 0.75rem;
    border-bottom: 1px solid #eee;

    .n-breadcrumb-item {
      font-size: 1rem;
    }
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
