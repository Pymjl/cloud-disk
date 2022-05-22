<script lang="ts">
import { Component, defineComponent, h, inject, reactive, Ref, toRefs } from 'vue'
import { NIcon } from 'naive-ui'
import { Add, Folder, Document, VolumeFileStorage, TrashCan } from '@vicons/carbon'
import TopNav from '@/components/public/TopNav.vue'
import AdminPanel from '@/components/admin/Admin.vue'
import PersonalInformation from '@/components/user/Personal.vue'

export default defineComponent({
  name: 'FilesPage',
  components: { TopNav, Add, AdminPanel, PersonalInformation },
  setup() {
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
      loading: false,
      // 当前路径
      path: '',
      // 列表数据
      list: [
        { type: 'dir', name: '目录1', link: null },
        { type: 'dir', name: '目录2', link: null },
        { type: 'file', name: '文件1', link: '1.1.1.1' },
        { type: 'file', name: '文件2', link: '1.1.1.1' }
      ] as FileItem[]
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

    return { personalActive, adminActive, menuOptions, columns, ...toRefs(state), handleMenuUpdate }
  }
})
</script>

<template>
  <TopNav />
  <NLayout has-sider class="files-container">
    <NLayoutSider bordered>
      <div class="new-btn">
        <NButton secondary type="info" size="large" round>
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
      <NDataTable :loading="loading" :columns="columns" :data="list" />
    </NLayoutContent>
  </NLayout>
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

  .n-data-table-td {
    display: flex;
    align-items: center;

    span {
      margin-left: 0.5rem;
      font-size: 0.9rem;
      line-height: 22px;
    }
  }
}
</style>
